package com.timesheetapplication.projection;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TimesheetEntryProjection {

    Long getId();
    LocalDate getEntryDate();
    Time getTime();
    BigDecimal getHours();
    String getUserName();

    Long getCategoryId();
    String getCategoryName();

    Long getPlatformId();
    String getPlatformName();

    Long getProjectId();
    String getProjectName();

    Long getActivityId();
    String getActivityName();

    String getDetails();
    LocalDateTime getRaisedOn();
}
