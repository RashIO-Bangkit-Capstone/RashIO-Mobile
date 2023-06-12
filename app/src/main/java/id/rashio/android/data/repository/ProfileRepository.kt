package id.rashio.android.data.repository

import id.rashio.android.model.DetailArticle
import retrofit2.Call

interface ProfileRepository {

    fun getArticleDetail(id: Int): Call<DetailArticle>

}