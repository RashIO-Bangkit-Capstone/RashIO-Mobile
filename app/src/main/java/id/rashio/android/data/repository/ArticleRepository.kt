package id.rashio.android.data.repository

import id.rashio.android.data.local.entity.BookmarkArticleEntity
import id.rashio.android.model.Article
import id.rashio.android.model.DetailArticle
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface ArticleRepository {

    fun getAllArticle(): Call<Article>

    fun getArticleDetail(id: Int): Call<DetailArticle>

    suspend fun getArticleIsBookmarked(articleId: Int): Boolean

    suspend fun articleBookmarked(articleId: Int, title: String, imageUrl: String, author: String)

    suspend fun removeBookmarkedArticle(articleId: Int)

    fun getAllBookmarkedArticle(): Flow<List<BookmarkArticleEntity>>

}