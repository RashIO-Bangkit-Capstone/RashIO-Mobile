package id.rashio.android.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "bookmark_article")
@Parcelize
data class BookmarkArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val articleId: Int,
    val title: String,
    val imageUrl: String,
    val author: String,
    val isBookmarked: Boolean
) : Parcelable