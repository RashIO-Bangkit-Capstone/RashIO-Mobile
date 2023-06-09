package id.rashio.android.data.repository

import android.content.SharedPreferences
import id.rashio.android.api.ApiService
import id.rashio.android.model.Article
import id.rashio.android.model.DetailArticle
import retrofit2.Call
import javax.inject.Inject

class DefaultArticleRepository @Inject constructor(
    private val api: ApiService,
) : ArticleRepository {


    override fun getAllArticle(): Call<Article> {
        return api.getAllArticles()
    }

    override fun getArticleDetail(id: Int): Call<DetailArticle> {
        return api.getDetailArticle(id)
    }

}