package id.rashio.android.data.repository

import id.rashio.android.model.Article
import id.rashio.android.model.DetailArticle
import retrofit2.Call

interface ArticleRepository {

    fun getAllArticle(): Call<Article>

    fun getArticleDetail(id: Int): Call<DetailArticle>

    suspend fun getArticleIsBookmarked(articleId: Int): Boolean

    suspend fun articleBookmarked(articleId: Int)

    suspend fun removeBookmarkedArticle(articleId: Int)

}