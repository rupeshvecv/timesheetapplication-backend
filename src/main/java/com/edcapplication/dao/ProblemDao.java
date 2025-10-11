package com.edcapplication.dao;

public class ProblemDao {
    private Long id;
    private String problemName;
    private Long equipmentId;

    public ProblemDao() {}

    public ProblemDao(Long id, String problemName, Long equipmentId) {
        this.id = id;
        this.problemName = problemName;
        this.equipmentId = equipmentId;
    }

    public Long getId() {
        return id;
    }

    public String getProblemName() {
        return problemName;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
}
