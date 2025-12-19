package com.example.subtrack.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.subtrack.R
import com.example.subtrack.SubTrackApplication
import com.example.subtrack.databinding.ActivityMainBinding
import com.example.subtrack.ui.adapter.SubscriptionAdapter
import com.example.subtrack.ui.viewmodel.SubscriptionViewModel
import com.example.subtrack.ui.viewmodel.SubscriptionViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SubscriptionAdapter

    private val viewModel: SubscriptionViewModel by viewModels {
        SubscriptionViewModelFactory((application as SubTrackApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        setupRecyclerView()
        setupClickListeners()
        setupSwipeToDelete()
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

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val subscriptionToDelete = adapter.currentList[position]
                    viewModel.delete(subscriptionToDelete)

                    Snackbar.make(
                        binding.root,
                        "${subscriptionToDelete.name} silindi",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("GERİ AL") {
                            viewModel.insert(subscriptionToDelete)
                        }
                        .setBackgroundTint(getColor(R.color.primary_dark))
                        .setActionTextColor(getColor(R.color.accent_pink))
                        .show()
                }


            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rv)
    }
}