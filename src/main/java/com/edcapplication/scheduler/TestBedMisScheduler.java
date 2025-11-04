package com.edcapplication.scheduler;

import com.edcapplication.dao.TestBedEntryDao;
import com.edcapplication.dto.UserDTO;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Project;
import com.edcapplication.model.TestBed;
import com.edcapplication.model.TestBedEntry;
import com.edcapplication.repository.ProjectRepository;
import com.edcapplication.repository.TestBedRepository;
import com.edcapplication.service.TestBedEntryService;
import com.edcapplication.service.TestBedUserService;
import com.edcapplication.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestBedMisScheduler {

    @Autowired
    private TestBedEntryService testBedEntryService;
    
    @Autowired
    private TestBedRepository testBedRepository;
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private TestBedUserService testBedService;

    // Runs every day at 08:00 AM (adjust cron as needed)
    //@Scheduled(cron = "0 0 8 * * ?")
    // ⏰ Runs every day at 12:05 PM
    @Scheduled(cron = "0 3 16 * * *")
    public void sendDailyTestBedUptimeUtilizationMISMail() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(18);
        String formattedDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //Get yesterday’s data
        //List<TestBedEntry> entries = testBedEntryService.getEntriesByDateRange(yesterday, yesterday);
        List<TestBedEntry> entries = testBedEntryService.getEntriesByDateRange(yesterday, yesterday);
        
        System.out.println("TestBedMisScheduler.sendDailyTestBedUptimeUtilizationMISMail(entries) "+entries);

        //Build HTML Table
        String htmlBody = buildTestBedUptimeUtilizationHtmlMIS(entries, formattedDate);

        //Mail Subject
        String subject = "Test Bed Uptime & Utilization MIS- " + formattedDate;

        //Recipients (can be from DB/config)
        String[] to = {"rkraghuvanshi@vecv.in", "askushwah2@VECV.IN"};
        
        //Fetch Attender Users
   	    List<UserDTO> testBedAttenderUsers 		= testBedService.fetchAllAttenderUsers();
   	    System.out.println("TestBedMisScheduler.sendWeeklyTestBedMis( testBedAttenderUsers) "+testBedAttenderUsers);
   	    String[] toAttenderRecipients			= extractEmailsFromUsers(testBedAttenderUsers);
   	    System.out.println("TestBedMisScheduler.sendDailyTestBedUptimeUtilizationMISMail( toAttenderRecipients.SIZE) "+toAttenderRecipients.length);
   	    
	   	if (toAttenderRecipients.length == 0) {
	   	    System.out.println("⚠️ No Attender recipients to send mail!");
	   	    return;
	   	}
   	 
   	 	//Fetch TestBed Users
   	    List<UserDTO> testBedUsers 				= testBedService.fetchAllTestBedUsers();
   	    System.out.println("TestBedMisScheduler.sendDailyTestBedUptimeUtilizationMISMail( testBedUsers) "+testBedUsers);
   	    String[] toTestBedRecipients 			= extractEmailsFromUsers(testBedUsers);
   	    System.out.println("TestBedMisScheduler.sendDailyTestBedUptimeUtilizationMISMail( toTestBedRecipients.SIZE) "+toTestBedRecipients.length);
   	 
	   	if (toTestBedRecipients.length == 0) {
	   	    System.out.println("⚠️ No TestBed recipients to send mail!");
	   	    return;
	   	}

        //Send
	   	//BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };
        
        String[] attachments = {"C:/reports/daily-report.xlsx"};
        
        System.out.println("Before Sending mail tttttttttttttttttttttttttttttttttt " + formattedDate);
		//mailService.sendMail(bcc, subject, htmlBody, bcc);
        //mailService.sendHtmlMail("rkraghuvanshi@vecv.in",subject,htmlBody);
		mailService.sendMailHTMLFile("idmadmin@VECV.IN",to,subject,htmlBody,attachments,bcc);

        System.out.println("MIS Mail sent for " + formattedDate);
    }

    private String buildTestBedUptimeUtilizationHtmlMIS(List<TestBedEntry> entries, String formattedDate) {
        StringBuilder html = new StringBuilder();

        // Header wrapper
        html.append("<html>");
        html.append("<body style='font-family: Calibri, Arial, sans-serif; background-color:#f9f9f9; color:#333; padding:20px;'>");
        html.append("<div style='max-width:1200px; margin:auto; background:#fff; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:20px;'>");
        html.append("<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>EDC Test Bed MIS Report</h2>");
        html.append("<p style='font-size:15px;'><b style='color:#C0392B;'>Dear All,</b><br>");
        html.append("Please find below the details of <b>EDC Test Bed MIS</b> for date: ");
        html.append("<span style='color:#2874A6;'>").append(formattedDate).append("</span></p>");

        // Table start / header
        html.append("<table style='border-collapse: collapse; width:100%; font-size:13px; text-align:center; border:1px solid #ddd;'>");
        html.append("<thead>");
        html.append("<tr style='background-color:#34495E; color:white;'>");
        html.append("<th colspan='12' style='padding:6px;'>Basic Details</th>");
        html.append("<th colspan='3' style='padding:6px;'>Progress Issue</th>");
        html.append("<th colspan='1' style='padding:6px;'>Uptime Issue</th>");
        html.append("<th colspan='3' style='padding:6px;'>Utilization Issue</th>");
        html.append("<th colspan='1' style='padding:6px;'>Cumulative</th>");
        html.append("</tr>");

        html.append("<tr style='background-color:#BFC9CA; font-weight:bold;'>");
        html.append("<th>Test Bed</th>");
        html.append("<th>Planned Hrs</th>");
        html.append("<th>Uptime Hrs</th>");
        html.append("<th>Uptime %</th>");
        html.append("<th>Utilization Hrs</th>");
        html.append("<th>Utilization %</th>");
        html.append("<th>Validation/Dev Run</th>");
        html.append("<th>Validation/Dev Run %</th>");
        html.append("<th>Shift</th>");
        html.append("<th>Raised By</th>");
        html.append("<th>Raised On</th>");
        html.append("<th>Project</th>");
        html.append("<th>Test Description</th>");
        html.append("<th>Set Up Remarks</th>");
        html.append("<th>Work On Engine</th>");
        html.append("<th>Breakdown</th>");
        html.append("<th>No Manpower</th>");
        html.append("<th>Power/Diesel</th>");
        html.append("<th>Any Other</th>");
        html.append("<th>Cumulative Description</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        // No records case
        if (entries == null || entries.isEmpty()) {
            html.append("<tr><td colspan='20' style='padding:10px; color:#999; text-align:center;'><b>No Records Found ..</b></td></tr>");
        } else {
            // Group by TestBed (keep your summary logic)
            Map<Long, TestBedSummary> summaryMap = new LinkedHashMap<>();

            for (TestBedEntry entry : entries) {
                if (entry.getId() == null || entry.getId().getTestbedId() == null) {
                    throw new BadRequestException("Missing required fields: testbedId");
                }
                TestBed testBed = testBedRepository.findById(entry.getId().getTestbedId())
                        .orElseThrow(() -> new ResourceNotFoundException("TestBed not found"));

                Long testBedId = entry.getId().getTestbedId();
                TestBedSummary summary = summaryMap.getOrDefault(testBedId, new TestBedSummary(testBed.getName()));
                summary.addEntry(entry);
                summaryMap.put(testBedId, summary);
            }

            // Print grouped rows with rowspan
            for (TestBedSummary summary : summaryMap.values()) {
                int rowSpan = (summary.entries == null) ? 1 : summary.entries.size();
                boolean firstRow = true;

                for (TestBedEntry entry : summary.entries) {
                    // alternate row backgrounds subtly
                    html.append("<tr style='background-color:").append(firstRow ? "#FDFEFE" : "#FAFAFA").append("; border-bottom:1px solid #ddd;'>");

                    if (firstRow) {
                        html.append("<td rowspan='").append(rowSpan).append("' style='font-weight:bold; padding:6px;'>")
                            .append(escapeHtml(summary.testBedName)).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(summary.totalPlanned).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(summary.totalUptime).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(calcPercent(summary.totalUptime, summary.totalPlanned)).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(summary.totalUtilization).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(calcPercent(summary.totalUtilization, summary.totalPlanned)).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(summary.totalValidation).append("</td>");
                        html.append("<td rowspan='").append(rowSpan).append("' style='padding:6px;'>")
                            .append(calcPercent(summary.totalValidation, summary.totalPlanned)).append("</td>");
                    }

                    // row-specific columns
                    html.append("<td style='padding:6px;'>").append(escapeHtml(String.valueOf(entry.getId().getShift()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getRaisedBy()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(String.valueOf(entry.getId().getRaisedOn()))).append("</td>");
                    html.append("<td style='padding:6px;'>")
                        .append(escapeHtml(entry.getProject() != null ? entry.getProject().getProjectCode() : "")).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getTestDescription()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getSetUpRemarks()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getWorkonEngineRemarks()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getBreakDownRemarks()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getNoManPowerRemarks()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getPowerCutRemarks()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getAnyOtherRemarks()))).append("</td>");
                    html.append("<td style='padding:6px;'>").append(escapeHtml(nvl(entry.getCoummulativeDescription()))).append("</td>");

                    html.append("</tr>");
                    firstRow = false;
                }
            }
        }

        // Close table and footer
        html.append("</tbody>");
        html.append("</table>");
        html.append("<p style='margin-top:20px; font-size:14px;'>Regards,<br><b>EDC Admin</b></p>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        System.out.println("✅ TestBedMisScheduler.buildHtmlMIS(OUTPUT): " + html.toString());
        return html.toString();
    }

    // Small helper to escape basic HTML special chars to avoid broken HTML when data contains < & >
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    private static class TestBedSummary {
        String testBedName;
        double totalPlanned = 0;
        double totalUptime = 0;
        double totalUtilization = 0;
        double totalValidation = 0;
        List<TestBedEntry> entries = new ArrayList<>();

        public TestBedSummary(String testBedName) {
            this.testBedName = testBedName;
        }

        public void addEntry(TestBedEntry entry) {
            entries.add(entry);
            totalPlanned += entry.getPlannedHours();
            totalUptime += entry.getUptimeHours();
            totalUtilization += entry.getUtilizationHours();
            totalValidation += entry.getValidationHours();
        }
    }

    private String calcPercent(double value, double total) {
        if (total == 0) return "0.00";
        return String.format("%.2f", (value / total) * 100);
    }

    private String format(double value) {
        return String.format("%.2f", value);
    }

    private String nvl(String str) {
        return (str == null || str.isEmpty()) ? "" : str;
    }
    
    //Runs every Monday at 8:00 AM
    //@Scheduled(cron = "0 0 8 * * MON")
    @Scheduled(cron = "0 2 16 * * *")
    public void sendWeeklyTestBedMis() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate endDate = startDate.plusDays(6); // Sunday

        System.out.println("📅 Generating Weekly MIS from " + startDate + " to " + endDate);

        //Fetch entries from last week
        List<TestBedEntry> entries = testBedEntryService.getEntriesByDateRange(startDate, endDate);

        if (entries.isEmpty()) {
            System.out.println("⚠️ No Test Bed entries found for last week!");
            return;
        }

        //Group by TestBed and Shift
        Map<String, Map<String, List<TestBedEntry>>> grouped = entries.stream()
                .collect(Collectors.groupingBy(
                        e -> String.valueOf(e.getId().getTestbedId()),
                        Collectors.groupingBy(e -> String.valueOf(e.getId().getShift()))
                ));
        
        //Build status map (TestBed → Shift → Filled/Not Filled)
        Map<String, Map<String, String>> statusMap = new LinkedHashMap<>();

        for (Map.Entry<String, Map<String, List<TestBedEntry>>> testBedEntry : grouped.entrySet()) {
            String testBedId = testBedEntry.getKey();
            Map<String, String> shiftStatus = new LinkedHashMap<>();

            for (String shift : Arrays.asList("A", "B", "C")) {
                List<TestBedEntry> shiftEntries = testBedEntry.getValue().getOrDefault(shift, Collections.emptyList());
                shiftStatus.put(shift, shiftEntries.isEmpty() ? "NOT FILLED" : "FILLED");
            }

            statusMap.put(testBedId, shiftStatus);
        }

        //Build HTML email body safely (Java 8 compatible)
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body style='font-family:Arial, sans-serif;'>");
        html.append("<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>Weekly Test Bed Entry MIS</h2>");
        html.append("<p><b>Duration:</b> ").append(startDate).append(" to ").append(endDate).append("</p>");
        html.append("<table border='1' cellspacing='0' cellpadding='6' style='border-collapse:collapse;width:80%;'>");
        html.append("<tr style='background-color:#f2f2f2;text-align:center;'>");
        html.append("<th>Test Bed</th>");
        html.append("<th>Shift A</th>");
        html.append("<th>Shift B</th>");
        html.append("<th>Shift C</th>");
        html.append("</tr>");

        for (Map.Entry<String, Map<String, String>> entry : statusMap.entrySet()) {
            String testBedId = entry.getKey();
            Map<String, String> shiftMap = entry.getValue();

            html.append("<tr style='text-align:center;'>");
            html.append("<td>TestBed ").append(testBedId).append("</td>");

            for (String shift : Arrays.asList("A", "B", "C")) {
                String status = shiftMap.getOrDefault(shift, "NOT FILLED");
                String color = status.equals("FILLED") ? "#28a745" : "#dc3545";
                html.append("<td style='color:#ffffff;background-color:").append(color).append(";'>")
                        .append(status)
                        .append("</td>");
            }

            html.append("</tr>");
        }

        html.append("</table>");
        html.append("<br/>");
        html.append("<p>Regards,<br/><b>EDC Admin</b></p>");
        html.append("</body>");
        html.append("</html>");

        //Send mail with attachment
   	 	String subject = "Weekly Test Bed MIS (" + startDate + " - " + endDate + ")";
   	 	String[] attachments = {"C:/reports/weekly-testbed-report.xlsx"};
       
   	 	//Fetch Attender Users
   	    List<UserDTO> testBedAttenderUsers 		= testBedService.fetchAllAttenderUsers();
   	    System.out.println("TestBedMisScheduler.sendWeeklyTestBedMis( testBedAttenderUsers) "+testBedAttenderUsers);
   	    String[] toAttenderRecipients			= extractEmailsFromUsers(testBedAttenderUsers);
   	    System.out.println("TestBedMisScheduler.sendWeeklyTestBedMis( toAttenderRecipients.SIZE) "+toAttenderRecipients.length);
   	    
	   	if (toAttenderRecipients.length == 0) {
	   	    System.out.println("⚠️ No Attender recipients to send mail!");
	   	    return;
	   	}
   	 
   	 	//Fetch TestBed Users
   	    List<UserDTO> testBedUsers 				= testBedService.fetchAllTestBedUsers();
   	    System.out.println("TestBedMisScheduler.sendWeeklyTestBedMis( testBedUsers) "+testBedUsers);
   	    String[] toTestBedRecipients 			= extractEmailsFromUsers(testBedUsers);
   	    System.out.println("TestBedMisScheduler.sendWeeklyTestBedMis( toTestBedRecipients.SIZE) "+toTestBedRecipients.length);
   	 
	   	if (toTestBedRecipients.length == 0) {
	   	    System.out.println("⚠️ No TestBed recipients to send mail!");
	   	    return;
	   	}
   	 
   	 	//Recipients (can be from DB/config)
   	 	String[] to = {"rkraghuvanshi@vecv.in", "askushwah2@VECV.IN"};

   	 	//BCC recipients
   	 	String[] bcc = new String[] {
           "rkraghuvanshi@vecv.in",
           "askushwah2@VECV.IN"
       };

        try {
    		mailService.sendMailHTMLFile("idmadmin@VECV.IN",to,subject,html.toString(),attachments,bcc);
            System.out.println("✅ Weekly MIS Mail sent successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error sending Weekly MIS Mail: " + e.getMessage());
        }
    }
    
    private String[] extractEmailsFromUsers(List<UserDTO> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("⚠️ No users provided to extract emails!");
            return new String[0];
        }

        List<String> emailList = users.stream()
                .map(UserDTO::getEmail)
                .filter(email -> email != null && !email.isEmpty())
                .toList();

        if (emailList.isEmpty()) {
            System.out.println("⚠️ No valid email IDs found in user list!");
            return new String[0];
        }

        String[] emailArray = emailList.toArray(new String[0]);
        System.out.println("✅ Extracted email array: " + Arrays.toString(emailArray));
        return emailArray;
    }
}
