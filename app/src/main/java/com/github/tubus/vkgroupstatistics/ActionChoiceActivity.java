package com.github.tubus.vkgroupstatistics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ActionChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_choice);
        setAllButtons();
    }

    private void setAllButtons() {
        setCountButton();
        setDownloadAllButton();
        setRepeatingButton();
    }

    private void setCountButton() {
        final Button button = findViewById(R.id.count_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                VkRestService vkRestService = new VkRestService();
                TextView textView = findViewById(R.id.textView);
                String name = "";
                try {
                    VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                    request.setAction(VK_REST_SERVICE_ACTION.COUNT_ACTION);
                    name += vkRestService.execute(request).get().getCount();
                } catch (Exception ex) {
                }
                textView.setText(name);
            }
        });
    }

    private void setDownloadAllButton() {
        final Button button1 = findViewById(R.id.download_all_button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                VkRestService vkRestService = new VkRestService();

                List<byte[]> names = Collections.emptyList();
                try {
                    VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                    request.setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_SINGLE_ACTION);
                    names = vkRestService.execute(request).get().getImage();
                } catch (Exception ex) {
                }
                ImageView imageView = findViewById(R.id.imageView2);
                int count = 1;
                for (byte[] name : names) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(name, 0, name.length);

                    try (FileOutputStream out = new FileOutputStream(count + ".jpeg")) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                    imageView.refreshDrawableState();

                    count++;
                }
            }
        });
    }

    private void setRepeatingButton() {
        final Button button3 = findViewById(R.id.repeating_button_id);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                VkRestService vkRestService = new VkRestService();
                TextView textView = findViewById(R.id.textView3);
                String name = "";
                try {
                    VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                    request.setAction(VK_REST_SERVICE_ACTION.REPEATING_ACTION);
                    name += vkRestService.execute(request).get().getRepeating();
                } catch (Exception ex) {
                }
                textView.setText(name);
            }
        });
    }
}