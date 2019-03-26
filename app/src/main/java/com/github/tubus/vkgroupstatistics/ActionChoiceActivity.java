package com.github.tubus.vkgroupstatistics;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.listener.CountButtonOnClick;
import com.github.tubus.vkgroupstatistics.listener.DownloadMultiplePhotoButtonOnClick;
import com.github.tubus.vkgroupstatistics.listener.DownloadSinglePhotoButtonOnClick;
import com.github.tubus.vkgroupstatistics.listener.RepeatingButtonOnClick;
import com.github.tubus.vkgroupstatistics.listener.SubcriptionStatisticsButtonOnClick;
import com.github.tubus.vkgroupstatistics.service.SubscriptionNotificationService;

public class ActionChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_choice);
        setAllButtons();
    }

    private void setAllButtons() {
        new Thread(new ServiceStarter(this)).run();

        final TextInputEditText textInputEditText = (TextInputEditText) findViewById(R.id.download_single_input_count);
        final TextInputEditText hours_input = (TextInputEditText) findViewById(R.id.hours_input);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.count_button).setOnClickListener(
                new CountButtonOnClick((TextView) findViewById(R.id.countTextView), this));
        findViewById(R.id.download_single_button).setOnClickListener(
                new DownloadSinglePhotoButtonOnClick(textInputEditText, imageView,this));
        findViewById(R.id.download_multiple_button).setOnClickListener(
                new DownloadMultiplePhotoButtonOnClick(imageView, bar, this));
        findViewById(R.id.repeating_button_id).setOnClickListener(
                new RepeatingButtonOnClick((TextView) findViewById(R.id.repeating_text_view), this));
        findViewById(R.id.subscription_statistics_button).setOnClickListener(
                new SubcriptionStatisticsButtonOnClick((TextView) findViewById(R.id.subscription_statistics_text_view), hours_input, this)
        );
    }

    private class ServiceStarter implements Runnable {

        private final Activity activity;

        public ServiceStarter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            startService(new Intent(activity, SubscriptionNotificationService.class));
        }
    }
}