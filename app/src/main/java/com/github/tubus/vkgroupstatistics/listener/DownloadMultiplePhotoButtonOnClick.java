package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequesWrapper;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;

import static com.github.tubus.vkgroupstatistics.utils.FileUtils.writeFileToStorage;

public class DownloadMultiplePhotoButtonOnClick implements View.OnClickListener, Runnable {
    private ImageView imageView;
    private ProgressBar bar;
    private Activity activity;

    VkRestService vkRestService = new VkRestService();

    public DownloadMultiplePhotoButtonOnClick(ImageView imageView, ProgressBar bar, Activity activity) {
        this.imageView = imageView;
        this.bar = bar;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        VkRestServiceRequesWrapper request1 = new VkRestServiceRequesWrapper();
        request1.setAction(VK_REST_SERVICE_ACTION.COUNT_ACTION);
        try {
            Integer count = vkRestService.execute(request1).get().getCount();
            for (int index = 1; index <= count; index++) {
                VkRestService vkRestServiceC = new VkRestService();
                VkRestServiceRequesWrapper request = new VkRestServiceRequesWrapper();
                request.setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_SINGLE_ACTION);
                request.setId(index);
                byte[] image = vkRestServiceC.execute(request).get().getImage().get(0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                activity.runOnUiThread(new RunImageViewUpdateOnUI(bitmap));
                activity.runOnUiThread(new RunProgresBarUpdateOnUI(index / count));
                writeFileToStorage("vkgroupstatistics_" + index + ".jpeg", bitmap, activity.getApplicationContext());
                Thread.sleep(500);
            }
        } catch (Exception exception) {
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

    private class RunProgresBarUpdateOnUI implements Runnable {
        private final double ratio;

        private RunProgresBarUpdateOnUI(double ratio) {
            this.ratio = ratio;
        }

        @Override
        public void run() {
            bar.setProgress((int) (bar.getMax() * ratio));
        }
    }
}
