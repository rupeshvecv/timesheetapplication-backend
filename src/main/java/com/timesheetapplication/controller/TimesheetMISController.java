package com.timesheetapplication.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.NotFilledDTO;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.projection.TimesheetEntryProjection;
import com.timesheetapplication.service.TimesheetEntryService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/api/timesheetapplication/timesheetmis")
public class TimesheetMISController {

    @Autowired
    private TimesheetEntryService timesheetEntryService;

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @GetMapping("/not-filled-list")
    @ResponseBody
    public String getNotFilledList(
            @RequestParam String head,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam String headFullName) {

        List<UserSummaryDTO> subordinates = userServiceFeignClient.getSubordinateUsers(head);

        List<NotFilledDTO> notFilledList = new ArrayList<>();

        for (UserSummaryDTO emp : subordinates) {

            int working = 0;
            int filled = 0;

            LocalDate day = from;

            while (!day.isAfter(to)) {

                List<TimesheetEntryProjection> entries =
                        timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(
                                emp.userName(), day, day);

                working++;

                if (!entries.isEmpty()) {
                    filled++;
                }

                day = day.plusDays(1);
            }

            if (working != filled) {
                notFilledList.add(
                        new NotFilledDTO(
                                emp.firstName() + " " + emp.lastName(),
                                head,
                                working,
                                filled
                        )
                );
            }
        }

     // ------- Build HTML -------
        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<body style='font-family:Arial, sans-serif;'>");

        // Title
        html.append("<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>")
            .append("⚠️ Timesheet Not Filled Report")
            .append("</h2>");

        // summary lines
        html.append("<p><b>Head:</b> ").append(headFullName).append("</p>");
        html.append("<p><b>Duration:</b> ").append(from).append(" to ").append(to).append("</p>");

        html.append("<p>Please find the list of employees who have <b>not filled</b> timesheets during the selected period.</p><br>");

        // table
        html.append("<table border='1' cellspacing='0' cellpadding='8' ")
            .append("style='border-collapse:collapse;width:100%;'>");

        // header row
        html.append("<tr style='background-color:#2E86C1;color:white;text-align:center;font-weight:bold;'>")
            .append("<th>Employee Name</th>")
            .append("<th>Reporting Head</th>")
            .append("<th>Total Working Days</th>")
            .append("<th>Filled Days</th>")
            .append("<th>Not Filled Days</th>")
            .append("</tr>");

        // data rows
        for (NotFilledDTO nf : notFilledList) {
            int notFilled = nf.getWorkingDays() - nf.getFilledDays();

            html.append("<tr style='text-align:center;'>");

            html.append("<td>").append(nf.getEmployee()).append("</td>");
            html.append("<td>").append(nf.getManager()).append("</td>");
            html.append("<td>").append(nf.getWorkingDays()).append("</td>");

            // filled green
            html.append("<td style='color:green;font-weight:bold;'>")
                .append(nf.getFilledDays())
                .append("</td>");

            // not filled red
            html.append("<td style='color:red;font-weight:bold;'>")
                .append(notFilled)
                .append("</td>");

            html.append("</tr>");
        }

        // footer
        html.append("</table><br>");
        html.append("<p>Regards,<br><b>Timesheet Admin</b></p>");

        html.append("</body></html>");

        return html.toString();
    }
    
    @GetMapping("/functionHeadTimesheetDetails")
    @ResponseBody
    public List<NotFilledDTO> getAllFunctionHeadTimesheetDetails(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        LocalDate startDate = from;
        LocalDate endDate = to;

        // Final result list for ALL function heads
        List<NotFilledDTO> result = new ArrayList<>();

        // ===== FETCH ALL FUNCTION HEADS =====
        List<UserSummaryDTO> allFunctionHeads = userServiceFeignClient.getAllFunctionHead();
        // sort alphabetically by full name
        allFunctionHeads.sort(
                Comparator.comparing(h -> (h.firstName() + " " + h.lastName()).toLowerCase())
        );

        for (UserSummaryDTO manager : allFunctionHeads) {

            List<UserSummaryDTO> subordinates = userServiceFeignClient.getSubordinateUsers(manager.userName());
            if (subordinates == null || subordinates.isEmpty()) continue;

            for (UserSummaryDTO emp : subordinates) {

                int workingDays = 0;
                int filledDays = 0;

                LocalDate day = startDate;

                while (!day.isAfter(endDate)) {

                    List<TimesheetEntryProjection> entries =
                            timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(
                                    emp.userName(), day, day);

                    workingDays++;
                    if (!entries.isEmpty()) {
                        filledDays++;
                    }

                    day = day.plusDays(1);
                }

                // If not fully filled, add to result
                if (filledDays != workingDays) {
                    result.add(
                            new NotFilledDTO(
                                    emp.firstName() + " " + emp.lastName(),          // employee
                                    manager.firstName() + " " + manager.lastName(),   // function head
                                    workingDays,
                                    filledDays
                            )
                    );
                }
            }
        }
        //Return NOT FILLED details for ALL function heads
        return result;
    }

}

