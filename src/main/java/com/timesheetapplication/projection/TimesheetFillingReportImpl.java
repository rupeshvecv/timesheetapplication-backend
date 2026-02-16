package com.timesheetapplication.projection;

public class TimesheetFillingReportImpl
        implements TimesheetFillingReportProjection {

    private final String userName;
    private final Long filledDays;
    private final Long totalDays;
    private final Double percentageFilled;

    public TimesheetFillingReportImpl(
            String userName,
            Long filledDays,
            Long totalDays,
            Double percentageFilled) {
        this.userName = userName;
        this.filledDays = filledDays;
        this.totalDays = totalDays;
        this.percentageFilled = percentageFilled;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public Long getFilledDays() {
        return filledDays;
    }

    @Override
    public Long getTotalDays() {
        return totalDays;
    }

    @Override
    public Double getPercentageFilled() {
        return percentageFilled;
    }
}

