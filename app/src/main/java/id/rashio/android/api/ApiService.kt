package id.rashio.android.api

import id.rashio.android.model.LoginRequest
import id.rashio.android.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("authentications")
    fun login(@Body loginRequest: LoginRequest,
              @Header("Content-Type") contentType: String = "application/json"
    ): Call<LoginResponse>
}