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

    // CHECK NOTIFICATION CHANNEL
    @TargetApi(Build.VERSION_CODES.O)
    fun isNotificationChannelAvailable(notificationManager: NotificationManager) = notificationManager.getNotificationChannel(channelID) != null

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        notificationManager: NotificationManager
    ) {
        val channel = NotificationChannel(channelID, CHANNEL, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    // CREATE INTENT
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("status", status)
    contentIntent.putExtra("name", name)

    // CREATE PENDING INTENT
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val appImage = (applicationContext.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_assistant_black_24dp, null) } as VectorDrawable).toBitmap()

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(appImage)
        .bigLargeIcon(null)

    // CHECK IF CHANNEL IS AVAILABLE
    if(!isNotificationChannelAvailable(this)) {
        createNotificationChannel(this)
    }


    // BUILD NOTIFICATION
    val builder = NotificationCompat.Builder(
        applicationContext,
        channelID
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(appImage)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            "See status",
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())

}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}

