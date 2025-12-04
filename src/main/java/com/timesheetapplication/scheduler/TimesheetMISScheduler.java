package com.timesheetapplication.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.UserDTO;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.dto.WeeklyTimesheetDTO;
import com.timesheetapplication.projection.TimesheetEntryProjection;
import com.timesheetapplication.service.MailService;
import com.timesheetapplication.service.TimesheetEntryService;

@Component
public class TimesheetMISScheduler {

	@Autowired
	private UserServiceFeignClient userServiceFeignClient;

	@Autowired
	private MailService mailService;

	@Autowired
	private TimesheetEntryService timesheetEntryService;

	@Scheduled(cron = "0 30 15 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	public void sendWeeklyFridayTimesheetMis() 
	{
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.with(DayOfWeek.MONDAY);
		LocalDate endDate = today.with(DayOfWeek.FRIDAY);

		System.out.println("📅 Generating Weekly MIS sendWeeklyFridayTimesheetMis from " + startDate + " to " + endDate);

		// Convert LocalDate → LocalDateTime
		// LocalDateTime startDateTime = startDate.atStartOfDay();
		// LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		System.out.println("📅 Generating Weekly MIS startDate from " + startDate + " to endDate " + endDate);

		// Fetch all users
		List<UserSummaryDTO> allPDDUsers = userServiceFeignClient.getAllOptimizedPDDUsers();
		System.out.println(
				"sendWeeklyTimesheetMis(allPDDUsers.SIZE) " + allPDDUsers.size() + " :Total PDD users: " + allPDDUsers);

		if (allPDDUsers == null || allPDDUsers.isEmpty()) {
			System.out.println("⚠️ No users found!");
			return;
		}

		// Iterate user -> subordinate -> timesheet
		for (UserSummaryDTO manager : allPDDUsers) {
			// Fetch subordinate users for given manager
			List<UserSummaryDTO> subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());
			System.out.println("Subordinates for Manager " + manager.userName() + ": " + subordinates.size());

			if (subordinates.isEmpty()) {
				System.out.println("⚠️ No subordinates under Manager " + manager.userName());
				continue;
			}

			// Map<EmployeeName, Map<Date, Status>>
			Map<String, Map<LocalDate, String>> weeklyData = new LinkedHashMap<>();

			for (UserSummaryDTO subordinate : subordinates) {

				Map<LocalDate, String> statusMap = new LinkedHashMap<>();
				LocalDate day = startDate;

				while (!day.isAfter(endDate)) {
					List<TimesheetEntryProjection> dayEntries = timesheetEntryService
							.findForUserBetween(subordinate.userName(), day, day);

					statusMap.put(day, dayEntries.isEmpty() ? "Not Filled" : "Filled");
					day = day.plusDays(1);
				}

				weeklyData.put(subordinate.firstName() + " " + subordinate.lastName(), statusMap);
			}

			// String htmlBody = buildWeeklyHtmlMail(startDate, endDate,
			// weeklyData,manager.userName());
			String htmlBody = buildWeeklyHtmlMail(startDate, endDate, weeklyData,
					manager.firstName() + " " + manager.lastName());
			String subject = "Weekly Timesheet Report (" + startDate + " - " + endDate + ")";
			String[] attachments = { "C:/reports/weekly-BDRR-report.xlsx" };

			System.out.println("📧 Weekly Timesheet MIS Mail sent to Manager: " + manager.firstName());
			System.out.println("📧 htmlBody: " + htmlBody);

			// Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			// BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Weekly MIS Mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
			}
		}

	}
	
	@Scheduled(cron = "0 33 16 * * *")
	//@Scheduled(cron = "0 30 09 * * MON")//every Monday 9:30 AM
	public void sendWeeklyTimesheetMis() 
	{
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.minusWeeks(1).with(DayOfWeek.MONDAY);
		// LocalDate endDate = startDate.plusDays(6); // Sunday
		// LocalDate endDate = startDate.plusDays(5); // Saturday
		LocalDate endDate = startDate.plusDays(4); // friday

		System.out.println("📅 Generating Weekly MIS sendWeeklyTimesheetMis from " + startDate + " to " + endDate);

		// Convert LocalDate → LocalDateTime
		// LocalDateTime startDateTime = startDate.atStartOfDay();
		// LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		System.out.println("📅 Generating Weekly MIS startDate from " + startDate + " to endDate " + endDate);

		// Fetch all users
		List<UserSummaryDTO> allPDDUsers = userServiceFeignClient.getAllOptimizedPDDUsers();
		System.out.println(
				"sendWeeklyTimesheetMis(allPDDUsers.SIZE) " + allPDDUsers.size() + " :Total PDD users: " + allPDDUsers);

		if (allPDDUsers == null || allPDDUsers.isEmpty()) {
			System.out.println("⚠️ No users found!");
			return;
		}

		// Iterate user -> subordinate -> timesheet
		for (UserSummaryDTO manager : allPDDUsers) {
			// Fetch subordinate users for given manager
			List<UserSummaryDTO> subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());
			System.out.println("Subordinates for Manager " + manager.userName() + ": " + subordinates.size());

			if (subordinates.isEmpty()) {
				System.out.println("⚠️ No subordinates under Manager " + manager.userName());
				continue;
			}

			// Map<EmployeeName, Map<Date, Status>>
			Map<String, Map<LocalDate, String>> weeklyData = new LinkedHashMap<>();

			for (UserSummaryDTO subordinate : subordinates) {

				Map<LocalDate, String> statusMap = new LinkedHashMap<>();
				LocalDate day = startDate;

				while (!day.isAfter(endDate)) {
					List<TimesheetEntryProjection> dayEntries = timesheetEntryService
							.findForUserBetween(subordinate.userName(), day, day);

					statusMap.put(day, dayEntries.isEmpty() ? "Not Filled" : "Filled");
					day = day.plusDays(1);
				}

				weeklyData.put(subordinate.firstName() + " " + subordinate.lastName(), statusMap);
			}

			// String htmlBody = buildWeeklyHtmlMail(startDate, endDate,
			// weeklyData,manager.userName());
			String htmlBody = buildWeeklyHtmlMail(startDate, endDate, weeklyData,
					manager.firstName() + " " + manager.lastName());
			String subject = "Weekly Timesheet Report (" + startDate + " - " + endDate + ")";
			String[] attachments = { "C:/reports/weekly-BDRR-report.xlsx" };

			System.out.println("📧 Weekly Timesheet MIS Mail sent to Manager: " + manager.firstName());
			System.out.println("📧 htmlBody: " + htmlBody);

			// Recipients (can be from DB/config)
			String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			// BCC recipients
			String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

			try {
				mailService.sendMailHTMLFile("idmadmin@VECV.IN", to, subject, htmlBody, attachments, bcc);
				System.out.println("✅ Weekly MIS Mail sent successfully!");
			} catch (Exception e) {
				System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
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

	/*
	 * private String buildWeeklyHtmlMail(LocalDate startDate, LocalDate endDate,
	 * List<WeeklyTimesheetDTO> submittedList, List<WeeklyTimesheetDTO>
	 * notSubmittedList) {
	 * 
	 * StringBuilder html = new StringBuilder();
	 * 
	 * html.append("<html><body style='font-family: Arial, sans-serif;'>");
	 * 
	 * html.append("<h3 style='color:#333;'>Weekly Timesheet Status Report (").
	 * append(startDate).append(" - ") .append(endDate).append(")</h3>");
	 * 
	 * html.append(
	 * "<p>Dear Sir/Madam,<br><br>Please find below the weekly timesheet entry status for your reportees:</p>"
	 * );
	 * 
	 * html.append(
	 * "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse: collapse; width:100%;'>"
	 * );
	 * 
	 * //Table Header
	 * html.append("<tr style='background-color:#f2f2f2; font-weight:bold;'>").
	 * append("<th>Employee Name</th>")
	 * .append("<th>Username</th>").append("<th>Department</th>").append(
	 * "<th>Manager</th>") .append("<th>Status</th>").append("</tr>");
	 * 
	 * //Submitted entries (GREEN) for (WeeklyTimesheetDTO dto : submittedList) {
	 * html.append("<tr>").append("<td>").append(dto.employeeName()).append("</td>")
	 * .append("<td>")
	 * .append(dto.userName()).append("</td>").append("<td>").append(dto.
	 * departmentName()).append("</td>")
	 * .append("<td>").append(dto.managerName()).append("</td>")
	 * .append("<td style='color:green; font-weight:bold;'>").append(dto.status()).
	 * append("</td>") .append("</tr>"); }
	 * 
	 * //Not Submitted entries (RED) for (WeeklyTimesheetDTO dto : notSubmittedList)
	 * {
	 * html.append("<tr>").append("<td>").append(dto.employeeName()).append("</td>")
	 * .append("<td>")
	 * .append(dto.userName()).append("</td>").append("<td>").append(dto.
	 * departmentName()).append("</td>")
	 * .append("<td>").append(dto.managerName()).append("</td>")
	 * .append("<td style='color:red; font-weight:bold;'>").append(dto.status()).
	 * append("</td>") .append("</tr>"); }
	 * 
	 * html.append("</table><br>");
	 * 
	 * html.append("<p>Regards,<br><b>Timesheet Automation System</b></p>");
	 * html.append("</body></html>");
	 * 
	 * return html.toString(); }
	 */

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
}
