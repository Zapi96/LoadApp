package com.udacity

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap


private val NOTIFICATION_ID = 0
private const val CHANNEL_ID = "channelId"
private const val CHANNEL = "Downloads"

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context,channelID: String,status:String, name:String?) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    Log.d("SUCCESS","Notification")

    @TargetApi(Build.VERSION_CODES.O)
    fun isNotificationChannelAvailable(notificationManager: NotificationManager) = notificationManager.getNotificationChannel(channelID) != null

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        notificationManager: NotificationManager
    ) {
        val channel = NotificationChannel(channelID, CHANNEL, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("status", status)
    contentIntent.putExtra("name", name)


    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // TODO: Step 2.0 add style
//    val appImage = BitmapFactory.decodeResource(
//        applicationContext.resources,
//        R.drawable.ic_assistant_black_24dp
//    )
    val appImage = (applicationContext.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_assistant_black_24dp, null) } as VectorDrawable).toBitmap()

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(appImage)
        .bigLargeIcon(null)



    if(!isNotificationChannelAvailable(this)) {
        createNotificationChannel(this)
    }


    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        channelID
    )

        // TODO: Step 1.8 use the new 'breakfast' notification channel

        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // TODO: Step 2.1 add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(appImage)

        // TODO: Step 2.3 add  action
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            "See status",
            contentPendingIntent
        )

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())





}



// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

