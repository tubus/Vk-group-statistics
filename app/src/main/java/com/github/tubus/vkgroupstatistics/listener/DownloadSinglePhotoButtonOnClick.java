package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ImageView;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;
import java.util.ArrayList;
import java.util.List;
import static com.github.tubus.vkgroupstatistics.consts.Consts.DEFAULT_PHOTO_ID;
import static com.github.tubus.vkgroupstatistics.utils.FileUtils.writeFileToStorage;
import static com.github.tubus.vkgroupstatistics.utils.ParseUtils.parseIntegerOrDefault;

public class DownloadSinglePhotoButtonOnClick implements View.OnClickListener, Runnable {

    private final Activity activity;
    private final TextInputEditText textInputEditText;
    private final ImageView imageView;

    public DownloadSinglePhotoButtonOnClick(TextInputEditText textInputEditText,  ImageView imageView,
                                            Activity activity) {
        this.activity = activity;
        this.textInputEditText = textInputEditText;
        this.imageView = imageView;
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        synchronized (this) {
            final VkRestService vkRestService = new VkRestService();

            final int photoId = parseIntegerOrDefault(textInputEditText.getText().toString(), DEFAULT_PHOTO_ID);

            List<byte[]> images = new ArrayList<>();
            try {
                VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                request.setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_SINGLE_ACTION);
                request.setId(photoId);
                images.addAll(vkRestService.execute(request).get().getImage());
            } catch (Exception ex) {
            }
            if (!images.isEmpty()) {
                byte[] image = images.get(0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                activity.runOnUiThread(new RunImageViewUpdateOnUI(bitmap));
                writeFileToStorage("vkgroupstatistics_" + photoId + ".jpeg", bitmap, activity.getApplicationContext());
            }
        }
    }

    private class RunImageViewUpdateOnUI implements Runnable {
        private final Bitmap bitmap;

        private RunImageViewUpdateOnUI(Bitmap bitmap) {
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