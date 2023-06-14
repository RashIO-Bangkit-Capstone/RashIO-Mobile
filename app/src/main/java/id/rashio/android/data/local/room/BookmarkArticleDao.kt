package id.rashio.android.data.local.room

import androidx.room.*
import id.rashio.android.data.local.entity.BookmarkArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmarkArticleEntity: BookmarkArticleEntity)

    @Query("DELETE FROM bookmark_article WHERE articleId = :articleId")
    suspend fun delete(articleId: Int)

    @Query("SELECT * from bookmark_article ORDER BY id DESC")
    fun getAllArticleBookmark(): Flow<List<BookmarkArticleEntity>>

    @Query("SELECT EXISTS (SELECT * FROM bookmark_article WHERE articleId = :articleId)")
    suspend fun exists(articleId: Int): Boolean
}