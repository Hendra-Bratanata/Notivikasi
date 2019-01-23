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

        getCurrentWeather();
        return true;
    }

    private void getCurrentWeather() {

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public static final String TAG = MyJobService.class.getSimpleName();

    final String APP_ID = "f864d0880a4cd0cf243ef3344166893d";

    public static String EXTRA_CITY = "extra_city";

    private void getCurrentWeather(final JobParameters job) {

        String city = job.getExtras().getString(EXTRA_CITY);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.openeweathermap.org/data/2.5/weather?q=" + city + "&appid="+APP_ID;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String currenWeather = responseObject.getJSONArray("weather").getJSONObject(0).getString("main");
                    String deskripsi = responseObject.getJSONArray("weather").getJSONObject(0).getString("deskripsi");

                    double tempInKelvin = responseObject.getJSONObject("main").getDouble("temp");
                    double tempInCelcius = tempInKelvin - 273;
                    String temperature = new DecimalFormat("##.##").format(tempInCelcius);

                    String tittle = "current weather ";
                    String massage = currenWeather + ',' + deskripsi + "with" + temperature + "celcius";
                    int notifid = 100;
                    showNotification(getApplicationContext(), tittle, massage, notifid);

                    jobFinished(job, false);


                } catch (JSONException e) {
                    jobFinished(job, true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                jobFinished(job, true);

            }
        });
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
