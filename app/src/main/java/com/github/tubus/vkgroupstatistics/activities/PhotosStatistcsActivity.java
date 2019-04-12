package com.github.tubus.vkgroupstatistics.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import com.github.tubus.vkgroupstatistics.R;
import com.github.tubus.vkgroupstatistics.listener.OnEditorActionListenerImple;
import com.github.tubus.vkgroupstatistics.restful.dto.NETWORK_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.NetworkServiceRequest;
import com.github.tubus.vkgroupstatistics.restful.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.VkRestServiceRequest;
import com.github.tubus.vkgroupstatistics.listener.FloatingNavigationOnClick;
import com.github.tubus.vkgroupstatistics.restful.service.NetworkService;
import com.github.tubus.vkgroupstatistics.restful.service.VkRestService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import static com.github.tubus.vkgroupstatistics.consts.Consts.DEFAULT_PHOTO_ID;

public class PhotosStatistcsActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    public static final List<String> urls = new ArrayList<>();

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_statistcs);

        getSupportActionBar().hide();

        image = (ImageView) findViewById(R.id.imageView4);


        EditText imageIdTextView = (EditText) findViewById(R.id.aps_image_id);
        imageIdTextView.setText("" + DEFAULT_PHOTO_ID);

        FloatingActionButton buttonRight = findViewById(R.id.floatingActionButtonRight);
        buttonRight.setOnClickListener(new FloatingNavigationOnClick(true, imageIdTextView, image, this));

        FloatingActionButton buttonLeft = findViewById(R.id.floatingActionButtonLeft);
        buttonLeft.setOnClickListener(new FloatingNavigationOnClick(false, imageIdTextView, image, this));

        imageIdTextView.setOnEditorActionListener(new OnEditorActionListenerImple(image, this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText textStatistics = (EditText) findViewById(R.id.aps_statistics_text);
        Integer count = 0;
        try {
            count = new VkRestService().execute(VkRestServiceRequest.builder().setAction(VK_REST_SERVICE_ACTION.COUNT_ACTION).build()).get().getCount();
        } catch (InterruptedException | ExecutionException e) {
        }
        textStatistics.setText("Фотографий в группе: " + '\n' + count);
        setUpViewsBackground();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void setUpViewsBackground() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                VkRestServiceRequest request = VkRestServiceRequest.builder()
                        .setAction(VK_REST_SERVICE_ACTION.GET_ALL_PHOTOS_URL)
                        .build();
                try {
                    urls.addAll(new VkRestService().execute(request).get().getUrls());
                } catch (InterruptedException | ExecutionException e) {
                }
                try {
                    Integer imageId = DEFAULT_PHOTO_ID;
                    if (urls.size() > imageId && imageId >= 1) {
                        NetworkService networkService = new NetworkService();
                        NetworkServiceRequest requestNet = NetworkServiceRequest.builder()
                                .setAction(NETWORK_SERVICE_ACTION.DOWNLOAD_IMAGE_FROM_URI)
                                .setDataString(urls.get(imageId - 1))
                                .build();
                        Bitmap bitmap = networkService.execute(requestNet).get().getImage();

                        runOnUiThread(new UpdateImageView(bitmap));
                    }
                } catch (InterruptedException | ExecutionException e) {
                }
            }
        });
    }

    public class UpdateImageView implements Runnable {
        private Bitmap bitmap;

        public UpdateImageView(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            image.clearColorFilter();
            image.refreshDrawableState();
            image.setImageBitmap(bitmap);
            image.refreshDrawableState();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}