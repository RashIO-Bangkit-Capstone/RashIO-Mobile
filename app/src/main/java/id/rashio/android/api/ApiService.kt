package id.rashio.android.api

import id.rashio.android.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("authentications")
    fun login(@Body loginRequest: LoginRequest,
              @Header("Content-Type") contentType: String = "application/json"
    ): Call<LoginResponse>

    @POST("users")
    fun register(@Body registerRequst: RegisterRequest,
                 @Header("Content-Type") contentType: String = "application/json"
    ): Call<RegisterResponse>

    @GET("articles")
    fun getAllArticles(@Header("Content-Type") contentType: String = "application/json"
    ) : Call<Article>

    @GET("articles/{id}")
    fun getDetailArticle(
        @Path("id") id: Int,
        @Header("Content-Type") contentType: String = "application/json"
    ) : Call <DetailArticle>

}