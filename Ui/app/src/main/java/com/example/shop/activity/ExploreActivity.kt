package com.example.shop.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.ListItemsAdapter
import com.example.shop.databinding.ActivityExploreBinding
import com.example.shop.viewModel.ExploreViewModel

class ExploreActivity : BaseActivity() {

    private lateinit var binding: ActivityExploreBinding
    private lateinit var adapter: ListItemsAdapter
    private val viewModel: ExploreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        setupRecyclerView()
        setupSearch()
        setupFilters()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListItemsAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterItems()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFilters() {
        val sizes = listOf("(Size) Tất cả", "36", "37", "38", "39", "40", "41", "42", "43")
        val prices = listOf("(Giá) Tất cả", "Dưới 500.000", "500.000 - 2.000.000", "Trên 2.000.000")

        binding.sizeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizes)
        binding.priceSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prices)

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterItems()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.sizeSpinner.onItemSelectedListener = spinnerListener
        binding.priceSpinner.onItemSelectedListener = spinnerListener
    }

    private fun filterItems() {
        try {
            val searchText = binding.searchEditText.text.toString().lowercase()
            val selectedSize = binding.sizeSpinner.selectedItem.toString()
            val selectedPrice = binding.priceSpinner.selectedItem.toString()

            viewModel.filterItems(searchText, selectedSize, selectedPrice)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi lọc dữ liệu", Toast.LENGTH_SHORT).show()
        }
    }


    private fun observeViewModel() {
        viewModel.filteredList.observe(this) { items ->
            adapter.updateList(items)
            binding.progressBar.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
