package com.dh.dhbooking.dto;




public class RolDTO {

    private Integer id;
    private String name;
    private Boolean status;

    public RolDTO() {
    }

    public RolDTO(String name, Boolean status) {
        this.name = name;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
