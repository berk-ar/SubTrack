package com.example.subtrack

import android.app.Application
import com.example.subtrack.data.db.SubTrackDatabase
import com.example.subtrack.data.repository.SubscriptionRepository

class SubTrackApplication : Application() {
    val database by lazy { SubTrackDatabase.getDatabase(this) }
    val repository by lazy { SubscriptionRepository(database.subscriptionDao()) }
}