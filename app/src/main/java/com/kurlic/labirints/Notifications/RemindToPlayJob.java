package com.kurlic.labirints.Notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.kurlic.labirints.R;

import java.util.concurrent.TimeUnit;

public class RemindToPlayJob extends JobService
{

    @Override
    public boolean onStartJob(JobParameters params)
    {
        // Выполните необходимую логику для отправки уведомления

        // Создайте и отправьте уведомление

        sendNotification();

        // Запланируйте следующее выполнение задачи через 24 часа
        scheduleJob();

        // Верните true, если есть незавершенная работа, false в противном случае
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        // Возвращайте true, если задача должна быть повторно запланирована, если она была прервана,
        // false в противном случае
        return true;
    }

    private void scheduleJob()
    {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null)
        {
            ComponentName componentName = new ComponentName(this, RemindToPlayJob.class);
            JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                    .setMinimumLatency(TimeUnit.SECONDS.toMillis(15)) // Запланировать задачу через 24 часа
                    .build();
            jobScheduler.schedule(jobInfo);
        }
    }

    private void sendNotification()
    {
        // Создание канала уведомлений (требуется для версии Android 8.0 Oreo и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "my_channel_id";
            String channelName = "My Channel";
            String channelDescription = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null)
            {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Создание и настройка уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.icon2)
                .setContentTitle("Название уведомления")
                .setContentText("Текст уведомления")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Отправка уведомления
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(1, builder.build());
    }



}

