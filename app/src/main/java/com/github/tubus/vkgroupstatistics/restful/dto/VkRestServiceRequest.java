package com.github.tubus.vkgroupstatistics.restful.dto;

public class VkRestServiceRequest {

    private VK_REST_SERVICE_ACTION action;

    private Integer id;

    private Integer hours = 24;

    private String changedStatus;

    private VkRestServiceRequest() {
    }

    public static VkRestServiceRequestBuilder builder() {
        return new VkRestServiceRequestBuilder();
    }

    public VK_REST_SERVICE_ACTION getAction() {
        return action;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHours() {
        return hours;
    }

    public String getChangedStatus() {
        return changedStatus;
    }

    public static class VkRestServiceRequestBuilder {

        VkRestServiceRequest vkRestServiceRequest;

        private VkRestServiceRequestBuilder() {
            vkRestServiceRequest = new VkRestServiceRequest();
        }

        public VkRestServiceRequestBuilder setAction(VK_REST_SERVICE_ACTION action) {
            vkRestServiceRequest.action = action;
            return this;
        }

        public VkRestServiceRequestBuilder setId(Integer id) {
            vkRestServiceRequest.id = id;
            return this;
        }

        public VkRestServiceRequestBuilder setHours(Integer hours) {
            vkRestServiceRequest.hours = hours;
            return this;
        }

        public VkRestServiceRequestBuilder setChangedStatus(String changedStatus) {
            vkRestServiceRequest.changedStatus = changedStatus;
            return this;
        }

        public VkRestServiceRequest build() {
            return vkRestServiceRequest;
        }
    }
}