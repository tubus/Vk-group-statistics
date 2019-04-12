package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.github.tubus.vkgroupstatistics.restful.dto.NETWORK_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.NetworkServiceRequest;
import com.github.tubus.vkgroupstatistics.restful.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.VkRestServiceRequest;
import com.github.tubus.vkgroupstatistics.restful.service.NetworkService;
import com.github.tubus.vkgroupstatistics.restful.service.VkRestService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.github.tubus.vkgroupstatistics.consts.Consts.DEFAULT_PHOTO_ID;
import static com.github.tubus.vkgroupstatistics.utils.ParseUtils.parseIntegerOrDefault;

public class FloatingNavigationOnClick implements View.OnClickListener, Runnable {

    private final Activity activity;
    private final EditText textInputEditText;
    private final ImageView imageView;
    private final boolean isRight;

    public static List<String> urls = new ArrayList<>();

    public FloatingNavigationOnClick(boolean isRight, EditText textInputEditText, ImageView imageView, Activity activity) {
        this.activity = activity;
        this.textInputEditText = textInputEditText;
        this.imageView = imageView;
        this.isRight = isRight;
        if (urls.isEmpty()) {
            VkRestServiceRequest request = VkRestServiceRequest.builder()
                    .setAction(VK_REST_SERVICE_ACTION.GET_ALL_PHOTOS_URL)
                    .build();
            try {
                urls.addAll(new VkRestService().execute(request).get().getUrls());
            } catch (InterruptedException | ExecutionException e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        synchronized (this) {

            int photoId = parseIntegerOrDefault(textInputEditText.getText().toString(), DEFAULT_PHOTO_ID) + (isRight ? 1 : -1);
            if (photoId == 0) {
                photoId = urls.size();
            } else if (photoId == urls.size() + 1) {
                photoId = 1;
            }
            try {
                NetworkServiceRequest request = NetworkServiceRequest.builder()
                        .setAction(NETWORK_SERVICE_ACTION.DOWNLOAD_IMAGE_FROM_URI)
                        .setDataString(urls.get(photoId - 1))
                        .build();
                Bitmap image = new NetworkService().execute(request).get().getImage();
                activity.runOnUiThread(new RunImageAndTextViewUpdateOnUI(image, photoId));
            } catch (Exception ex) {
            }
        }
    }

    private class RunImageAndTextViewUpdateOnUI implements Runnable {
        private final Bitmap bitmap;
        private final int photoId;

        private RunImageAndTextViewUpdateOnUI(Bitmap bitmap, int photoId) {
            this.bitmap = bitmap;
            this.photoId = photoId;
        }

        @Override
        public void run() {
            imageView.clearColorFilter();
            imageView.refreshDrawableState();
            imageView.setImageBitmap(bitmap);
            imageView.refreshDrawableState();

            textInputEditText.setText("" + photoId);
        }
    }
}