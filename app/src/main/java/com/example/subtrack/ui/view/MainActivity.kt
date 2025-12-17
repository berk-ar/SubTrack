package com.example.subtrack.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subtrack.R
import com.example.subtrack.SubTrackApplication
import com.example.subtrack.databinding.ActivityMainBinding
import com.example.subtrack.ui.adapter.SubscriptionAdapter
import com.example.subtrack.ui.viewmodel.SubscriptionViewModel
import com.example.subtrack.ui.viewmodel.SubscriptionViewModelFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SubscriptionAdapter

    private val viewModel: SubscriptionViewModel by viewModels {
        SubscriptionViewModelFactory((application as SubTrackApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = SubscriptionAdapter()
        binding.rv.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddSubscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeData() {
        viewModel.allSubscriptions.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.totalCost.observe(this) { total ->
            val cost = total ?: 0.0

            binding.textTotalAmount.text = String.format(Locale.getDefault(), "₺%.2f", cost)
        }
    }


}