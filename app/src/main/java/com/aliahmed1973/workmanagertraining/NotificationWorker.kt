package com.aliahmed1973.workmanagertraining

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "NotificationWorker"
class NotificationWorker(context: Context, workerParams: WorkerParameters):Worker(context,workerParams) {
    override fun doWork(): Result {
        if(numofRetry<2)
        {
            numofRetry++
            Log.d(TAG, "doWork: $numofRetry")
            return Result.retry()
        }else {
            val data = inputData.getString("data") ?: "nothing"
            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotification(applicationContext, data)
            return Result.success()
        }
    }
}