package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequest;
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
        String name = "";
        try {
            VkRestServiceRequest request = VkRestServiceRequest.builder()
            .setAction(VK_REST_SERVICE_ACTION.COUNT_ACTION).build();

            name += new VkRestService().execute(request).get().getCount();
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
            textView.setVisibility(View.VISIBLE);
            textView.setText(countData);
        }
    }
}