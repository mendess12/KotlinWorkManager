package com.example.kotlinworkmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RefreshDatabase(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val getData = inputData
        val number = getData.getInt("intKey", 0)
        updateDatabase(number)
        return Result.success()
    }

    private fun updateDatabase(number: Int) {
        val sharedPreferences =
            context.getSharedPreferences("com.example.kotlinworkmanager", Context.MODE_PRIVATE)
        var savedNumber = sharedPreferences.getInt("number", 0)
        savedNumber = savedNumber + number
        println("saved number : ${savedNumber}")
        sharedPreferences.edit().putInt("number", savedNumber).apply()
    }
}