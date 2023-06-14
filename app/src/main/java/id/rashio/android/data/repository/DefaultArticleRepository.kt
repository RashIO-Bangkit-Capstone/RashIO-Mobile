package id.rashio.android.data.repository

import id.rashio.android.api.ApiService
import id.rashio.android.data.local.entity.BookmarkArticleEntity
import id.rashio.android.data.local.room.BookmarkArticleDao
import id.rashio.android.model.Article
import id.rashio.android.model.DetailArticle
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject

class DefaultArticleRepository @Inject constructor(
    private val api: ApiService,
    private val dao: BookmarkArticleDao
) : ArticleRepository {


    override fun getAllArticle(): Call<Article> {
        return api.getAllArticles()
    }

    override fun getArticleDetail(id: Int): Call<DetailArticle> {
        return api.getDetailArticle(id)
    }

    override suspend fun getArticleIsBookmarked(articleId: Int): Boolean {
        return dao.exists(articleId)
    }

    override suspend fun articleBookmarked(
        articleId: Int,
        title: String,
        imageUrl: String,
        author: String
    ) {
        dao.insert(
            BookmarkArticleEntity(
                articleId = articleId,
                title = title,
                imageUrl = imageUrl,
                author = author,
                isBookmarked = true
            )
        )
    }

    override suspend fun removeBookmarkedArticle(articleId: Int) {
        dao.delete(articleId)
    }

    override fun getAllBookmarkedArticle(): Flow<List<BookmarkArticleEntity>> =
        dao.getAllArticleBookmark()
}