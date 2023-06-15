package id.rashio.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.rashio.android.R
import id.rashio.android.databinding.ItemArticleBinding
import id.rashio.android.model.DataArticle

class ArticleListAdapter(
    private val onClick: (Int) -> Unit,
    private val onBookmark: (Int, String, String, String) -> Unit
) :
    ListAdapter<DataArticle, ArticleListAdapter.ArticleViewHolder>(ArticleDiffCallback) {

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: DataArticle,
            onClick: (Int) -> Unit,
            onBookmark: (Int, String, String, String) -> Unit
        ) {
            binding.apply {
                itemArticleContainer.setOnClickListener {
                    onClick(item.id)
                }

                if (item.isBookmarked) {
                    ivBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivBookmark.context,
                            R.drawable.ic_bookmark_full
                        )
                    )
                } else {
                    ivBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivBookmark.context,
                            R.drawable.ic_bookmark_empty
                        )
                    )
                }

                ivBookmark.setOnClickListener {
                    onBookmark(item.id, item.title, item.imageUrl, item.author)
                    if (item.isBookmarked) {
                        ivBookmark.setImageDrawable(
                            ContextCompat.getDrawable(
                                ivBookmark.context,
                                R.drawable.ic_bookmark_empty
                            )
                        )
                    } else {
                        ivBookmark.setImageDrawable(
                            ContextCompat.getDrawable(
                                ivBookmark.context,
                                R.drawable.ic_bookmark_full
                            )
                        )
                    }
                }
                tvItemTitle.text = item.title
                tvAuthor.text = item.author
                Glide.with(root.context)
                    .load(item.imageUrl)
                    .into(imgPoster)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val articleData = getItem(position)
        holder.bind(articleData, onClick, onBookmark)
    }
}

object ArticleDiffCallback : DiffUtil.ItemCallback<DataArticle>() {
    override fun areItemsTheSame(oldItem: DataArticle, newItem: DataArticle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataArticle, newItem: DataArticle): Boolean {
        return oldItem == newItem
    }

}