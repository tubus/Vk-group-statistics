package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.tubus.vkgroupstatistics.restful.dto.NETWORK_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.NetworkServiceRequest;
import com.github.tubus.vkgroupstatistics.restful.service.NetworkService;

import java.util.concurrent.ExecutionException;

import static com.github.tubus.vkgroupstatistics.activities.PhotosStatistcsActivity.urls;

public class OnEditorActionListenerImple implements TextView.OnEditorActionListener {

    private final ImageView imageView;

    private final Activity activity;

    public OnEditorActionListenerImple(ImageView imageView, Activity activity) {
        this.imageView = imageView;
        this.activity = activity;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String text = v.getText().toString();
            try {
                int photoId = Integer.parseInt(text);
                NetworkServiceRequest request = NetworkServiceRequest.builder()
                        .setAction(NETWORK_SERVICE_ACTION.DOWNLOAD_IMAGE_FROM_URI)
                        .setDataString(urls.get(photoId - 1))
                        .build();
                Bitmap bitmap = new NetworkService().execute(request).get().getImage();

                activity.runOnUiThread(new RunImageUpdateOnUI(bitmap));

            } catch (NumberFormatException | InterruptedException | ExecutionException ex) {
            }

        }
        return false;
    }

    private class RunImageUpdateOnUI implements Runnable {
        private final Bitmap bitmap;

        private RunImageUpdateOnUI(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            imageView.clearColorFilter();
            imageView.refreshDrawableState();
            imageView.setImageBitmap(bitmap);
            imageView.refreshDrawableState();
        }
    }
}