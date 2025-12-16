package com.example.subtrack.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSubscription(sub: Subscription)

    @Delete
    suspend fun deleteSubscription(sub: Subscription)

    // Flow
    @Query("SELECT * FROM subscription_table ORDER BY paymentDay ASC")
    fun getAllSubscriptions(): Flow<List<Subscription>>

    @Query("SELECT SUM(price) FROM subscription_table")
    fun getTotalMonthlyCost(): Flow<Double?>

}