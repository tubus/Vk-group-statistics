package com.github.tubus.vkgroupstatistics.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import com.github.tubus.vkgroupstatistics.R;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequest;
import com.github.tubus.vkgroupstatistics.listener.FloatingNavigationOnClick;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;
import java.util.ArrayList;
import java.util.List;

import static com.github.tubus.vkgroupstatistics.consts.Consts.DEFAULT_PHOTO_ID;

public class PhotosStatistcsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_statistcs);

        getSupportActionBar().hide();

        ImageView image = (ImageView) findViewById(R.id.imageView4);
        updateImageView(image, getBitmapForImage(DEFAULT_PHOTO_ID));

        EditText imageIdTextView = (EditText) findViewById(R.id.editText);
        imageIdTextView.setText("" + DEFAULT_PHOTO_ID);

        FloatingActionButton buttonRight = findViewById(R.id.floatingActionButtonRight);
        buttonRight.setOnClickListener(new FloatingNavigationOnClick(true, imageIdTextView, image, this));

        FloatingActionButton buttonLeft = findViewById(R.id.floatingActionButtonLeft);
        buttonLeft.setOnClickListener(new FloatingNavigationOnClick(false, imageIdTextView, image, this));

        //imageIdTextView.setOnEditorActionListener(null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void updateImageView(ImageView imageView, Bitmap bitmap) {
        imageView.clearColorFilter();
        imageView.refreshDrawableState();
        imageView.setImageBitmap(bitmap);
        imageView.refreshDrawableState();
    }

    private Bitmap getBitmapForImage(int photoId) {
        List<byte[]> images = new ArrayList<>();
        try {
            VkRestServiceRequest request = VkRestServiceRequest.builder()
                    .setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_SINGLE_ACTION)
                    .setId(photoId).build();
            images.addAll(new VkRestService().execute(request).get().getImage());
            if (!images.isEmpty()) {
                byte[] image = images.get(0);
                return BitmapFactory.decodeByteArray(image, 0, image.length);
            }
        } catch (Exception ex) {
        }
        return null;
    }
}