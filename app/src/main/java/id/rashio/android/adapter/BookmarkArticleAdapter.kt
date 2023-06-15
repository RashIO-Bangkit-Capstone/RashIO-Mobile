package id.rashio.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.rashio.android.R
import id.rashio.android.data.local.entity.BookmarkArticleEntity
import id.rashio.android.databinding.ItemArticleBinding

class BookmarkArticleAdapter(
    private val onClick: (Int) -> Unit,
    private val onBookmark: (Int, String, String, String) -> Unit
) :
    ListAdapter<BookmarkArticleEntity, BookmarkArticleAdapter.BookmarkArticleViewHolder>(
        BookmarkArticleDiffCallback
    ) {

    class BookmarkArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: BookmarkArticleEntity,
            onClick: (Int) -> Unit,
            onBookmark: (Int, String, String, String) -> Unit
        ) {
            binding.apply {
                itemArticleContainer.setOnClickListener {
                    onClick(item.articleId)
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
                    onBookmark(item.articleId, item.title, item.imageUrl, item.author)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkArticleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BookmarkArticleViewHolder,
        position: Int
    ) {
        val bookmarkArticleData = getItem(position)
        holder.bind(bookmarkArticleData, onClick, onBookmark)
    }
}

object BookmarkArticleDiffCallback : DiffUtil.ItemCallback<BookmarkArticleEntity>() {
    override fun areItemsTheSame(
        oldItem: BookmarkArticleEntity,
        newItem: BookmarkArticleEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BookmarkArticleEntity,
        newItem: BookmarkArticleEntity
    ): Boolean {
        return oldItem == newItem
    }

}