package id.rashio.android.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.rashio.android.data.local.entity.BookmarkArticleEntity

@Database(entities = [BookmarkArticleEntity::class], version = 1)
abstract class BookmarkArticleDatabase : RoomDatabase() {
    abstract fun bookmarkArticleDao(): BookmarkArticleDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarkArticleDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): BookmarkArticleDatabase {
            if (INSTANCE == null) {
                synchronized(BookmarkArticleDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BookmarkArticleDatabase::class.java, "bookmark_article_database"
                    )
                        .build()
                }
            }
            return INSTANCE as BookmarkArticleDatabase
        }
    }
}