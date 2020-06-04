package se.mobileinteraction.sleip.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.ui.progressBar.*
import timber.log.Timber

fun makeStatusNotification(message: String, context: Context) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name =
            VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description =
            VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)


        val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context,
                    CHANNEL_ID
                )
                    .setContentTitle(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

}

   fun sleep() {
    try {
        Thread.sleep(DELAY_TIME_MILLIS, 0)
    } catch (e: InterruptedException) {
        Timber.e(e)
    }

}
