package com.example.stalcraft_companion.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.stalcraft_companion.ItemViewModel
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.api.ApiClient
import com.example.stalcraft_companion.api.schemas.Item
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    companion object {
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: Int?): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_ID, itemId ?: -1)
                }
            }
        }
    }

    private val viewModel: ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = arguments?.getInt(ARG_ITEM_ID, -1) ?: -1

        if (itemId != -1) {
            loadItemDetails(itemId)
        } else {
            showPlaceholder()
        }

        setupBackButton(view)
    }

    private fun loadItemDetails(itemId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getItemById(itemId)?.let { item ->
                bindItem(item)
            } ?: showError()
        }
    }

    private fun bindItem(item: Item) {
        view?.apply {
            findViewById<TextView>(R.id.item_title).text = item.name?.lines?.ru
            findViewById<TextView>(R.id.item_category).text = item.category

            when (item.color) {
                "DEFAULT" -> {
                    findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#808080"))
                    findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#808080"))
                }
                "RANK_NEWBIE" -> {
                    findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#9ACD32"))
                    findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#9ACD32"))
                }
                "RANK_STALKER" -> {
                    findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#1E90FF"))
                    findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#1E90FF"))
                }
                "RANK_VETERAN" -> {
                    findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#BA55D3"))
                    findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#BA55D3"))
                }
                "RANK_MASTER" -> {
                    findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#B22222"))
                    findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#B22222"))
                }
                "RANK_LEGEND" -> {
                    findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#FFD700"))
                    findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#FFD700"))
                }
            }

            when (item.status?.state) {
                "PERSONAL_ON_USE" -> {
                    findViewById<ImageView>(R.id.item_state_img).setImageResource(R.drawable.personal_on_use)
                    findViewById<TextView>(R.id.item_state).setText(R.string.PERSONAL_ON_USE)
                }
                "NON_DROP" -> {
                    findViewById<ImageView>(R.id.item_state_img).setImageResource(R.drawable.non_drop)
                    findViewById<TextView>(R.id.item_state).setText(R.string.NON_DROP)
                }
                "PERSONAL_DROP_ON_GET" -> {
                    findViewById<ImageView>(R.id.item_state_img).setImageResource(R.drawable.personal_drop_on_get)
                    findViewById<TextView>(R.id.item_state).setText(R.string.PERSONAL_DROP_ON_GET)
                }
            }

            Picasso.get().load("${ApiClient.BASE_URL}icons/${item.category}/${item.id}").into(findViewById<ImageView>(R.id.item_icon))
        }
    }

    private fun showPlaceholder() {
        view?.findViewById<ConstraintLayout>(R.id.TemplateLayout)?.visibility = View.VISIBLE
        view?.findViewById<ConstraintLayout>(R.id.ItemLayout)?.visibility = View.INVISIBLE
        view?.findViewById<TextView>(R.id.templateText)?.text = "Выберите предмет из списка"
    }

    private fun showError() {
        view?.findViewById<ConstraintLayout>(R.id.TemplateLayout)?.visibility = View.VISIBLE
        view?.findViewById<ConstraintLayout>(R.id.ItemLayout)?.visibility = View.INVISIBLE
        view?.findViewById<TextView>(R.id.templateText)?.text = "Ошибка загрузки предмета!"
    }

    private fun setupBackButton(view: View) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.findViewById<ImageView>(R.id.leave_layout).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.leave_layout).setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        } else {
            view.findViewById<ImageView>(R.id.leave_layout).visibility = View.INVISIBLE
        }
    }
}