package com.example.kotlinworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data.Builder().putInt("intKey", 1).build()
        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            // .setInitialDelay(5,TimeUnit.MINUTES)
            // .addTag("tag")
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        val workRequestTwo: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<RefreshDatabase>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build()

        WorkManager.getInstance(this).enqueue(workRequestTwo)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer {
                if (it.state == WorkInfo.State.RUNNING) {
                    println("running")
                } else if (it.state == WorkInfo.State.FAILED) {
                    println("failed")
                } else if (it.state == WorkInfo.State.SUCCEEDED) {
                    println("succeeded")
                }
            })

        //work mananger iptal etme
        WorkManager.getInstance(this).cancelAllWork()

        //chaining
        val oneTimeRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).beginWith(oneTimeRequest)
            .then(oneTimeRequest)
            .then(oneTimeRequest)
            .enqueue()
    }
}