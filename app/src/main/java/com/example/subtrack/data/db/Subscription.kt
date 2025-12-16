package com.example.subtrack.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "subscription_table")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val price: Double,
    val paymentDay: Int,
    val category: String,
    val colorHex: String
) : Parcelable