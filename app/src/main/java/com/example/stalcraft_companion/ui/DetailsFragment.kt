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
import androidx.lifecycle.ViewModelProvider
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.TranslationString
import com.example.stalcraft_companion.databinding.FragmentDetailsBinding
import com.example.stalcraft_companion.databinding.ItemInfoblockTextBinding

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ItemViewModel::class.java)

        arguments?.getString("item_id")?.let { itemId ->
            viewModel.getItemById(itemId).observe(viewLifecycleOwner) { item ->
                item?.let { bindItemDetails(it) }
            }
        }

        binding.leaveLayout.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun bindItemDetails(item: Item) {
        with(binding) {
            itemTitle.text = when (val name = item.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            itemCategory.text = item.category
            itemRarityColor.setBackgroundColor(Color.parseColor(item.color))
            itemState.text = item.status.state

            itemInfoBlocks.removeAllViews()
            item.infoBlocks?.forEach { block ->
                when (block) {
                    is InfoBlock.TextBlock -> addTextBlock(block)
                    is InfoBlock.DamageBlock -> TODO()
                    is InfoBlock.RangeBlock -> TODO()
                    is InfoBlock.KeyValueBlock -> addKeyValueBlock(block)
                    is InfoBlock.ListBlock -> TODO()
                    is InfoBlock.NumericBlock -> TODO()
                }
            }
        }
    }

    private fun addKeyValueBlock(block: InfoBlock.KeyValueBlock) {
        val view = ItemInfoblockTextBinding.inflate(layoutInflater).apply {
            key.text = block.key.toString()
            value.text = when (val text = block.value) {
                is TranslationString.Text -> text.text
                is TranslationString.Translation -> text.lines.ru
            }
        }
        binding.itemInfoBlocks.addView(view.root)
    }

    private fun addTextBlock(block: InfoBlock.TextBlock) {
        val view = ItemInfoblockTextBinding.inflate(layoutInflater).apply {
            key.text = block.title.toString()
            value.text = when (val text = block.text) {
                is TranslationString.Text -> text.text
                is TranslationString.Translation -> text.lines.ru
            }
        }
        binding.itemInfoBlocks.addView(view.root)
    }

    companion object {
        fun newInstance(itemId: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString("item_id", itemId)
            }
        }
    }
}