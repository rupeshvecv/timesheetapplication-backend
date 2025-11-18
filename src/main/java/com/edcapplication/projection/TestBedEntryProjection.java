package com.edcapplication.projection;

import java.time.LocalDate;

public interface TestBedEntryProjection {

    //EmbeddedId fields
    Long getTestbedId();
    LocalDate getRaisedOn();
    String getShift();

    // Other fields
    String getTime();
    String getRaisedBy();
    String getTestBedUser();

    //Project fields (nested projection)
    Long getProjectId();
    String getProjectName();

    double getPlannedHours();
    double getUptimeHours();
    double getUtilizationHours();
    double getValidationHours();

    String getTestDescription();
    double getTestDescriptionHours();

    String getWorkonEngineRemarks();
    double getWorkonEngineHours();

    String getSetUpRemarks();
    double getSetUpHours();

    String getBreakDownRemarks();
    double getBreakDownHours();

    String getNoManPowerRemarks();
    double getNoManPowerHours();

    String getPowerCutRemarks();
    double getPowerCutHours();

    String getAnyOtherRemarks();
    double getAnyOtherHours();

    String getCoummulativeDescription();
    double getTotalSum();
}
