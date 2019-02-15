package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;

public class CountButtonOnClick implements View.OnClickListener, Runnable {

    private final Activity activity;

    private final TextView textView;

    public CountButtonOnClick(TextView textView, Activity activity) {
        this.textView = textView;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        VkRestService vkRestService = new VkRestService();
        String name = "";
        try {
            VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
            request.setAction(VK_REST_SERVICE_ACTION.COUNT_ACTION);
            name += vkRestService.execute(request).get().getCount();
        } catch (Exception ex) {
        }
        activity.runOnUiThread(new RunImageViewUpdateOnUI(name));
    }
    private class RunImageViewUpdateOnUI implements Runnable {

        private String countData;

        private RunImageViewUpdateOnUI(String countData) {
            this.countData = countData;
        }

        @Override
        public void run() {
            textView.setText(countData);
        }
    }

}
