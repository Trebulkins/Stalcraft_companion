package com.example.stalcraft_companion.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.adapters.ItemListingAdapter
import com.example.stalcraft_companion.api.schemas.CategoryGroup
import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.api.schemas.SubcategoryGroup

private const val TAG = "MainFragment"
class MainFragment : Fragment() {
    interface OnItemSelectedListener {
        fun onItemSelected(itemId: String)
    }

    private var listener: OnItemSelectedListener? = null

    companion object {
        private const val ARG_ITEMS = "items"

        fun newInstance(items: ArrayList<Item>): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ITEMS, items)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = arguments?.getParcelableArrayList<Item>(ARG_ITEMS) ?: return
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val categoryGroups = prepareCategoryGroups(items)
        val adapter = CategoryAdapter(categoryGroups) { itemId ->
            listener?.onItemSelected(itemId)
        }

        recyclerView.adapter = adapter
    }

    private fun prepareCategoryGroups(items: List<Item>): List<CategoryGroup> {
        return items.groupBy { it.category }
            .map { (category, categoryItems) ->
                val (withSubcat, withoutSubcat) = categoryItems.partition { it.subcategory.isNotEmpty() }

                CategoryGroup(
                    categoryName = category,
                    subcategories = withSubcat.groupBy { it.subcategory }
                        .map { (subcat, items) -> SubcategoryGroup(subcat, items.map { it.id }) },
                    itemIds = withoutSubcat.map { it.id }
                )
            }
            .sortedBy { it.categoryName }
    }
}