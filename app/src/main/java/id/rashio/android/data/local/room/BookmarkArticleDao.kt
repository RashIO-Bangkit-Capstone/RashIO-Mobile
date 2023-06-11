package id.rashio.android.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import id.rashio.android.data.local.entity.BookmarkArticleEntity

@Dao
interface BookmarkArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmarkArticleEntity: BookmarkArticleEntity)

    @Delete
    fun delete(bookmarkArticleEntity: BookmarkArticleEntity)

    @Query("SELECT * from bookmark_article ORDER BY id DSC")
    fun getAllArticleBookmark(): LiveData<List<BookmarkArticleEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM bookmark_article WHERE id = :id)")
    fun exists(id: Int)
}