package com.github.tubus.vkgroupstatistics.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.tubus.vkgroupstatistics.R;
import com.github.tubus.vkgroupstatistics.service.SubscriptionNotificationService;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new ServiceStarter(this)).run();

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {
            android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.hide();
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //TODO: startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }, 40); //TODO: 4000
    }


    private class ServiceStarter implements Runnable {

        private final Activity activity;

        public ServiceStarter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            startService(new Intent(activity, SubscriptionNotificationService.class));
        }
    }
}