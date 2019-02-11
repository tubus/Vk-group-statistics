package com.github.tubus.vkgroupstatistics.rest.service;

import android.os.AsyncTask;
import com.github.tubus.vkgroupstatistics.dto.MessagesWrapper;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class VkRestService extends AsyncTask<VkRestServiceRequesWrapper, Void, VkRestServiceResponseWrapper> {

    public VkRestService() {
    }

    @Override
    protected VkRestServiceResponseWrapper doInBackground(VkRestServiceRequesWrapper... request) {
        if (request.length != 1) {
            return null;
        }
        VK_REST_SERVICE_ACTION action = request[0].getAction();
        VkRestServiceResponseWrapper response = new VkRestServiceResponseWrapper();

        switch (action) {
            case COUNT_ACTION:
                response.setCount(getCountAllPhotosInGroup());
                break;
            case DOWNLOAD_SINGLE_ACTION:
                response.setImage(downloadSinglePhotoInGroup(request[0].getId()));
                break;
            case REPEATING_ACTION:
                response.setRepeating(getAllRepeatingPostsInGroup());
                break;
            case DOWNLOAD_ALL_ACTION:
                response.setImage(downloadAllPhotosInGroup());
                break;
        }
        return response;
    }

    private Integer getCountAllPhotosInGroup() {
        String url = "http://185.237.98.189:5823/vk/count/photo/all";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class);
        return Integer.parseInt(result);
    }

    private List<byte[]> downloadAllPhotosInGroup() {
        int count = getCountAllPhotosInGroup();
        List<byte[]> result = new ArrayList<>();

        for (int index = 1; index <= count; index++) {
            result.addAll(downloadSinglePhotoInGroup(index));
        }

        return result;
    }

    private String getAllRepeatingPostsInGroup() {
        String url = "http://185.237.98.189:5823/vk/group/find/repeating";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    private List<byte[]> downloadSinglePhotoInGroup(int index) {
        String url = "http://185.237.98.189:5823/vk/download/photos/from/" + index +"/to/" + index;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MessagesWrapper> forEntity = restTemplate.getForEntity(url, MessagesWrapper.class);
        MessagesWrapper messagesWrapper = forEntity.getBody();
        return messagesWrapper.getMessages();
    }
}
