package com.aliahmed1973.workmanagertraining

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "MultipleWorker"

class FirstWorker(context: Context, workerParams: WorkerParameters): Worker(context,workerParams){
    override fun doWork(): Result {

        val isTrue = inputData.getBoolean("state",false)
        val name = inputData.getString("name")
        return if(isTrue)
        {
            Log.d(TAG, "doWork: success $name")
            Result.success()
        }else
        {
            Log.d(TAG, "doWork: fail $name")
            Result.failure()
        }
    }
}
class SecondWorker(context: Context, workerParams: WorkerParameters): Worker(context,workerParams){
    override fun doWork(): Result {
        val isTrue = inputData.getBoolean("state",false)
        val name = inputData.getString("name")
        return if(isTrue)
        {
            Log.d(TAG, "doWork: success $name")
            Result.success()
        }else
        {
            Log.d(TAG, "doWork: fail $name")
            Result.failure()
        }
    }
}
class ThirdWorker(context: Context, workerParams: WorkerParameters): Worker(context,workerParams){
    override fun doWork(): Result {
        val isTrue = inputData.getBoolean("state",false)
        val name = inputData.getString("name")
        return if(isTrue)
        {
            Log.d(TAG, "doWork: success $name")
            Result.success()
        }else
        {
            Log.d(TAG, "doWork: fail $name")
            Result.failure()
        }
    }
}
