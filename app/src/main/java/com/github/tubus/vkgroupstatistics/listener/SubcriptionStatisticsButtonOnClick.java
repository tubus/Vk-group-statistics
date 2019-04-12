package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.restful.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.VkRestServiceRequest;
import com.github.tubus.vkgroupstatistics.restful.dto.VkRestServiceResponseWrapper;
import com.github.tubus.vkgroupstatistics.restful.service.VkRestService;
import java.util.concurrent.ExecutionException;
import static com.github.tubus.vkgroupstatistics.utils.ParseUtils.parseIntegerOrDefault;

public class SubcriptionStatisticsButtonOnClick implements View.OnClickListener, Runnable {

    private final Activity activity;
    private final TextView textView;
    private final TextInputEditText textInputEditText;

    public SubcriptionStatisticsButtonOnClick(TextView textView, TextInputEditText textInputEditText, Activity activity) {
        this.activity = activity;
        this.textView = textView;
        this.textInputEditText = textInputEditText;
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        String text = textInputEditText.getText().toString();
        Integer hours = parseIntegerOrDefault(text, 24);

        VkRestServiceRequest request = VkRestServiceRequest.builder()
        .setAction(VK_REST_SERVICE_ACTION.SUBSCRIPTION_STATISTICS_ACTION)
        .setHours(hours).build();
        String subscriptionStats = "";
        try {
            VkRestServiceResponseWrapper vkRestServiceResponseWrapper =  new VkRestService().execute(request).get();
            subscriptionStats += vkRestServiceResponseWrapper.getSubscriptionStats();
        } catch (InterruptedException | ExecutionException e) {
        }
        activity.runOnUiThread(new RunTextViewUpdateOnUI(subscriptionStats));
    }

    private class RunTextViewUpdateOnUI implements Runnable {

        private final String name;

        public RunTextViewUpdateOnUI(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            textView.setText(name);
            Linkify.addLinks(textView, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
        }
    }
}