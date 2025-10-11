package com.edcapplication.dao;

public class EquipmentDao {
    private Long id;
    private String equipmentName;

    public EquipmentDao() {}

    public EquipmentDao(Long id, String equipmentName) {
        this.id = id;
        this.equipmentName = equipmentName;
    }

    public Long getId() {
        return id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
