package com.github.tubus.vkgroupstatistics.dto;

import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;

import java.util.List;

public class VkRestServiceResponseWrapper {

    public VkRestServiceResponseWrapper() {
    }

    private Integer count;

    private List<byte[]> image;

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
}
