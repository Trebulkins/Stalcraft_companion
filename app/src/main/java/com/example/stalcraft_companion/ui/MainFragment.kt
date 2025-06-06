package com.example.stalcraft_companion.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.databinding.FragmentMainBinding

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

        viewModel.items.observe(viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                val categories = CategoryUtils.groupItemsByCategories(items)
                adapter.submitCategories(categories)
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}