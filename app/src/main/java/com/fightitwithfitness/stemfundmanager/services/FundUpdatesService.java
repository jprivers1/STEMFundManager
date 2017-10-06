package com.fightitwithfitness.stemfundmanager.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.fightitwithfitness.stemfundmanager.App;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.activities.FundDetailActivity;
import com.fightitwithfitness.stemfundmanager.activities.MainActivity;

/**
 * Created by jpriv on 9/28/2017.
 */

public class FundUpdatesService extends IntentService {
    private static final String TAG = "FundUpdateService";

    //2 minute interval for alarm
    private static final int INTERVAL = 1000 * 60 * 2;

    public static Intent newIntent(Context context){
        return new Intent(context, FundUpdatesService.class);
    }

    public FundUpdatesService() {
        super(TAG);
    }

    public static void setServiceAlarm(Context context){
        Intent i = FundUpdatesService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL, pi);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(!App.get().isNetworkAvailableAndConnected(this)){
            return;
        }

        int fakeUpdatedFundId = 2;

        Resources resources = getResources();

        Intent main = MainActivity.newIntent(this);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent detail = FundDetailActivity.newIntent(this, fakeUpdatedFundId);
        PendingIntent pi = PendingIntent.getActivities(this,
                0, new Intent[]{main, detail}, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.fund_updated))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.fund_updated))
                .setContentText(resources.getString(R.string.fund_updated_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(0, notification);
    }
}
