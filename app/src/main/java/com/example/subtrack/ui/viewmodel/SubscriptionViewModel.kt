package com.example.subtrack.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.subtrack.data.db.Subscription
import com.example.subtrack.data.repository.SubscriptionRepository
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val repository: SubscriptionRepository) : ViewModel() {
    val allSubscriptions: LiveData<List<Subscription>> = repository.allSubscriptions.asLiveData()

    val totalCost: LiveData<Double?> = repository.totalCost.asLiveData()

    fun insert(subscription: Subscription) = viewModelScope.launch {
        repository.insert(subscription)
    }

    fun delete(subscription: Subscription) = viewModelScope.launch {
        repository.delete(subscription)
    }
}

class SubscriptionViewModelFactory(private val repository: SubscriptionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubscriptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}