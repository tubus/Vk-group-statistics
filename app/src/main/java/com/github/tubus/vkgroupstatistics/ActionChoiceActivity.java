package com.github.tubus.vkgroupstatistics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;
import java.io.File;
import java.io.FileOutputStream;
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
        setDownloadSinglePhotoButton();
        setRepeatingButton();
        setDownloadMultiplePhotoButton();
    }

    private void setCountButton() {
        final Button button = findViewById(R.id.count_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

    private void setDownloadSinglePhotoButton() {
        final Button button1 = findViewById(R.id.download_single_button);
        final TextInputEditText textInputEditText = findViewById(R.id.text_input);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int tempId = 1;
                String text = textInputEditText.getText().toString();
                try {
                    tempId = Integer.parseInt(text);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                final int id = tempId;

                VkRestService vkRestService = new VkRestService();

                List<byte[]> names = Collections.emptyList();
                try {
                    VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                    request.setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_SINGLE_ACTION);
                    request.setId(id);
                    names = vkRestService.execute(request).get().getImage();
                } catch (Exception ex) {
                }
                ImageView imageView = findViewById(R.id.imageView2);
                int count = 1;
                for (byte[] name : names) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(name, 0, name.length);

                    imageView.clearColorFilter();
                    imageView.refreshDrawableState();
                    writeFileToStorage("vkgroupstatistics_" + id + ".jpeg", bitmap);
                    imageView.setImageBitmap(bitmap);
                    imageView.refreshDrawableState();

                    count++;
                }
            }
        });
    }

    private void setDownloadMultiplePhotoButton() {
        final Button button1 = findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VkRestService vkRestService = new VkRestService();

                List<byte[]> names = Collections.emptyList();
                try {
                    VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                    request.setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_ALL_ACTION);
                    names = vkRestService.execute(request).get().getImage();
                } catch (Exception ex) {
                }
                ImageView imageView = findViewById(R.id.imageView2);
                int count = 1;
                for (byte[] name : names) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(name, 0, name.length);
                    imageView.clearColorFilter();
                    imageView.refreshDrawableState();
                    writeFileToStorage("vkgroupstatistics_" + count + ".jpeg", bitmap);
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
                Linkify.addLinks(textView, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
            }
        });
    }

    private void writeFileToStorage(String fname, Bitmap bitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/vkGroup";
        File myDir = new File(root);
        myDir.mkdirs();
        File file = new File(myDir, fname);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
    }
}