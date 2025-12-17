package com.example.subtrack.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.subtrack.data.db.Subscription
import com.example.subtrack.databinding.ItemSubscriptionBinding
import java.util.Locale

class SubscriptionAdapter :
    ListAdapter<Subscription, SubscriptionAdapter.SubscriptionViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding =
            ItemSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SubscriptionViewHolder(private val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscription: Subscription) {
            binding.apply {
                textName.text = subscription.name
                textPrice.text = String.format(Locale.getDefault(), "₺%.2f", subscription.price)
                textDate.text = "Her ayın ${subscription.paymentDay}. günü"

                textInitial.text = subscription.name.firstOrNull()?.uppercase() ?: "?"
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Subscription>() {
        override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription) =
            oldItem == newItem

    }

}