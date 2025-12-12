package com.timesheetapplication.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.timesheetapplication.model.TimesheetEntry;
import com.timesheetapplication.projection.TimesheetEntryProjection;
import com.timesheetapplication.projection.TimesheetFillingReportProjection;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, Long> {
	
	@Query("SELECT COALESCE(SUM(t.hours), 0) FROM TimesheetEntry t " +
	           "WHERE t.userName = :user AND t.entryDate = :date")
    BigDecimal getTotalHoursForUserAndDate(@Param("user") String user,@Param("date") LocalDate date);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM TimesheetEntry t WHERE t.entryDate = :entryDate AND t.userName = :user")
	int deleteByEntryDateAndUserName(@Param("entryDate") LocalDate entryDate,@Param("user") String userName);
	
	boolean existsByEntryDateAndUserName(LocalDate date, String userName);
	
	List<TimesheetEntry> findByUserNameAndEntryDate(String user, LocalDate date);
	
	List<TimesheetEntry> findByUserNameAndEntryDateBetween(String userName, LocalDate from, LocalDate to);

	 @Query("""
     SELECT 
        t.id AS id,
        t.entryDate AS entryDate,
        FUNCTION('TIME_FORMAT', t.time, '%H:%i:%s') AS time,
        t.hours AS hours,
        t.userName AS userName,

        c.id AS categoryId,
        c.categoryName AS categoryName,

        p.id AS platformId,
        p.platformName AS platformName,

        pr.id AS projectId,
        pr.projectName AS projectName,

        a.id AS activityId,
        a.activityName AS activityName,

        t.details AS details,
        t.raisedOn AS raisedOn
    FROM TimesheetEntry t
    LEFT JOIN t.category c
    LEFT JOIN t.platform p
    LEFT JOIN t.project pr
    LEFT JOIN t.activity a
    WHERE t.userName = :user
      AND t.entryDate BETWEEN :startEntryDate AND :endEntryDate
    ORDER BY t.entryDate ASC
    """)
   List<TimesheetEntryProjection> findAllDateUserWiseOptimizedTimesheetEntries(@Param("user") String user,
                                                  @Param("startEntryDate") LocalDate startEntryDate,
                                                  @Param("endEntryDate") LocalDate endEntryDate);
	 
	 @Query("""
	    SELECT 
	        t.id AS id,
	        t.entryDate AS entryDate,
	         /* t.time AS time, */
	        FUNCTION('TIME_FORMAT', t.time, '%H:%i:%s') AS time,
	        t.hours AS hours,
	        t.userName AS userName,
	
	        c.id AS categoryId,
	        c.categoryName AS categoryName,
	
	        p.id AS platformId,
	        p.platformName AS platformName,
	
	        pr.id AS projectId,
	        pr.projectName AS projectName,
	
	        a.id AS activityId,
	        a.activityName AS activityName,
	
	        t.details AS details,
	        t.raisedOn AS raisedOn
	    FROM TimesheetEntry t
	    LEFT JOIN t.category c
	    LEFT JOIN t.platform p
	    LEFT JOIN t.project pr
	    LEFT JOIN t.activity a
	   WHERE t.entryDate BETWEEN :startEntryDate AND :endEntryDate
    ORDER BY t.entryDate ASC
    """)
	 List<TimesheetEntryProjection> findAllOptimizedTimesheetEntries(@Param("startEntryDate") LocalDate startEntryDate,@Param("endEntryDate") LocalDate endEntryDate);
		/*
		 * @Query(value = """ SELECT t.user_name AS userName, COUNT(t.entry_date) AS
		 * filledDays, (DATEDIFF(:endDate, :startDate) + 1) AS totalDays,
		 * (COUNT(t.entry_date) * 100.0 / (DATEDIFF(:endDate, :startDate) + 1)) AS
		 * percentageFilled FROM timesheetapplication.timesheet_entry t WHERE
		 * t.entry_date BETWEEN :startDate AND :endDate GROUP BY t.user_name ORDER BY
		 * filledDays DESC """, nativeQuery = true)
		 * List<TimesheetFillingReportProjection> getTimesheetReport(LocalDate
		 * startDate, LocalDate endDate);
		 */

	 @Query(value = """
	    WITH RECURSIVE date_range AS (
	        SELECT DATE_ADD(:startDate, INTERVAL seq DAY) AS dt
	        FROM timesheetapplication.seq_0_to_10000
	        WHERE DATE_ADD(:startDate, INTERVAL seq DAY) <= :endDate
	    ),
	    working_days AS (
	        SELECT dt
	        FROM date_range
	        WHERE DAYOFWEEK(dt) NOT IN (1, 7)       -- Exclude Sunday (1) & Saturday (7)
	        AND dt NOT IN (SELECT holiday_date FROM timesheetapplication.holiday_master)
	    )
	    SELECT
	        t.user_name AS userName,
	        COUNT(DISTINCT t.entry_date) AS filledDays,
	        (SELECT COUNT(*) FROM working_days) AS totalDays,
	        (COUNT(DISTINCT t.entry_date) * 100.0 / (SELECT COUNT(*) FROM working_days)) AS percentageFilled
	    FROM timesheetapplication.timesheet_entry t
	    WHERE t.entry_date IN (SELECT dt FROM working_days)   -- <=== Important Fix
	      AND t.entry_date BETWEEN :startDate AND :endDate
	    GROUP BY t.user_name
	    ORDER BY filledDays DESC
	    """, nativeQuery = true)
	 List<TimesheetFillingReportProjection> getTimesheetFilledUserReport(LocalDate startDate, LocalDate endDate);

	 @Query(value = """
			    WITH RECURSIVE date_range AS (
			        SELECT DATE_ADD(:startDate, INTERVAL seq DAY) AS dt
			        FROM timesheetapplication.seq_0_to_10000
			        WHERE DATE_ADD(:startDate, INTERVAL seq DAY) <= :endDate
			    ),
			    working_days AS (
			        SELECT dt
			        FROM date_range
			        WHERE DAYOFWEEK(dt) NOT IN (1, 7)   -- Exclude Sunday (1) & Saturday (7)
			        AND dt NOT IN (SELECT holiday_date FROM timesheetapplication.holiday_master)
			    )
			    SELECT
			        t.user_name AS userName,
			        COUNT(DISTINCT t.entry_date) AS filledDays,
			        (SELECT COUNT(*) FROM working_days) AS totalDays,
			        (COUNT(DISTINCT t.entry_date) * 100.0 / (SELECT COUNT(*) FROM working_days)) AS percentageFilled
			    FROM timesheetapplication.timesheet_entry t
			    WHERE t.entry_date IN (SELECT dt FROM working_days) -- <=== Important Fix
			      AND t.entry_date BETWEEN :startDate AND :endDate
			      AND (:userName IS NULL OR :userName = '' OR t.user_name = :userName)
			    GROUP BY t.user_name
			    ORDER BY filledDays DESC
			    """, nativeQuery = true)
			List<TimesheetFillingReportProjection> getTimesheetFilledUserReportByUser(
			        @Param("startDate") LocalDate startDate,
			        @Param("endDate") LocalDate endDate,
			        @Param("userName") String userName
			);

	 @Query(
			    value = "SELECT t.user_name AS userName, p.project_name AS projectName, SUM(t.hours) AS hours " +
			            "FROM timesheetapplication.timesheet_entry t " +
			            "JOIN project p ON p.id = t.project_id " +
			            "WHERE t.entry_date BETWEEN :startDate AND :endDate " +
			            "AND DAYOFWEEK(t.entry_date) NOT IN (1, 7) " +
			            "AND (:userName IS NULL OR :userName = '' OR t.user_name = :userName) " +
					    "AND (:projectName IS NULL OR :projectName = '' OR p.project_name = :projectName) " +
			            "GROUP BY t.user_name, p.project_name " +
			            "ORDER BY t.user_name",
			    nativeQuery = true
			)
	 List<Object[]> getUserProjectSummary(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,@Param("userName") String userName,@Param("projectName") String projectName);
}
