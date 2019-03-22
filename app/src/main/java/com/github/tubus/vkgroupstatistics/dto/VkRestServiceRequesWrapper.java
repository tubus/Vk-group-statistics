package com.github.tubus.vkgroupstatistics.dto;

public class VkRestServiceRequesWrapper {

    public VkRestServiceRequesWrapper() {
    }

    private VK_REST_SERVICE_ACTION action;

    private Integer id;

    private Integer hours = 24;

    private String changedStatus;

    public VK_REST_SERVICE_ACTION getAction() {
        return action;
    }

    public void setAction(VK_REST_SERVICE_ACTION action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getChangedStatus() {
        return changedStatus;
    }

    public void setChangedStatus(String changedStatus) {
        this.changedStatus = changedStatus;
    }
}