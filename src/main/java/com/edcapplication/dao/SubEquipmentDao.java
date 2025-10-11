package com.edcapplication.dao;

public class SubEquipmentDao {
    private Long id;
    private String subequipmentName;
    private Long equipmentId;

    public SubEquipmentDao() {}

    public SubEquipmentDao(Long id, String subequipmentName, Long equipmentId) {
        this.id = id;
        this.subequipmentName = subequipmentName;
        this.equipmentId = equipmentId;
    }

    public Long getId() {
        return id;
    }

    public String getSubequipmentName() {
        return subequipmentName;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubequipmentName(String subequipmentName) {
        this.subequipmentName = subequipmentName;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
}
