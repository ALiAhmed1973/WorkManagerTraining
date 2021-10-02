package com.aliahmed1973.workmanagertraining

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.*
import com.aliahmed1973.workmanagertraining.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private lateinit var workManger:WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        workManger = WorkManager.getInstance(this)

        binding.buttonStartWork.setOnClickListener {
            val text= binding.editTextTextPersonName.text.toString()
            val data = workDataOf("data" to text)
            val constraints = Constraints.Builder().setRequiresCharging(true).build()
            val workRequest =  OneTimeWorkRequestBuilder<NotificationWorker>()
//                .setInitialDelay(10, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setInputData(data)
                .setBackoffCriteria(BackoffPolicy.LINEAR,10,TimeUnit.SECONDS)
                .build()
            workManger.enqueue(workRequest)
        }

        binding.buttonStopWork.setOnClickListener {
            workManger.cancelAllWork()
        }

        binding.buttonSingleChain.setOnClickListener {
            singleChain()
        }
        binding.buttonSingleChainWithGroup.setOnClickListener {
            singleChainWithGroup()
        }
        binding.buttonMultipleChain.setOnClickListener {
            multipleChain()
        }
    }



    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notify by Worker"
            val descriptionText = "Test WorkManager"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun singleChain()
    {
        val workRequest1 =  OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workRequest1"))
            .build()
        val workRequest2 =  OneTimeWorkRequestBuilder<SecondWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workRequest2"))
            .build()
        val workRequest3 =  OneTimeWorkRequestBuilder<ThirdWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workRequest3"))
            .build()
        /// if one of chain element fail all chain fail
        workManger.beginWith(workRequest1).then(workRequest2).then(workRequest3).enqueue()
    }
    private fun singleChainWithGroup()
    {


        val workRequest1 =  OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workRequest1"))
            .build()
        ///work of the same type of FirstWorker
        val anotherWorkRequest1 =  OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(workDataOf("state" to false,"name" to "anotherWorkRequest1"))
            .build()

        val workRequest2 =  OneTimeWorkRequestBuilder<SecondWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workRequest2"))
            .build()
        val workRequest3 =  OneTimeWorkRequestBuilder<ThirdWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workRequest3"))
            .build()
        ///we can make multiple work in the in list because they are same type but if on of it fail all chain fail
        workManger.beginWith(listOf(workRequest1,anotherWorkRequest1)).then(workRequest2).then(workRequest3).enqueue()
    }

    private fun multipleChain() {
        val workChain1Request1 =  OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workChain1Request1"))
            .build()
        val workChain1Request2 =  OneTimeWorkRequestBuilder<SecondWorker>()
            .setInputData(workDataOf("state" to false,"name" to "workChain1Request2"))
            .build()
        val workChain1Request3 =  OneTimeWorkRequestBuilder<ThirdWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workChain1Request3"))
            .build()

        val workChain2Request1 =  OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workChain2Request1"))
            .build()
        val workChain2Request2 =  OneTimeWorkRequestBuilder<SecondWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workChain2Request2"))
            .build()
        val workChain2Request3 =  OneTimeWorkRequestBuilder<ThirdWorker>()
            .setInputData(workDataOf("state" to true,"name" to "workChain2Request3"))
            .build()

        ///Multiple chains if one of chain fail not to affect the other
        val firstWorkChain= workManger.beginWith(workChain1Request1).then(workChain1Request2).then(workChain1Request3)
        val secondWorkChain= workManger.beginWith(workChain2Request1).then(workChain2Request2).then(workChain2Request3)

        val allChains = WorkContinuation.combine(listOf(firstWorkChain,secondWorkChain))
        allChains.enqueue()
    }
}