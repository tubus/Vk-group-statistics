package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;

public class RepeatingButtonOnClick implements View.OnClickListener, Runnable {

    private final Activity activity;
    private final TextView repeating_text_view;

    VkRestService vkRestService = new VkRestService();

    public RepeatingButtonOnClick(TextView repeating_text_view, Activity activity) {
        this.repeating_text_view = repeating_text_view;
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
            VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
            request.setAction(VK_REST_SERVICE_ACTION.REPEATING_ACTION);
            name += vkRestService.execute(request).get().getRepeating();
        } catch (Exception ex) {
        }
        activity.runOnUiThread(new RunTextViewUpdateOnUI(name));
    }

    private class RunTextViewUpdateOnUI implements Runnable {

        private final String name;

        public RunTextViewUpdateOnUI(String name) {
           this.name = name;
        }

        @Override
        public void run() {
            repeating_text_view.setText(name);
            Linkify.addLinks(repeating_text_view, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
        }
    }
}