package com.example.subtrack

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.subtrack.data.db.SubTrackDatabase
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.util.NotificationWorker
import java.util.concurrent.TimeUnit

class SubTrackApplication : Application() {
    val database by lazy { SubTrackDatabase.getDatabase(this) }
    val repository by lazy { SubscriptionRepository(database.subscriptionDao()) }

    override fun onCreate() {
        super.onCreate()
        setupWorker()
    }

    private fun setupWorker() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailySubscriptionCheck",
            ExistingPeriodicWorkPolicy.KEEP,    // KEEP: zaten varsa yenisini eklemez
            workRequest
        )
    }
}