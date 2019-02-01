package com.github.tubus.vkgroupstatistics.rest.service;

import android.os.AsyncTask;
import com.github.tubus.vkgroupstatistics.dto.MessagesWrapper;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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
                response.setCount(downloadSingleAction());
                break;
            case DOWNLOAD_SINGLE_ACTION:
                response.setImage(downloadSinglePhoto());
                break;
        }
        return response;
    }

    private Integer downloadSingleAction() {
        // The connection URL
        String url = "http://185.237.98.189:5823/vk/count/photo/all";
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        // Make the HTTP GET request, marshaling the response to a String
        String result = restTemplate.getForObject(url, String.class);
        return Integer.parseInt(result);
    }

    private List<byte[]> downloadSinglePhoto() {
        // The connection URL
        String url = "http://185.237.98.189:5823/vk/download/photos/from/1/to/" + downloadSingleAction();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MessagesWrapper> forEntity = restTemplate.getForEntity(url, MessagesWrapper.class);
        MessagesWrapper messagesWrapper = forEntity.getBody();
        return messagesWrapper.getMessages();
    }
}
