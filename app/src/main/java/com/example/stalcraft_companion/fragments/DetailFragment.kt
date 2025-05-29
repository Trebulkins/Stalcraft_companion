package com.example.stalcraft_companion.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.api.schemas.ListingItem

class DetailFragment : Fragment() {

    companion object {
        private const val ARG_ITEM = "item"

        fun newInstance(item: ListingItem?): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getParcelable<ListingItem>(ARG_ITEM)

        if (item != null) {
            view.findViewById<TextView>(R.id.titleTextView).text = item.name
            view.findViewById<TextView>(R.id.categoryTextView).text = "Category: ${item.category}"

            // Заполнение других полей информации об объекте
        } else {
            view.findViewById<TextView>(R.id.titleTextView).text = "Select an item"
        }

        // Кнопка назад (только для портретной ориентации)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.findViewById<Button>(R.id.backButton).setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        } else {
            view.findViewById<Button>(R.id.backButton).visibility = View.GONE
        }
    }
}