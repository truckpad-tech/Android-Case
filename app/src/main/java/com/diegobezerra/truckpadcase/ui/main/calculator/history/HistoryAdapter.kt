package com.diegobezerra.truckpadcase.ui.main.calculator.history

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diegobezerra.truckpadcase.R
import com.diegobezerra.truckpadcase.databinding.ItemHistoryBinding
import com.diegobezerra.truckpadcase.databinding.ItemHistoryRecentsBinding
import com.diegobezerra.truckpadcase.domain.model.HistoryEntry
import com.diegobezerra.truckpadcase.ui.main.MainViewModel
import com.diegobezerra.truckpadcase.ui.main.calculator.history.HistoryViewHolder.*

class HistoryAdapter(
    private val mainViewModel: MainViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<Any, HistoryViewHolder>(HistoryDiff) {

    var history: List<HistoryEntry> = emptyList()
        set(value) {
            field = value
            submitList(buildList(value))
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_history_empty -> HistoryEmptyViewHolder(
                inflater.inflate(R.layout.item_history_empty, parent, false)
            )
            R.layout.item_history_recents -> HistoryRecentsViewHolder(
                ItemHistoryRecentsBinding.inflate(inflater, parent, false).apply {
                    viewModel = mainViewModel
                }.root
            )
            R.layout.item_history -> HistoryEntryViewHolder(
                ItemHistoryBinding.inflate(inflater, parent, false),
                mainViewModel,
                lifecycleOwner
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        when (holder) {
            is HistoryEntryViewHolder -> holder.apply {
                bind(getItem(position) as HistoryEntry)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoryEmpty -> R.layout.item_history_empty
            is HistoryRecents -> R.layout.item_history_recents
            else -> R.layout.item_history
        }
    }

    private fun buildList(history: List<HistoryEntry>): List<Any> {
        val result = mutableListOf<Any>()
        if (history.isNotEmpty()) {
            result += HistoryRecents
            result.addAll(history)
        } else {
            result += HistoryEmpty
        }
        return result
    }
}

object HistoryEmpty

object HistoryRecents

sealed class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class HistoryEmptyViewHolder(itemView: View) : HistoryViewHolder(itemView)

    class HistoryRecentsViewHolder(itemView: View) : HistoryViewHolder(itemView)

    class HistoryEntryViewHolder(
        private val binding: ItemHistoryBinding,
        private val mainViewModel: MainViewModel,
        private val lifecycleOwner: LifecycleOwner
    ) : HistoryViewHolder(binding.root) {

        fun bind(historyEntry: HistoryEntry) {
            with(binding.root.resources) {
                binding.origin.text =
                    makeBoldLabel(getString(R.string.history_origin, historyEntry.origin.displayName))
                binding.destination.text =
                    makeBoldLabel(getString(R.string.history_destination, historyEntry.destination.displayName))
            }
            binding.historyEntry = historyEntry
            binding.viewModel = mainViewModel
            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }

        private fun makeBoldLabel(str: String): CharSequence {
            val span = StyleSpan(Typeface.BOLD)
            return SpannableString(str).apply {
                setSpan(span, 0, str.indexOfFirst { it == ':' }, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

}

object HistoryDiff : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean {
        return when {
            oldItem is HistoryEmpty && newItem is HistoryEmpty -> true
            oldItem is HistoryRecents && newItem is HistoryRecents -> true
            oldItem is HistoryEntry && newItem is HistoryEntry -> oldItem.id == newItem.id
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is HistoryEntry && newItem is HistoryEntry -> oldItem == newItem
            else -> true
        }
    }
}