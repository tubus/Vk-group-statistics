package com.github.tubus.vkgroupstatistics.dto;

import java.util.List;

public class VkRestServiceResponseWrapper {

    public VkRestServiceResponseWrapper() {
    }

    private Integer count;

    private List<byte[]> image;

    private String repeating;

    private String subscriptionStats;

    private List<String> usersList;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<byte[]> getImage() {
        return image;
    }

    public void setImage(List<byte[]> image) {
        this.image = image;
    }

    public String getRepeating() {
        return repeating;
    }

    public void setRepeating(String repeating) {
        this.repeating = repeating;
    }

    public String getSubscriptionStats() {
        return subscriptionStats;
    }

    public void setSubscriptionStats(String subscriptionStats) {
        this.subscriptionStats = subscriptionStats;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }
}
