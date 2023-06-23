package com.kurlic.labirints.Notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.kurlic.labirints.MainActivity;
import com.kurlic.labirints.R;

import java.util.Random;


public class RemindToPlayWorker extends Worker
{
    public RemindToPlayWorker(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        createNotificationChannel();
        sendNotification();

        return Result.success();
    }

    private static final String CHANNEL_ID = "RemindToPlayId";

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = getApplicationContext().getString(R.string.app_name);
            String description = "RemindToPlayChannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            if (notificationManager != null)
            {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendNotification()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        MainActivity.cleanDataForIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);


        String[] notificationsArray = getApplicationContext().getResources().getStringArray(R.array.notification_remindToPlay);
        Random random = new Random();
        int randomNumber = random.nextInt(notificationsArray.length);
        String notificationDescribtion = notificationsArray[randomNumber];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon2)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setContentText(notificationDescribtion)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Labyrinth", "No permission notifications");
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
