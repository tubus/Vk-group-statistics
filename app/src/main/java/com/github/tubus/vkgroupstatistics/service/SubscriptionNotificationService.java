package com.github.tubus.vkgroupstatistics.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import com.github.tubus.vkgroupstatistics.ActionChoiceActivity;
import com.github.tubus.vkgroupstatistics.R;
import com.github.tubus.vkgroupstatistics.dto.VK_REST_SERVICE_ACTION;
import com.github.tubus.vkgroupstatistics.dto.VkRestServiceRequest;
import com.github.tubus.vkgroupstatistics.rest.service.VkRestService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class SubscriptionNotificationService extends Service {

    //TODO: put this settings into settings file or into application settings
    public static final long SUBSCRIPTION_CHECKING_INTERVAL = 5 * 60 * 1000;
    public static final long SUBSCRIPTION_CHECKING_DELAY = 1000;
    public static final int CHECKING_LAST_HOURS = 24;
    public static final long KEEPING_SUBSCRIBED_LATELY_TIME_MILLIS = 2 * 60 * 60 * 1000;

    private Map<String, Long> subscribedLately = new HashMap<>();

    private Timer timer = null;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("on bind called");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(timer != null) {
            timer.cancel();
        } else {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new CheckSubscriptionTask(), SUBSCRIPTION_CHECKING_DELAY, SUBSCRIPTION_CHECKING_INTERVAL);
    }

    private class CheckSubscriptionTask extends TimerTask {

        @Override
        public void run() {
            handler.post(new TaskWrapper());
        }
    }

    private class TaskWrapper implements Runnable {

        private int notificationId = 23;

        @Override
        public void run() {
            VkRestServiceRequest request = VkRestServiceRequest.builder()
            .setAction(VK_REST_SERVICE_ACTION.SUBSCRIPTION_LAST_ACTION)
            .setHours(CHECKING_LAST_HOURS)
            .setChangedStatus("subscribed").build();

            List<String> usersList = new ArrayList<>();
            try {
                usersList.addAll(new VkRestService().execute(request).get().getUsersList());
            } catch (InterruptedException | ExecutionException e) {
                //TODO: remake it with logging
                e.printStackTrace();
            }
            List<String> toShow = new ArrayList<>();
            for (String s : usersList) {
                if (!subscribedLately.containsKey(s)) {
                    subscribedLately.put(s, System.currentTimeMillis());
                    toShow.add(s);
                }
            }

            Map<String, Long> subscribedLatelyRefreshed = new HashMap<>();
            Set<String> strings = subscribedLately.keySet();
            for (String user : strings) {
                Long timeUserStartedFollowing = subscribedLately.get(user);
                if (System.currentTimeMillis() - timeUserStartedFollowing < KEEPING_SUBSCRIBED_LATELY_TIME_MILLIS) {
                    subscribedLatelyRefreshed.put(user, timeUserStartedFollowing);
                }
            }
            subscribedLately.clear();
            subscribedLately.putAll(subscribedLatelyRefreshed);


            for (int index = 0; index < toShow.size(); index++) {
                Toast.makeText(getApplicationContext(), toShow.get(index) + " subscribed", Toast.LENGTH_SHORT).show();
                call(toShow.get(index));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        private void call(String user) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.background_light)
                            .setContentTitle("Subscibed!")
                            .setContentText(user);
            Intent resultIntent = new Intent(getApplicationContext(), ActionChoiceActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(ActionChoiceActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                            0, PendingIntent.FLAG_UPDATE_CURRENT );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationId, mBuilder.build());
            notificationId++;
        }
    }
}