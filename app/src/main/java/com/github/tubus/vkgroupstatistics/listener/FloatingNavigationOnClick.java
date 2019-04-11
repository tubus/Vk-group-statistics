package com.github.tubus.vkgroupstatistics.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequest;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;
import java.util.ArrayList;
import java.util.List;
import static com.github.tubus.vkgroupstatistics.consts.Consts.DEFAULT_PHOTO_ID;
import static com.github.tubus.vkgroupstatistics.utils.ParseUtils.parseIntegerOrDefault;

public class FloatingNavigationOnClick implements View.OnClickListener, Runnable {

    private final Activity activity;
    private final EditText textInputEditText;
    private final ImageView imageView;
    private final boolean isRight;

    public FloatingNavigationOnClick(boolean isRight, EditText textInputEditText, ImageView imageView,
                                     Activity activity) {
        this.activity = activity;
        this.textInputEditText = textInputEditText;
        this.imageView = imageView;
        this.isRight = isRight;
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        synchronized (this) {

            final int photoId = parseIntegerOrDefault(textInputEditText.getText().toString(), DEFAULT_PHOTO_ID) + (isRight ? 1 : -1);

            List<byte[]> images = new ArrayList<>();
            try {
                VkRestServiceRequest request = VkRestServiceRequest.builder()
                        .setAction(VK_REST_SERVICE_ACTION.DOWNLOAD_SINGLE_ACTION)
                        .setId(photoId).build();
                images.addAll(new VkRestService().execute(request).get().getImage());
            } catch (Exception ex) {
            }
            if (!images.isEmpty()) {
                byte[] image = images.get(0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                activity.runOnUiThread(new RunImageAndTextViewUpdateOnUI(bitmap, photoId));
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