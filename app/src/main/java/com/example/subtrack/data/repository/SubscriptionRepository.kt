package com.example.subtrack.data.repository

import com.example.subtrack.data.db.Subscription
import com.example.subtrack.data.db.SubscriptionDao
import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(private val subscriptionDao: SubscriptionDao) {

    val allSubscriptions: Flow<List<Subscription>> = subscriptionDao.getAllSubscriptions()

    val totalCost: Flow<Double?> = subscriptionDao.getTotalMonthlyCost()

    suspend fun insert(subscription: Subscription) {
        subscriptionDao.insertSubscription(subscription)
    }

    suspend fun delete(subscription: Subscription) {
        subscriptionDao.deleteSubscription(subscription)
    }

}