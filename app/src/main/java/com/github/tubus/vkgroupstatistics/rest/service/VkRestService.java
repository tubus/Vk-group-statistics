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
import static com.github.tubus.vkgroupstatistics.consts.Consts.BASE_VK_REST_SERVICE_URL;

public class VkRestService extends AsyncTask<VkRestServiceRequesWrapper, Void, VkRestServiceResponseWrapper> {

    RestTemplate restTemplate = new RestTemplate();

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
            case SUBSCRIPTION_STATISTICS_ACTION:
                response.setSubscriptionStats(getSubscriptionStatistics(request[0].getHours()));
                break;
        }
        return response;
    }

    private Integer getCountAllPhotosInGroup() {
        String url = BASE_VK_REST_SERVICE_URL + "/vk/count/photo/all";
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
        String url = BASE_VK_REST_SERVICE_URL + "/vk/group/find/repeating";
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    private String getSubscriptionStatistics(int hours) {
        String url1 = BASE_VK_REST_SERVICE_URL + "/vk/subscription/subscribed/hours/" + hours;
        String url2 = BASE_VK_REST_SERVICE_URL + "/vk/subscription/unsubscribed/hours/" + hours;
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String resultPart1 = restTemplate.getForObject(url1, String.class);
        String resultPart2 = restTemplate.getForObject(url2, String.class);
        return "Subscribed: \n" + resultPart1 + "\n\nUnsubscribed: \n" + resultPart2;
    }

    private List<byte[]> downloadSinglePhotoInGroup(int index) {
        String url = BASE_VK_REST_SERVICE_URL + "/vk/download/photos/from/" + index +"/to/" + index;
        ResponseEntity<MessagesWrapper> forEntity = restTemplate.getForEntity(url, MessagesWrapper.class);
        MessagesWrapper messagesWrapper = forEntity.getBody();
        return messagesWrapper.getMessages();
    }
}
