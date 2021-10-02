package com.aliahmed1973.workmanagertraining

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

private const val notificationId=123
const val CHANNEL_ID="notification-work"
var numofRetry=0
fun NotificationManager.sendNotification(context:Context,message:String)
{
    var builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Work Manager")
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(notificationId,builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
