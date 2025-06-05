package com.example.stalcraft_companion.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.TranslationString

class DetailsFragment : Fragment() {
    companion object {
        private const val ARG_ITEM = "item"
        fun newInstance(item: Item?) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ITEM, item)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getParcelable<Item>(ARG_ITEM)
        item?.let { bindItemDetails(view, it) }

        view.findViewById<ImageView>(R.id.leave_layout).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun bindItemDetails(view: View, item: Item) {
        view.findViewById<TextView>(R.id.item_title).text = when (val name = item.name) {
            is TranslationString.Text -> name.text
            is TranslationString.Translation -> name.lines.ru
        }

        view.findViewById<TextView>(R.id.item_category).text = item.category
        view.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor(item.color))
        view.findViewById<TextView>(R.id.item_state).text = item.status.state

        // Отображение infoBlocks
        val infoContainer = view.findViewById<LinearLayout>(R.id.item_infoBlocks)
        infoContainer.removeAllViews()

        item.infoBlocks?.forEach { block ->
            when (block) {
                is InfoBlock.TextBlock -> addTextBlock(infoContainer, block)
                is InfoBlock.DamageBlock -> TODO()
                is InfoBlock.KeyValueBlock -> TODO()
                is InfoBlock.ListBlock -> TODO()
                is InfoBlock.NumericBlock -> TODO()
                is InfoBlock.RangeBlock -> TODO()
            }
        }
    }

    private fun addTextBlock(container: LinearLayout, block: InfoBlock.TextBlock) {
        // Создание и добавление блока
    }
}