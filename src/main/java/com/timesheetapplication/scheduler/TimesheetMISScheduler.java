package com.timesheetapplication.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	@Scheduled(cron = "0 21 17 * * *")
	// @Scheduled(cron = "0 30 09 * * MON") // every Monday 9:30 AM
	public void sendWeeklyTimesheetMis() {
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

		//Fetch all users
		List<UserSummaryDTO> allPDDUsers = userServiceFeignClient.getAllOptimizedPDDUsers();
		System.out.println("sendWeeklyTimesheetMis(allPDDUsers.SIZE) " + allPDDUsers.size());

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

			List<WeeklyTimesheetDTO> submittedList = new ArrayList<>();
			List<WeeklyTimesheetDTO> notSubmittedList = new ArrayList<>();

			// Fetch Timesheet entries for each subordinate
			for (UserSummaryDTO subordinate : subordinates) {

				List<TimesheetEntryProjection> timeSheetEntries = timesheetEntryService
						.findForUserBetween(subordinate.userName(), startDate, endDate);

				System.out.println("Timesheet entries for " + subordinate.userName() + ": " + timeSheetEntries.size());

				if (timeSheetEntries.isEmpty()) {
					// Not Filled
					notSubmittedList.add(new WeeklyTimesheetDTO(subordinate.firstName() + " " + subordinate.lastName(),
							subordinate.userName(), subordinate.departmentName(),
							manager.firstName() + " " + manager.lastName(), "Not Filled"));
				} else {
					// Filled
					submittedList.add(new WeeklyTimesheetDTO(subordinate.firstName() + " " + subordinate.lastName(),
							subordinate.userName(), subordinate.departmentName(),
							manager.firstName() + " " + manager.lastName(), "Filled"));
				}
			}

			// If empty lists → skip email
			if (submittedList.isEmpty() && notSubmittedList.isEmpty()) {
				System.out.println("⚠️ No Weekly Timesheet data found for Manager " + manager.userName());
				continue;
			}

			String htmlBody = buildWeeklyHtmlMail(startDate, endDate, submittedList, notSubmittedList);
			String subject = "Weekly Timesheet Report (" + startDate + " - " + endDate + ")";

			System.out.println("📧 Weekly Timesheet MIS Mail sent to Manager: " + manager.firstName());
			System.out.println("📧 htmlBody: " + htmlBody);
		}

		// Send mail with attachment
		String subject = "Weekly BDRR MIS (" + startDate + " - " + endDate + ")";
		String[] attachments = { "C:/reports/weekly-timesheet-report.xlsx" };

		// Recipients (can be from DB/config)
		String[] to = { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

		// BCC recipients
		String[] bcc = new String[] { "rkraghuvanshi@vecv.in", "askushwah2@VECV.IN" };

		try {
			// mailService.sendMailHTMLFile("idmadmin@VECV.IN",to,subject,htmlBody,attachments,bcc);
			System.out.println("✅ Weekly Timesheet MIS Mail sent successfully!");
		} catch (Exception e) {
			System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
		}
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

	private String buildWeeklyHtmlMail(LocalDate startDate, LocalDate endDate, List<WeeklyTimesheetDTO> submittedList,
			List<WeeklyTimesheetDTO> notSubmittedList) {

		StringBuilder html = new StringBuilder();

		html.append("<html><body style='font-family: Arial, sans-serif;'>");

		html.append("<h3 style='color:#333;'>Weekly Timesheet Status Report (").append(startDate).append(" - ")
				.append(endDate).append(")</h3>");

		html.append(
				"<p>Dear Sir/Madam,<br><br>Please find below the weekly timesheet entry status for your reportees:</p>");

		html.append(
				"<table border='1' cellpadding='6' cellspacing='0' style='border-collapse: collapse; width:100%;'>");

		//Table Header
		html.append("<tr style='background-color:#f2f2f2; font-weight:bold;'>").append("<th>Employee Name</th>")
				.append("<th>Username</th>").append("<th>Department</th>").append("<th>Manager</th>")
				.append("<th>Status</th>").append("</tr>");

		//Submitted entries (GREEN)
		for (WeeklyTimesheetDTO dto : submittedList) {
			html.append("<tr>").append("<td>").append(dto.employeeName()).append("</td>").append("<td>")
					.append(dto.userName()).append("</td>").append("<td>").append(dto.departmentName()).append("</td>")
					.append("<td>").append(dto.managerName()).append("</td>")
					.append("<td style='color:green; font-weight:bold;'>").append(dto.status()).append("</td>")
					.append("</tr>");
		}

		//Not Submitted entries (RED)
		for (WeeklyTimesheetDTO dto : notSubmittedList) {
			html.append("<tr>").append("<td>").append(dto.employeeName()).append("</td>").append("<td>")
					.append(dto.userName()).append("</td>").append("<td>").append(dto.departmentName()).append("</td>")
					.append("<td>").append(dto.managerName()).append("</td>")
					.append("<td style='color:red; font-weight:bold;'>").append(dto.status()).append("</td>")
					.append("</tr>");
		}

		html.append("</table><br>");

		html.append("<p>Regards,<br><b>Timesheet Automation System</b></p>");
		html.append("</body></html>");

		return html.toString();
	}

}
