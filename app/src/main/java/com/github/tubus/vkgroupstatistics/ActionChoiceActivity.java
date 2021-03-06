package com.github.tubus.vkgroupstatistics;

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

public class ActionChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_choice);
        setAllButtons();
    }

    private void setAllButtons() {
        final TextInputEditText textInputEditText = findViewById(R.id.download_single_input_count);
        final ImageView imageView = findViewById(R.id.imageView);
        final ProgressBar bar = findViewById(R.id.progressBar);

        findViewById(R.id.count_button).setOnClickListener(
                new CountButtonOnClick((TextView) findViewById(R.id.countTextView), this));
        findViewById(R.id.download_single_button).setOnClickListener(
                new DownloadSinglePhotoButtonOnClick(textInputEditText, imageView,this));
        findViewById(R.id.download_multiple_button).setOnClickListener(
                new DownloadMultiplePhotoButtonOnClick(imageView, bar, this));
        findViewById(R.id.repeating_button_id).setOnClickListener(
                new RepeatingButtonOnClick((TextView) findViewById(R.id.repeating_text_view), this));
    }
}