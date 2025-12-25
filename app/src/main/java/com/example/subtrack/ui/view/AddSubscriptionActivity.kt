package com.example.subtrack.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.subtrack.SubTrackApplication
import com.example.subtrack.data.db.Subscription
import com.example.subtrack.databinding.ActivityAddSubscriptionBinding
import com.example.subtrack.ui.viewmodel.SubscriptionViewModel
import com.example.subtrack.ui.viewmodel.SubscriptionViewModelFactory
import com.google.android.material.chip.Chip

class AddSubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubscriptionBinding

    private val viewModel: SubscriptionViewModel by viewModels {
        SubscriptionViewModelFactory((application as SubTrackApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            saveSubscription()
        }
    }

    private fun saveSubscription() {
        val name = binding.etName.text.toString()
        val priceStr = binding.etPrice.text.toString()
        val paymentDayStr = binding.etDay.text.toString()

        if (name.isEmpty() || priceStr.isEmpty() || paymentDayStr.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        if (paymentDayStr >= 32.toString() || paymentDayStr <= 0.toString()) {
            Toast.makeText(this, "Lütfen geçerli bir ödeme günü girin", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedChipId = binding.chipGroupCategory.checkedChipId
        val selectedChip = binding.chipGroupCategory.findViewById<Chip>(selectedChipId)
        val category = selectedChip?.text?.toString() ?: ""

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val day = paymentDayStr.toIntOrNull() ?: 0

        val colorHex = "#000000"

        val subscription = Subscription(
            name = name,
            price = price,
            paymentDay = day,
            category = category,
            colorHex = colorHex
        )

        viewModel.insert(subscription)

        Toast.makeText(this, "Abonelik başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
        finish()
    }
}