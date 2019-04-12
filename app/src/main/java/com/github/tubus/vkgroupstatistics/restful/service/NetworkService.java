package com.github.tubus.vkgroupstatistics.restful.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.github.tubus.vkgroupstatistics.restful.dto.NETWORK_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.restful.dto.NetworkServiceRequest;
import com.github.tubus.vkgroupstatistics.restful.dto.NetworkServiceResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetworkService extends AsyncTask<NetworkServiceRequest, Void, NetworkServiceResponse> {

    public NetworkService() {
    }

    @Override
    protected NetworkServiceResponse doInBackground(NetworkServiceRequest... requests) {
        if (requests.length != 1) {
            return null;
        }

        NetworkServiceResponse response = new NetworkServiceResponse();

        NETWORK_SERVICE_ACTION action = requests[0].getAction();
        switch (action) {
            case DOWNLOAD_IMAGE_FROM_URI:
                response.setImage(getImageFromUri(requests[0].getData()));
                break;
        }

        return response;
    }

    private Bitmap getImageFromUri(String uriString) {
        URLConnection conn = null;
        try {
            conn = new URL(uriString).openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, 8192);
            return BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
        }
        return null;
    }
}