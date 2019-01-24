package com.anime.notivikasi;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {

        getCurrentWeather(job);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }



    private void getCurrentWeather(final JobParameters job) {

      showNotification(getApplicationContext(),"TEST","Pesan",100);
      jobFinished(job,false);
    }

    private void showNotification(Context context, String tittle, String massage, int notifid) {


        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentText(tittle).setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentText(massage)
                .setColor(ContextCompat.getColor(context, android.R.color.holo_blue_bright))

                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmsound);

        notificationManagerCompat.notify(notifid, builder.build());

    }
}
