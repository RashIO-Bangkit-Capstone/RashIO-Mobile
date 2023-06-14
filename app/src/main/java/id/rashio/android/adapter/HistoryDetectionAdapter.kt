package id.rashio.android.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.rashio.android.databinding.ItemRiwayatDeteksiBinding
import id.rashio.android.model.PredictionLogs
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetectionAdapter(
    private val onClick: (String, Float) -> Unit
) : ListAdapter<PredictionLogs, HistoryDetectionAdapter.HistoryDetectionViewHolder>(
    HistoryArticleDiffCallback
) {
    class HistoryDetectionViewHolder(private val binding: ItemRiwayatDeteksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: PredictionLogs, onClick: (String, Float) -> Unit) {
            binding.apply {
                itemHistoryContainer.setOnClickListener {
                    onClick(item.result, item.percentage)
                }

                tvItemTitle.text = item.result
                val inputFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = inputFormat.parse(item.createdAt)
                val formattedDate = date?.let { outputFormat.format(it) }
                tvDate.text = formattedDate
                val percentage = ((item.percentage ?: 0F).times(100)).toInt()
                binding.tvPercentage.text = ("$percentage%")
                Glide.with(root.context)
                    .load(item.imageUrl)
                    .into(imgPoster)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetectionViewHolder {
        val binding =
            ItemRiwayatDeteksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryDetectionViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(
        holder: HistoryDetectionViewHolder,
        position: Int
    ) {
        val historyDetectionData = getItem(position)
        holder.bind(historyDetectionData, onClick)
    }

}

object HistoryArticleDiffCallback : DiffUtil.ItemCallback<PredictionLogs>() {
    override fun areItemsTheSame(
        oldItem: PredictionLogs,
        newItem: PredictionLogs
    ): Boolean {
        return oldItem.createdAt == newItem.createdAt
    }

    override fun areContentsTheSame(
        oldItem: PredictionLogs,
        newItem: PredictionLogs
    ): Boolean {
        return oldItem == newItem
    }

}