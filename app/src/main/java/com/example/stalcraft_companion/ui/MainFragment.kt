package com.example.stalcraft_companion.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stalcraft_companion.data.modles.CategoryGroup
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.SubcategoryGroup
import com.example.stalcraft_companion.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    interface OnItemSelectedListener {
        fun onItemSelected(item: Item)
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: ItemViewModel
    private var listener: OnItemSelectedListener? = null
    private lateinit var adapter: CategoryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ItemViewModel::class.java]
        adapter = CategoryAdapter { item -> listener?.onItemSelected(item) }

        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MainFragment.adapter
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                val categories = items.groupBy { it.category }.map { (category, items) ->
                    CategoryGroup(
                        categoryName = category,
                        subcategories = items.groupBy { it.subcategory }.map { (subcategory, items) ->
                            SubcategoryGroup(subcategory, items)
                        }
                    )
                }
                adapter.submitList(categories)
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}