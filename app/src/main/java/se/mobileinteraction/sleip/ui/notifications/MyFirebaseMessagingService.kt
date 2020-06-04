package se.mobileinteraction.sleip.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import se.mobileinteraction.sleip.MainActivity
import se.mobileinteraction.sleip.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.data.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        if (remoteMessage.notification != null) {
            remoteMessage.notification?.title?.let { title ->
                remoteMessage.notification?.body?.let { body ->
                    sendNotification(title, body)
                }
            }
        }

        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data["Nick"]?.let {title->
                remoteMessage.data[""]?.let {body ->
                sendNotification(title , body) } }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }


    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun sendNotification(title: String, body: String?) {

        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    applicationContext.getString(R.string.notification_channel_id),
                    applicationContext.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)

            notificationManager.createNotificationChannel(notificationChannel)


            val contentIntent = Intent(this, MainActivity::class.java)
            contentIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val contentPendingIntent = PendingIntent.getActivity(
                    this,
                    REQUEST_CODE,
                    contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )


            val sleipImage = BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.logosleip
            )

            val builder = NotificationCompat.Builder(
                    this,
                    applicationContext.getString(R.string.notification_channel_id)
            )
            builder.apply {
                setAutoCancel(true)
                setSmallIcon(R.drawable.logo_sleip)
                setContentTitle(title)
                setContentText(body)
                setWhen(System.currentTimeMillis())
                setSmallIcon(R.drawable.logo_sleip)
                setLargeIcon(sleipImage)
                setContentIntent(contentPendingIntent)
                color = applicationContext.getColor(R.color.colorUsedBlue)
            }
            notificationManager.notify(333, builder.build())

        }
    }

    companion object {
         const val TAG = "MyFirebaseMsgService"
         const val REQUEST_CODE = 9
    }
}