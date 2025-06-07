package com.example.stalcraft_companion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.TranslationString

class InfoBlocksAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<InfoBlock>()

    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_DAMAGE = 1
        private const val TYPE_LIST = 2
        private const val TYPE_NUMERIC= 3
        private const val TYPE_KEY_VALUE = 4
        private const val TYPE_RANGE = 5
        private const val TYPE_USAGE= 6
        private const val TYPE_ITEM = 7
    }

    fun submitList(newItems: List<InfoBlock>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEXT -> TextBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            TYPE_DAMAGE -> DamageBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_damage, parent, false)
            )
            TYPE_RANGE -> RangeBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            TYPE_LIST -> ListBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            TYPE_KEY_VALUE -> KeyValueBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            TYPE_NUMERIC -> NumericBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            TYPE_USAGE -> UsageBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            TYPE_ITEM -> ItemBlockViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_infoblock_text, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TextBlockViewHolder -> holder.bind(items[position] as InfoBlock.TextBlock)
            is DamageBlockViewHolder -> holder.bind(items[position] as InfoBlock.DamageBlock)
            is ListBlockViewHolder -> holder.bind(items[position] as InfoBlock.ListBlock)
            is NumericBlockViewHolder -> holder.bind(items[position] as InfoBlock.NumericBlock)
            is KeyValueBlockViewHolder -> holder.bind(items[position] as InfoBlock.KeyValueBlock)
            is RangeBlockViewHolder -> holder.bind(items[position] as InfoBlock.RangeBlock)
            is UsageBlockViewHolder -> holder.bind(items[position] as InfoBlock.UsageBlock)
            is ItemBlockViewHolder -> holder.bind(items[position] as InfoBlock.ItemBlock)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is InfoBlock.TextBlock -> TYPE_TEXT
            is InfoBlock.DamageBlock -> TYPE_DAMAGE
            is InfoBlock.RangeBlock -> TYPE_RANGE
            is InfoBlock.ItemBlock -> TYPE_ITEM
            is InfoBlock.KeyValueBlock -> TYPE_KEY_VALUE
            is InfoBlock.ListBlock -> TYPE_LIST
            is InfoBlock.NumericBlock -> TYPE_NUMERIC
            is InfoBlock.UsageBlock -> TYPE_USAGE
        }
    }

    inner class TextBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.TextBlock) {
            itemView.findViewById<TextView>(R.id.title).text = when (val text = block.title) {
                is TranslationString.Text -> text.text
                is TranslationString.Translation -> text.lines.ru
                null -> null
            }
            itemView.findViewById<TextView>(R.id.text).text = when (val text = block.text) {
                is TranslationString.Text -> text.text
                is TranslationString.Translation -> text.lines.ru
            }
        }
    }

    inner class DamageBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.DamageBlock) {
            itemView.findViewById<TextView>(R.id.damageVal1).text = "startDamage: ${block.startDamage}"
            itemView.findViewById<TextView>(R.id.damageVal2).text = "damageDecreaseStart: ${block.damageDecreaseStart}"
            itemView.findViewById<TextView>(R.id.damageVal3).text = "endDamage: ${block.endDamage}"
            itemView.findViewById<TextView>(R.id.damageVal4).text = "damageDecreaseEnd: ${block.damageDecreaseEnd}"
            itemView.findViewById<TextView>(R.id.damageVal5).text = "maxDistance: ${block.maxDistance}"
        }
    }

    inner class RangeBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.RangeBlock) {
            itemView.findViewById<TextView>(R.id.title).text = when (val name = block.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            itemView.findViewById<TextView>(R.id.text).text = "[${block.min}, ${block.max}]"
        }
    }

    inner class ItemBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.ItemBlock) {
            itemView.findViewById<TextView>(R.id.text).text = when (val name = block.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
        }
    }

    inner class NumericBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.NumericBlock) {
            itemView.findViewById<TextView>(R.id.title).text = when (val name = block.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            itemView.findViewById<TextView>(R.id.text).text = block.formatted.value.ru
        }
    }

    inner class ListBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.ListBlock) {
            itemView.findViewById<TextView>(R.id.title).text = when (val name = block.title) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            itemView.findViewById<TextView>(R.id.text).text = "[Список]"
        }
    }

    inner class KeyValueBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.KeyValueBlock) {
            itemView.findViewById<TextView>(R.id.title).text = when (val name = block.key) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            itemView.findViewById<TextView>(R.id.text).text = when (val name = block.value) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
        }
    }

    inner class UsageBlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(block: InfoBlock.UsageBlock) {
            itemView.findViewById<TextView>(R.id.title).text = when (val name = block.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
        }
    }
}