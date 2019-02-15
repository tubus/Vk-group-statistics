package com.github.tubus.vkgroupstatistics.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {
    public static void writeFileToStorage(String fname, Bitmap bitmap, Context context) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/vkGroup/temp";
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

        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
    }
}
