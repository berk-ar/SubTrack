package com.example.subtrack.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.subtrack.R
import com.example.subtrack.SubTrackApplication
import java.util.Calendar

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val repository = (applicationContext as SubTrackApplication).repository

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrowDay = calendar.get(Calendar.DAY_OF_MONTH)

        return try {
            val database = (applicationContext as SubTrackApplication).database
            val subs = database.subscriptionDao().getAllSubscriptionsOneShot()

            val dueTomorrow = subs.filter { it.paymentDay == tomorrowDay }

            dueTomorrow.forEach { sub ->
                showNotification(sub.name, sub.price)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(name: String, price: Double) {
        val context = applicationContext
        val channelId = "subtrack_channel"
        val notificationId = name.hashCode()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Abonelik Bildirimleri",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Yarın ödeme var!")
                .setContentText("$name için ₺$price ödeme yapmalısın.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)
        }
    }
}