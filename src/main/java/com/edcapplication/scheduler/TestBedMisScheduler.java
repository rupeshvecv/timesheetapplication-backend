package com.edcapplication.scheduler;

import com.edcapplication.dao.TestBedEntryDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Project;
import com.edcapplication.model.TestBed;
import com.edcapplication.model.TestBedEntry;
import com.edcapplication.repository.ProjectRepository;
import com.edcapplication.repository.TestBedRepository;
import com.edcapplication.service.TestBedEntryService;
import com.edcapplication.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // Runs every day at 08:00 AM (adjust cron as needed)
    //@Scheduled(cron = "0 0 8 * * ?")
    // ⏰ Runs every day at 12:05 PM
    @Scheduled(cron = "0 41 8 * * *")
    public void sendDailyMisMail() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(18);
        String formattedDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Get yesterday’s data
        //List<TestBedEntry> entries = testBedEntryService.getEntriesByDateRange(yesterday, yesterday);
        List<TestBedEntry> entries = testBedEntryService.getEntriesByDateRange(yesterday, yesterday);
        
        System.out.println("TestBedMisScheduler.sendDailyMisMail(entries) "+entries);

        // Build HTML Table
        String htmlBody = buildHtmlMIS(entries, formattedDate);

        // Mail Subject
        String subject = "Test Bed Uptime & Utilization MIS - " + formattedDate;

        // Recipients (can be from DB/config)
        String[] to = {"rkraghuvanshi@vecv.in", "askushwah2@VECV.IN"};

        // Send
      //BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };
        
        System.out.println("Before Sending mail ***************************** " + formattedDate);
		mailService.sendMail(bcc, subject, htmlBody, bcc);
        //mailService.sendHtmlMail("rkraghuvanshi@vecv.in",subject,htmlBody);

        System.out.println("MIS Mail sent for " + formattedDate);
    }

    private String buildHtmlMIS(List<TestBedEntry> entries, String formattedDate) {
        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<table><tr><td align='left'><font color='red'>Dear All,<br>");
        html.append("Please find the details of EDC Test Bed MIS for Date ")
            .append(formattedDate)
            .append(" :-</font></td></tr></table>");

        html.append("<table border='1' style='border-collapse: collapse; font-family: Calibri; font-size: 12px; width:100%; text-align:center;'>");

        // 🔹 Header section
        html.append("<tr bgcolor='#DCB6A5'>");
        html.append("<th colspan='12'>Basic Details</th>");
        html.append("<th colspan='3'>Progress Issue</th>");
        html.append("<th colspan='1'>Uptime Issue</th>");
        html.append("<th colspan='3'>Utilization Issue</th>");
        html.append("<th colspan='1'>Cummulative </th>");
        html.append("</tr>");

        html.append("<tr bgcolor='#B6B6B6'>");
        html.append("<th>Test Bed</th>");
        html.append("<th>Planned Hrs</th>");
        html.append("<th>Uptime Hrs</th>");
        html.append("<th>Uptime %</th>");
        html.append("<th>Utilization Hrs</th>");
        html.append("<th>Utilization %</th>");
        html.append("<th>Validation/Dev Run</th>");
        html.append("<th>Validation/Dev Run %</th>");
        html.append("<th>Shift</th>");
        html.append("<th>RaisedBy</th>");
        html.append("<th>RaisedOn</th>");
        html.append("<th>Project</th>");
        html.append("<th>Test Description</th>");
        html.append("<th>Set Up Remarks</th>");
        html.append("<th>Work On Engine</th>");
        html.append("<th>Breakdown</th>");
        html.append("<th>No Manpower</th>");
        html.append("<th>Power/Diesel</th>");
        html.append("<th>Any Other</th>");
        html.append("<th>Cummulative Description</th>");
        html.append("</tr>");

        if (entries.isEmpty()) {
            html.append("<tr><td colspan='18' align='center'><b>No Records Found ..</b></td></tr>");
        } else {
            //Step 1: Group by TestBed
            Map<Long, TestBedSummary> summaryMap = new LinkedHashMap<>();

            for (TestBedEntry entry : entries) {
                if (entry.getId().getTestbedId() == null)
                    throw new BadRequestException("Missing required fields: testbedId");

                TestBed testBed = testBedRepository.findById(entry.getId().getTestbedId())
                        .orElseThrow(() -> new ResourceNotFoundException("TestBed not found"));

                Long testBedId = entry.getId().getTestbedId();
                TestBedSummary summary = summaryMap.getOrDefault(testBedId, new TestBedSummary(testBed.getName()));
                summary.addEntry(entry);
                summaryMap.put(testBedId, summary);
            }

            //Step 2: Print grouped rows with rowspan
            for (TestBedSummary summary : summaryMap.values()) {
                int rowSpan = summary.entries.size();
                
                boolean firstRow = true;
                for (TestBedEntry entry : summary.entries) {
                	
                    html.append("<tr>");
                    //TestBed name and total values (merged once)
                    if (firstRow) {
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                            .append(summary.testBedName).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(summary.totalPlanned).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(summary.totalUptime).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(calcPercent(summary.totalUptime, summary.totalPlanned)).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(summary.totalUtilization).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(calcPercent(summary.totalUtilization, summary.totalPlanned)).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(summary.totalValidation).append("</b></td>");
                        html.append("<td rowspan='").append(rowSpan).append("'><b>")
                        .append(calcPercent(summary.totalValidation, summary.totalPlanned)).append("</b></td>");
                    }

                    html.append("<td>").append(entry.getId().getShift()).append("</td>");
                    html.append("<td>").append(nvl(entry.getRaisedBy())).append("</td>");
                    html.append("<td>").append(entry.getId().getRaisedOn()).append("</td>");
                    html.append("<td>").append(entry.getProject() != null ? entry.getProject().getProjectCode() : "").append("</td>");
                    html.append("<td>").append(nvl(entry.getTestDescription())).append("</td>");
                    html.append("<td>").append(nvl(entry.getSetUpRemarks())).append("</td>");
                    html.append("<td>").append(nvl(entry.getWorkonEngineRemarks())).append("</td>");
                    html.append("<td>").append(nvl(entry.getBreakDownRemarks())).append("</td>");
                    html.append("<td>").append(nvl(entry.getNoManPowerRemarks())).append("</td>");
                    html.append("<td>").append(nvl(entry.getPowerCutRemarks())).append("</td>");
                    html.append("<td>").append(nvl(entry.getAnyOtherRemarks())).append("</td>");
                    html.append("<td>").append(nvl(entry.getCoummulativeDescription())).append("</td>");
                    html.append("</tr>");

                    firstRow = false;
                }
            }
        }

        html.append("</table></body></html>");
        System.out.println("TestBedMisScheduler.buildHtmlMIS( OUTPUT ) :: "+html.toString());
        return html.toString();
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

}
