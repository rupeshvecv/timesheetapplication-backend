package com.timesheetapplication.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.NotFilledDTO;
import com.timesheetapplication.dto.UserDTO;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.projection.TimesheetEntryProjection;
import com.timesheetapplication.service.MailService;
import com.timesheetapplication.service.TimesheetEntryService;

import feign.FeignException;

@Component
public class TimesheetMISScheduler {

	@Autowired
	private UserServiceFeignClient userServiceFeignClient;

	@Autowired
	private MailService mailService;

	@Autowired
	private TimesheetEntryService timesheetEntryService;

	@Scheduled(cron = "0 42 15 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	public void sendDailyNonEntryNotification() 
	{
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.now();

		System.out.println("📅 Generating Daily NonEntryNotification sendDailyNonEntryNotification from " + startDate + " to " + endDate);

		//1️.Fetch all PDD users safely
	    List<UserSummaryDTO> allPDDUsers;
	    try {
	        allPDDUsers = userServiceFeignClient.getAllOptimizedPDDUsers();
	    } catch (FeignException ex) {
	    	System.out.println("Error fetching PDD users "+ ex);
	        throw new BusinessException("TSMIS_004");
	    }

	    if (allPDDUsers == null || allPDDUsers.isEmpty()) {
	        throw new BusinessException("TSMIS_001");
	    }

   	    String[] toAllPDDRecipients			= extractEmailsFromSummaryUsers(allPDDUsers);
   	    System.out.println("TimesheetMISScheduler.sendDailyNonEntryNotification( toAllPDDRecipients.SIZE) "+toAllPDDRecipients.length);
   	    System.out.println("TimesheetMISScheduler.sendDailyNonEntryNotification( toAllPDDRecipients) "+toAllPDDRecipients);
	    
	   	 if (toAllPDDRecipients == null || toAllPDDRecipients.length == 0) {
	         throw new BusinessException("TSMIS_002");
	     }

			String htmlBody = buildDailyNonEntryHtmlMail();
			
			String subject = "Notification for Daily NonEntry ";
			String[] attachments = { "C:/reports/dailyNonEntry.xlsx" };

			System.out.println("📧 htmlBody: " + htmlBody);

			// Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			// BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Non Entry Notification for Timesheet mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
				throw new BusinessException("MIS_003");
			}
		}
	
	@Scheduled(cron = "0 55 09 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	public void sendWeeklyFridayTimesheetMis() 
	{
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.with(DayOfWeek.MONDAY);
		LocalDate endDate = today.with(DayOfWeek.FRIDAY);

		System.out.println("📅 Generating Weekly MIS sendWeeklyFridayTimesheetMis from " + startDate + " to " + endDate);

		//1️.Fetch all PDD users safely
	    List<UserSummaryDTO> allPDDUsers;
	    try {
	        allPDDUsers = userServiceFeignClient.getAllOptimizedPDDUsers();
	    } catch (FeignException ex) {
	    	System.out.println("Error fetching PDD users "+ ex);
	        throw new BusinessException("TSMIS_004");
	    }

	    if (allPDDUsers == null || allPDDUsers.isEmpty()) {
	        throw new BusinessException("TSMIS_001");
	    }
	    
		//Iterate user -> subordinate -> timesheet
		for (UserSummaryDTO manager : allPDDUsers) {
			System.out.println("AAAAA fetching subordinates for 1111 manager.userName() {}"+ manager.userName());
			//Fetch subordinate users for given manager
			List<UserSummaryDTO> subordinates;
	        try {
	            subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());
	        } catch (FeignException ex) {
	        	System.out.println("fetching subordinates for manager {}"+ manager.userName()+","+ ex);
	            throw new BusinessException("TSMIS_007");
	        }

	        if (subordinates == null || subordinates.isEmpty()) {
	            continue;
	        }
			//Map<EmployeeName, Map<Date, Status>>
			Map<String, Map<LocalDate, String>> weeklyData = new LinkedHashMap<>();

			for (UserSummaryDTO subordinate : subordinates) {

				Map<LocalDate, String> statusMap = new LinkedHashMap<>();
				LocalDate day = startDate;
				System.out.println("222222 fetching subordinates for subordinate.userName() {}"+ subordinate.userName());
				while (!day.isAfter(endDate)) {
					if (subordinate.userName() == null || subordinate.userName().isBlank()) {
				        day = day.plusDays(1);
				        continue;
				    }
					List<TimesheetEntryProjection> dayEntries = timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(subordinate.userName(), day, day);
					statusMap.put(day, dayEntries.isEmpty() ? "Not Filled" : "Filled");
					day = day.plusDays(1);
				}
				weeklyData.put(subordinate.firstName() + " " + subordinate.lastName(), statusMap);
			}

			String htmlBody = buildWeeklyHtmlMail(startDate, endDate, weeklyData,manager.firstName() + " " + manager.lastName());
			String subject = "Weekly Timesheet Report (" + startDate + " - " + endDate + ")";
			String[] attachments = { "C:/reports/WeeklyEntry.xlsx" };
			System.out.println("📧 htmlBody: " + htmlBody);

			//Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			//BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Daily Non Entry Mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
				throw new BusinessException("MIS_008");
			}
		}
	}
	
	@Scheduled(cron = "0 43 09 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	public void sendWeeklyTimesheetMis() 
	{
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.minusWeeks(1).with(DayOfWeek.MONDAY);
		// LocalDate endDate = startDate.plusDays(6); // Sunday
		// LocalDate endDate = startDate.plusDays(5); // Saturday
		LocalDate endDate = startDate.plusDays(4); // friday

		System.out.println("📅 Generating Weekly MIS sendWeeklyTimesheetMis from " + startDate + " to " + endDate);

		//1️.Fetch all PDD users safely
	    List<UserSummaryDTO> allPDDUsers;
	    try {
	        allPDDUsers = userServiceFeignClient.getAllOptimizedPDDUsers();
	    } catch (FeignException ex) {
	    	System.out.println("Error fetching PDD users "+ ex);
	        throw new BusinessException("TSMIS_004");
	    }

	    if (allPDDUsers == null || allPDDUsers.isEmpty()) {
	        throw new BusinessException("TSMIS_001");
	    }

		//Iterate user -> subordinate -> timesheet
		for (UserSummaryDTO manager : allPDDUsers) {
			//Fetch subordinate users for given manager
			//Fetch subordinate users for given manager
			List<UserSummaryDTO> subordinates;
	        try {
	            subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());
	        } catch (FeignException ex) {
	        	System.out.println("Error fetching subordinates for manager {}"+ manager.userName()+","+ ex);
	            throw new BusinessException("TSMIS_007");
	        }

	        if (subordinates == null || subordinates.isEmpty()) {
	            continue;
	        }

			//Map<EmployeeName, Map<Date, Status>>
			Map<String, Map<LocalDate, String>> weeklyData = new LinkedHashMap<>();

			for (UserSummaryDTO subordinate : subordinates) {

				Map<LocalDate, String> statusMap = new LinkedHashMap<>();
				LocalDate day = startDate;

				while (!day.isAfter(endDate)) {
					if (subordinate.userName() == null || subordinate.userName().isBlank()) {
				        day = day.plusDays(1);
				        continue;
				    }
					List<TimesheetEntryProjection> dayEntries = timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(subordinate.userName(), day, day);

					statusMap.put(day, dayEntries.isEmpty() ? "Not Filled" : "Filled");
					day = day.plusDays(1);
				}

				weeklyData.put(subordinate.firstName() + " " + subordinate.lastName(), statusMap);
			}

			//String htmlBody = buildWeeklyHtmlMail(startDate, endDate,
			//weeklyData,manager.userName());
			String htmlBody = buildWeeklyHtmlMail(startDate, endDate, weeklyData,
					manager.firstName() + " " + manager.lastName());
			String subject = "Weekly Timesheet Report (" + startDate + " - " + endDate + ")";
			String[] attachments = { "C:/reports/weekly-report.xlsx" };

			System.out.println("📧 Weekly Timesheet MIS Mail sent to Manager: " + manager.firstName());
			System.out.println("📧 htmlBody: " + htmlBody);

			//Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			//BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Weekly MIS Mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
				throw new BusinessException("MIS_008");
			}
		}
	}

	private String buildWeeklyHtmlMail(LocalDate startDate, LocalDate endDate,
			Map<String, Map<LocalDate, String>> weeklyData, String managerName) {

		StringBuilder html = new StringBuilder();

		html.append("<html>");
		html.append("<body style='font-family:Arial, sans-serif;'>");
		html.append(
				"<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>📌 Weekly Timesheet Status Report</h2>");

		html.append("<p><b>Duration:</b> ").append(startDate).append(" to ").append(endDate).append("</p>");

		html.append("<p>Dear ").append(managerName).append(",</p>");
		html.append("<p>Please find below the weekly day-wise timesheet entry status for your reportees:</p><br>");

		html.append("<table border='1' cellspacing='0' cellpadding='6' style='border-collapse:collapse;width:100%;'>");

		//Header Row
		html.append("<tr style='background-color:#2E86C1;color:white;text-align:center;font-weight:bold;'>");
		html.append("<th>Employee Name</th>");

		LocalDate d = startDate;
		while (!d.isAfter(endDate)) {
			html.append("<th>").append(d).append("</th>");
			d = d.plusDays(1);
		}
		html.append("</tr>");

		//Employee rows
		for (Map.Entry<String, Map<LocalDate, String>> entry : weeklyData.entrySet()) {
			html.append("<tr>");
			html.append("<td>").append(entry.getKey()).append("</td>");

			LocalDate day = startDate;
			while (!day.isAfter(endDate)) {
				String status = entry.getValue().getOrDefault(day, "Not Filled");
				String color = status.equalsIgnoreCase("Filled") ? "#90EE90" : "#F08080";

				html.append("<td style='text-align:center; color:").append(color).append("; font-weight:bold;'>")
						.append(status).append("</td>");

				day = day.plusDays(1);
			}
			html.append("</tr>");
		}

		html.append("</table><br>");
		html.append("<p>Regards,<br><b>Timesheet Admin</b></p>");
		html.append("</body></html>");

		return html.toString();
	}

	private String buildDailyNonEntryHtmlMail() 
	{
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<body style='font-family:Arial, sans-serif;'>");

		//Title
		html.append("<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>📌 Daily Timesheet Reminder</h2>");

		//Message content
		html.append("<p style='font-size:15px;'><b style='color:#2E86C1;'>Dear All,</b></p>");
		html.append("<p style='font-size:14px; line-height:1.6;'>Please fill your time sheet.<br>");
		html.append("If you have already filled, please ignore this mail.</p>");

		//Button Link
		html.append("<p style='margin-top:15px; margin-bottom:15px;'>");
		html.append("<a href='https://sss.sss.net/Timesheet' ");
		html.append("style='background-color:#2E86C1; color:white; padding:10px 18px; text-decoration:none; ");
		html.append("border-radius:6px; font-weight:bold;'>");
		html.append("➤ Open Timesheet</a></p>");

		//Regards section
		html.append("<p style='font-size:14px;'>Regards,<br><b>Timesheet Admin</b></p>");

		//Optional footer
		html.append("<hr style='border:0; border-top:1px solid #ccc; margin-top:25px;'>");
		html.append("<p style='font-size:12px; color:#888;'>This is an automated system generated message. Please do not reply.</p>");

		html.append("</body>");
		html.append("</html>");

		return html.toString();
	}

	private String[] extractEmailsFromUsers(List<UserDTO> users) {
		if (users == null || users.isEmpty()) {
			System.out.println("⚠️ No users provided to extract emails!");
			return new String[0];
		}

		List<String> emailList = users.stream().map(UserDTO::getEmail)
				.filter(email -> email != null && !email.isEmpty()).toList();

		if (emailList.isEmpty()) {
			System.out.println("⚠️ No valid email IDs found in user list!");
			return new String[0];
		}

		String[] emailArray = emailList.toArray(new String[0]);
		System.out.println("✅ Extracted email array: " + Arrays.toString(emailArray));
		return emailArray;
	}

	private String safe(Object value) {
		return value == null ? "" : value.toString();
	}
	
	 private String getLoggedInUsername() 
	 {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return "SYSTEM"; // fallback
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
	 
	 private String[] extractEmailsFromSummaryUsers(List<UserSummaryDTO> users) {
		if (users == null || users.isEmpty()) {
			System.out.println("⚠️ No users provided to extract emails!");
			return new String[0];
		}

		List<String> emailList = users.stream().map(UserSummaryDTO::email)
				.filter(email -> email != null && !email.isEmpty()).toList();

		if (emailList.isEmpty()) {
			System.out.println("⚠️ No valid email IDs found in user list!");
			return new String[0];
		}

		String[] emailArray = emailList.toArray(new String[0]);
		System.out.println("✅ Extracted email array: " + Arrays.toString(emailArray));
		return emailArray;
	}
	 
	 @Scheduled(cron = "0 44 11 * * *")
	 //@Scheduled(cron = "0 01 16 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	 public void sendWeeklyAllFunctionHeadTimesheetMis() {

		    LocalDate today = LocalDate.now();
		    LocalDate startDate = today.minusWeeks(1).with(DayOfWeek.MONDAY);
		    LocalDate endDate = startDate.plusDays(4);

		    // ========== START HTML BODY ==========
		    StringBuilder html = new StringBuilder();

		    html.append("<html><body style='font-family:Arial, sans-serif;'>");

		    html.append("<h2 style='color:#2E86C1;border-bottom:3px solid #2E86C1;padding-bottom:6px;'>")
		        .append("📊 Weekly Timesheet MIS Summary (All Function Heads)")
		        .append("</h2>");

		    html.append("<p><b>Duration:</b> ").append(startDate).append(" to ").append(endDate).append("</p>");

		    html.append("<table border='1' cellspacing='0' cellpadding='8' style='border-collapse:collapse;width:100%;'>");

		    // table header
		    html.append("<tr style='background-color:#2E86C1;color:white;text-align:center;font-weight:bold;'>")
		        .append("<th>Functional Head</th>")
		        .append("<th>Total Team Members</th>")
		        .append("<th>Filled Timesheet</th>")
		        .append("<th>Not Filled Timesheet</th>")
		        .append("</tr>");

		    // ===== FETCH ALL FUNCTION HEADS =====
		    List<UserSummaryDTO> allFunctionHeads = userServiceFeignClient.getAllFunctionHead();
		    //sort alphabetically
		    allFunctionHeads.sort(Comparator.comparing(h -> (h.firstName() + " " + h.lastName()).toLowerCase()));
		    
		    for (UserSummaryDTO manager : allFunctionHeads) {

		        List<UserSummaryDTO> subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());
		        if (subordinates == null || subordinates.isEmpty()) continue;

		        int totalMembers = subordinates.size();
		        int filledCount = 0;
		        int notFilledCount = 0;

		        List<NotFilledDTO> notFilledList = new ArrayList<>();

		        for (UserSummaryDTO emp : subordinates) {

		            int workingDays = 0;
		            int filledDays = 0;

		            LocalDate day = startDate;

		            while (!day.isAfter(endDate)) {

		                List<TimesheetEntryProjection> entries =
		                        timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(
		                                emp.userName(), day, day);

		                workingDays++;
		                if (!entries.isEmpty()) filledDays++;

		                day = day.plusDays(1);
		            }

		            if (filledDays == workingDays) {
		                filledCount++;
		            } else {
		                notFilledCount++;

		                notFilledList.add(
		                        new NotFilledDTO(
		                                emp.firstName() + " " + emp.lastName(),
		                                manager.firstName() + " " + manager.lastName(),
		                                workingDays,
		                                filledDays
		                        )
		                );
		            }
		        }

		        // ---------- hyperlink for popup ----------
		        String notFilledLink =
		                "http://localhost:9082/api/timesheetapplication/timesheetmis/not-filled-list"
		                        + "?head=" + manager.userName()
		                        + "&from=" + startDate
		                        + "&to=" + endDate
		                        + "&headFullName=" + manager.firstName() + " " + manager.lastName();

		        // ---------- add ROW into SAME TABLE ----------
		        html.append("<tr style='text-align:center;'>")
		            .append("<td>").append(manager.firstName()).append(" ").append(manager.lastName()).append("</td>")
		            .append("<td>").append(totalMembers).append("</td>")
		            .append("<td style='color:green;font-weight:bold;'>").append(filledCount).append("</td>")
		            .append("<td><a href='").append(notFilledLink)
		            .append("' style='color:red;font-weight:bold;text-decoration:none;'>")
		            .append(notFilledCount)
		            .append("</a></td>")
		            .append("</tr>");
		    }

		    //close main table and email body
		    html.append("</table><br>");
		    html.append("<p>Regards,<br><b>Timesheet Admin</b></p>");
		    html.append("</body></html>");

		    String subject = "Weekly Timesheet MIS (" + startDate + " - " + endDate + ")";

		    String htmlBody = html.toString();

		    System.out.println(htmlBody);

			String[] attachments = { "C:/reports/weekly-report.xlsx" };
			System.out.println("📧 htmlBody: " + htmlBody);

			//Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			//BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Weekly MIS Mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
				throw new BusinessException("MIS_008");
			}
		}

	 @Scheduled(cron = "0 34 15 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	 public void sendWeeklyAllFunctionHeadTimesheetMisOLD() {
		 LocalDate today = LocalDate.now();
		    LocalDate startDate = today.minusWeeks(1).with(DayOfWeek.MONDAY);
		    LocalDate endDate = startDate.plusDays(4);   // Monday–Friday
		    
		    String htmlBody ="-";
		    List<UserSummaryDTO> allFunctionHeads = userServiceFeignClient.getAllFunctionHead();
		    for (UserSummaryDTO manager : allFunctionHeads) {
		        List<UserSummaryDTO> subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());

		        if (subordinates == null || subordinates.isEmpty()) continue;

		        int totalMembers = subordinates.size();
		        int filledCount = 0;
		        int notFilledCount = 0;

		        List<NotFilledDTO> notFilledList = new ArrayList<>();
		        for (UserSummaryDTO emp : subordinates) {
		            int workingDays = 0;
		            int filledDays = 0;
		            LocalDate day = startDate;
		            while (!day.isAfter(endDate)) {
		                List<TimesheetEntryProjection> entries = timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(emp.userName(), day, day);
		                workingDays++;
		                if (!entries.isEmpty()) {
		                    filledDays++;
		                }
		                day = day.plusDays(1);
		            }

		            if (filledDays == workingDays) {
		                filledCount++;
		            } else {
		                notFilledCount++;

		                notFilledList.add(
		                        new NotFilledDTO(
		                                emp.firstName() + " " + emp.lastName(),
		                                manager.firstName() + " " + manager.lastName(),
		                                workingDays,
		                                filledDays
		                        )
		                );
		            }
		        }

		        htmlBody = buildAllFunctionHeadWeeklyHtmlMIS(
		                manager.firstName() + " " + manager.lastName(),
		                totalMembers,
		                filledCount,
		                notFilledCount,
		                notFilledList,
		                startDate,
		                endDate,manager.userName()
		        );
		        
		        System.out.println("📧 Weekly Timesheet MIS Mail sent to Manager: " + manager.firstName());
		    }
	        String subject = "Weekly Timesheet MIS (" + startDate + " - " + endDate + ")";
	        //String htmlBody = buildWeeklyHtmlMail(startDate, endDate,
			//weeklyData,manager.userName());
			String[] attachments = { "C:/reports/weekly-report.xlsx" };

			System.out.println("📧 htmlBody: " + htmlBody);

			//Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			//BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Weekly MIS Mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
				throw new BusinessException("MIS_008");
			}
	    //}
	}
	 
 private String buildAllFunctionHeadWeeklyHtmlMIS(
	        String functionHead,
	        int totalMembers,
	        int filled,
	        int notFilled,
	        List<NotFilledDTO> notFilledList,
	        LocalDate start,
	        LocalDate end,
	        String functionHeadName) {

	    String notFilledLink =
	            "http://localhost:9082/api/timesheetapplication/timesheetmis/not-filled-list"
	            + "?head=" + functionHeadName
	            + "&from=" + start
	            + "&to=" + end
	            + "&headFullName=" + functionHead;

	    StringBuilder html = new StringBuilder();

	    html.append("<html>");
	    html.append("<body style='font-family:Arial, sans-serif;'>");

	    //Title
	    html.append("<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>")
	        .append("📊 Weekly Timesheet MIS Summary")
	        .append("</h2>");

	    //Duration line
	    html.append("<p><b>Duration:</b> ").append(start).append(" to ").append(end).append("</p>");

	    //Message
	    html.append("<p><b>Function Head:</b> ").append(functionHead).append("</p>");
	    html.append("<p>Please find below the weekly MIS summary:</p><br>");

	    //Table
	    html.append("<table border='1' cellspacing='0' cellpadding='8' ")
	        .append("style='border-collapse:collapse;width:100%;'>");

	    //Header row
	    html.append("<tr style='background-color:#2E86C1;color:white;text-align:center;font-weight:bold;'>");
	    html.append("<th>Functional Head</th>");
	    html.append("<th>Total Team Members</th>");
	    html.append("<th>Filled Timesheet</th>");
	    html.append("<th>Not Filled Timesheet</th>");
	    html.append("</tr>");

	    //Data row
	    html.append("<tr style='text-align:center;'>");
	    html.append("<td>").append(functionHead).append("</td>");
	    html.append("<td>").append(totalMembers).append("</td>");
	    html.append("<td style='color:green;font-weight:bold;'>").append(filled).append("</td>");

	    html.append("<td>")
	        .append("<a href='").append(notFilledLink)
	        .append("' style='color:red;font-weight:bold;text-decoration:none;'>")
	        .append(notFilled)
	        .append("</a></td>");

	    html.append("</tr>");
	    html.append("</table><br>");

	    // Footer
	    html.append("<p>Regards,<br><b>Timesheet Admin</b></p>");
	    html.append("</body></html>");

	    return html.toString();
	}
 
 @Scheduled(cron = "0 05 15 * * *")
//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
 public void sendWeeklyFunctionHeadTimesheetMis() {
	 LocalDate today = LocalDate.now();
	    LocalDate startDate = today.minusWeeks(1).with(DayOfWeek.MONDAY);
	    LocalDate endDate = startDate.plusDays(4);   // Monday–Friday

	    List<UserSummaryDTO> allFunctionHeads = userServiceFeignClient.getAllFunctionHead();

	    for (UserSummaryDTO manager : allFunctionHeads) {

	        List<UserSummaryDTO> subordinates =
	                userServiceFeignClient.getSubordinateUsers(manager.userName());

	        if (subordinates == null || subordinates.isEmpty()) continue;

	        int totalMembers = subordinates.size();
	        int filledCount = 0;
	        int notFilledCount = 0;

	        List<NotFilledDTO> notFilledList = new ArrayList<>();

	        for (UserSummaryDTO emp : subordinates) {

	            int workingDays = 0;
	            int filledDays = 0;

	            LocalDate day = startDate;

	            while (!day.isAfter(endDate)) {

	                List<TimesheetEntryProjection> entries = timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(emp.userName(), day, day);

	                workingDays++;

	                if (!entries.isEmpty()) {
	                    filledDays++;
	                }

	                day = day.plusDays(1);
	            }

	            if (filledDays == workingDays) {
	                filledCount++;
	            } else {
	                notFilledCount++;

	                notFilledList.add(
	                        new NotFilledDTO(
	                                emp.firstName() + " " + emp.lastName(),
	                                manager.firstName() + " " + manager.lastName(),
	                                workingDays,
	                                filledDays
	                        )
	                );
	            }
	        }

	        String htmlBody = buildFunctionHeadWeeklyHtmlMIS(
	                manager.firstName() + " " + manager.lastName(),
	                totalMembers,
	                filledCount,
	                notFilledCount,
	                notFilledList,
	                startDate,
	                endDate,manager.userName()
	        );

        String subject = "Weekly Timesheet MIS (" + startDate + " - " + endDate + ")";
        System.out.println(htmlBody);

        //String htmlBody = buildWeeklyHtmlMail(startDate, endDate,
		//weeklyData,manager.userName());
		String[] attachments = { "C:/reports/weekly-report.xlsx" };

		System.out.println("📧 Weekly Timesheet MIS Mail sent to Manager: " + manager.firstName());
		System.out.println("📧 htmlBody: " + htmlBody);

		//Recipients (can be from DB/config)
		String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

		//BCC recipients
		String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

		try {
			mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
			System.out.println("✅ Weekly MIS Mail sent successfully!");
		} catch (Exception e) {
			System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
			throw new BusinessException("MIS_008");
		}
    }
}
	 
private String buildFunctionHeadWeeklyHtmlMIS(
	        String functionHead,
	        int totalMembers,
	        int filled,
	        int notFilled,
	        List<NotFilledDTO> notFilledList,
	        LocalDate start,
	        LocalDate end,
	        String functionHeadName) {

	    String notFilledLink =
	            "http://localhost:9082/api/timesheetapplication/timesheetmis/not-filled-list"
	            + "?head=" + functionHeadName
	            + "&from=" + start
	            + "&to=" + end
	            + "&headFullName=" + functionHead;

	    StringBuilder html = new StringBuilder();

	    html.append("<html>");
	    html.append("<body style='font-family:Arial, sans-serif;'>");

	    //Title
	    html.append("<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>")
	        .append("📊 Weekly Timesheet MIS Summary")
	        .append("</h2>");

	    //Duration line
	    html.append("<p><b>Duration:</b> ").append(start).append(" to ").append(end).append("</p>");

	    //Message
	    html.append("<p><b>Function Head:</b> ").append(functionHead).append("</p>");
	    html.append("<p>Please find below the weekly MIS summary:</p><br>");

	    //Table
	    html.append("<table border='1' cellspacing='0' cellpadding='8' ")
	        .append("style='border-collapse:collapse;width:100%;'>");

	    //Header row
	    html.append("<tr style='background-color:#2E86C1;color:white;text-align:center;font-weight:bold;'>");
	    html.append("<th>Functional Head</th>");
	    html.append("<th>Total Team Members</th>");
	    html.append("<th>Filled Timesheet</th>");
	    html.append("<th>Not Filled Timesheet</th>");
	    html.append("</tr>");

	    //Data row
	    html.append("<tr style='text-align:center;'>");
	    html.append("<td>").append(functionHead).append("</td>");
	    html.append("<td>").append(totalMembers).append("</td>");
	    html.append("<td style='color:green;font-weight:bold;'>").append(filled).append("</td>");

	    html.append("<td>")
	        .append("<a href='").append(notFilledLink)
	        .append("' style='color:red;font-weight:bold;text-decoration:none;'>")
	        .append(notFilled)
	        .append("</a></td>");

	    html.append("</tr>");
	    html.append("</table><br>");

	    // Footer
	    html.append("<p>Regards,<br><b>Timesheet Admin</b></p>");
	    html.append("</body></html>");

	    return html.toString();
	}

}
