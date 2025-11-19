package com.edcapplication.projection;

import java.time.LocalDate;

public interface BDRREntryProjection {

	Long getId();
    String getBdrrNumber();
    String getStatus();
    LocalDate getRaisedOn();
    String getRaisedBy();
    String getShift();

    Long getTestbedId();
    Long getEquipmentId();
    Long getSubEquipmentId();
    Long getProblemId();

    //Names MUST MATCH JPQL ALIAS EXACTLY (camelCase)
    String getTestbedName();
    String getEquipmentName();
    String getSubEquipmentName();
    String getProblemName();

    String getTestAffected();
    String getAlternateArrangement();
    String getSuggestion();
    String getAttender();

    String getSolutionRootCause();
    String getSolutionActionTaken();
    String getSolutionBy();
    LocalDate getSolutionGivenOn();

    LocalDate getBdrrOfDate();
    String getAreaAttender();
    LocalDate getTargetDate();
    LocalDate getClosingDate();

    String getPartUsed();
    String getPartNumber();
    String getPartDescriptions();
    String getQuantity();

    String getBreakDownDescription();
    String getInitialAnalysis();
    String getWorkDoneDescription();
}
