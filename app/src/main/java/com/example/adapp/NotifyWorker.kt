package com.example.adapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(val context: Context, params: WorkerParameters) : Worker(context, params){

    override fun doWork(): Result {
        val  category = inputData.getStringArray("categories") ?: ""

        val outputData = Data.Builder()
                .putStringArray("cat", category as Array<out String>)
                .build()

        return Result.success(outputData)
    }

    private fun sendNotification(notify: List<String>) {

        val nManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var builder : Notification.Builder

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    "Test", "AdApp",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            nManager.createNotificationChannel(channel)
            builder = Notification.Builder(context, "Test")
        }
        else{
            builder = Notification.Builder(context)
        }

        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
        builder.setContentTitle("New Ad is posted!")
        builder.setContentText(notify.toString())
        builder.setAutoCancel(true)

        val myNotification = builder.build()

        nManager.notify(1, myNotification)
    }
}