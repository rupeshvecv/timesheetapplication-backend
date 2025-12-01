package com.timesheetapplication.projection;

public interface TimesheetFillingReportProjection {

    String getUserName();
    Long getFilledDays();
    Long getTotalDays();
    Double getPercentageFilled();
}
