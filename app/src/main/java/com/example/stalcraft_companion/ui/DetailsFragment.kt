package com.example.stalcraft_companion.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var currentItem: Item? = null

    companion object {
        private const val ARG_ITEM = "item"

        fun newInstance(item: Item?): DetailsFragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                currentItem = it.getParcelable(ARG_ITEM)
            }
        }
    }

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

        currentItem?.let { bindItemDetails(it) }

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
            itemCategory.text = CategoryUtils.getMainCategory(item.category)
            itemRarityColor.setBackgroundColor(0x000)
            itemState.text = item.status.state

            itemInfoBlocks.removeAllViews()

            item.infoBlocks?.forEach { block ->
                when (block) {
                    is InfoBlock.TextBlock -> addTextBlock(block)
                    is InfoBlock.DamageBlock -> addDamageBlock(block)
                    is InfoBlock.RangeBlock -> addRangeBlock(block)
                    is InfoBlock.KeyValueBlock -> addKeyValueBlock(block)
                    is InfoBlock.ListBlock -> addListBlock(block)
                    is InfoBlock.NumericBlock -> addNumericBlock(block)
                    is InfoBlock.UsageBlock -> addUsageBlock(block)
                    is InfoBlock.ItemBlock -> addItemBlock(block)
                }
            }
        }
    }

    private fun addItemBlock(block: InfoBlock.ItemBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_text, null)
        view.findViewById<TextView>(R.id.text).text = when (val text = block.name) {
            is TranslationString.Text -> text.text
            is TranslationString.Translation -> text.lines.ru
        }
        binding.itemInfoBlocks.addView(view)
    }

    private fun addNumericBlock(block: InfoBlock.NumericBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_text, null)
        view.findViewById<TextView>(R.id.text).text = block.formatted.toString()
        binding.itemInfoBlocks.addView(view)
    }

    private fun addUsageBlock(block: InfoBlock.UsageBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_text, null)
        view.findViewById<TextView>(R.id.text).text = when (val text = block.name) {
            is TranslationString.Text -> text.text
            is TranslationString.Translation -> text.lines.ru
        }
        binding.itemInfoBlocks.addView(view)
    }

    private fun addListBlock(block: InfoBlock.ListBlock) {
        TODO("Not yet implemented")
    }

    private fun addTextBlock(block: InfoBlock.TextBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_text, null)
        view.findViewById<TextView>(R.id.title).text = when (val text = block.title) {
            is TranslationString.Text -> text.text
            is TranslationString.Translation -> text.lines.ru
            null -> ""
        }
        view.findViewById<TextView>(R.id.text).text = when (val text = block.text) {
            is TranslationString.Text -> text.text
            is TranslationString.Translation -> text.lines.ru
        }
        binding.itemInfoBlocks.addView(view)
    }

    private fun addDamageBlock(block: InfoBlock.DamageBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_damage, null)
        view.findViewById<TextView>(R.id.damageVal1).text = block.startDamage.toString()
        view.findViewById<TextView>(R.id.damageVal2).text = block.damageDecreaseStart.toString()
        view.findViewById<TextView>(R.id.damageVal3).text = block.endDamage.toString()
        view.findViewById<TextView>(R.id.damageVal4).text = block.damageDecreaseEnd.toString()
        view.findViewById<TextView>(R.id.damageVal5).text = block.maxDistance.toString()
        binding.itemInfoBlocks.addView(view)
    }

    private fun addRangeBlock(block: InfoBlock.RangeBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_text, null)
        view.findViewById<TextView>(R.id.title).text = when (val name = block.name) {
            is TranslationString.Text -> name.text
            is TranslationString.Translation -> name.lines.ru
        }
        view.findViewById<TextView>(R.id.text).text = "[${block.min}, ${block.max}]"
        binding.itemInfoBlocks.addView(view)
    }

    private fun addKeyValueBlock(block: InfoBlock.KeyValueBlock) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infoblock_text, null)
        view.findViewById<TextView>(R.id.title).text = when (val name = block.key) {
            is TranslationString.Text -> name.text
            is TranslationString.Translation -> name.lines.ru
        }
        view.findViewById<TextView>(R.id.text).text = when (val name = block.value) {
            is TranslationString.Text -> name.text
            is TranslationString.Translation -> name.lines.ru
        }
        binding.itemInfoBlocks.addView(view)
    }
}