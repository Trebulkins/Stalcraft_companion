package com.example.stalcraft_companion.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.ApiClient
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.TranslationString
import com.example.stalcraft_companion.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var infoBlocksAdapter: InfoBlocksAdapter
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

        infoBlocksAdapter = InfoBlocksAdapter()
        binding.itemInfoBlocks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = infoBlocksAdapter
        }

        currentItem?.let { bindItemDetails(it) }

        binding.leaveLayout?.setOnClickListener {
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
            when (item.color) {
                "DEFAULT" -> {
                    itemTitle.setTextColor(resources.getColor(R.color.DEFAULT))
                    itemRarityColor.setBackgroundColor(resources.getColor(R.color.DEFAULT))
                }
                "RANK_NEWBIE" -> {
                    itemTitle.setTextColor(resources.getColor(R.color.RANK_NEWBIE))
                    itemRarityColor.setBackgroundColor(resources.getColor(R.color.RANK_NEWBIE))
                }
                "RANK_STALKER" -> {
                    itemTitle.setTextColor(resources.getColor(R.color.RANK_STALKER))
                    itemRarityColor.setBackgroundColor(resources.getColor(R.color.RANK_STALKER))
                }
                "RANK_VETERAN" -> {
                    itemTitle.setTextColor(resources.getColor(R.color.RANK_VETERAN))
                    itemRarityColor.setBackgroundColor(resources.getColor(R.color.RANK_VETERAN))
                }
                "RANK_MASTER" -> {
                    itemTitle.setTextColor(resources.getColor(R.color.RANK_MASTER))
                    itemRarityColor.setBackgroundColor(resources.getColor(R.color.RANK_MASTER))
                }
                "RANK_LEGEND" -> {
                    itemTitle.setTextColor(resources.getColor(R.color.RANK_LEGEND))
                    itemRarityColor.setBackgroundColor(resources.getColor(R.color.RANK_LEGEND))
                }
            }
            itemState.text = item.status.state
            Picasso.get().load(ApiClient.DATABASE_BASE_URL + item.iconPath).into(itemIcon)
            item.infoBlocks?.let { infoBlocksAdapter.submitList(it) }
        }
    }
}