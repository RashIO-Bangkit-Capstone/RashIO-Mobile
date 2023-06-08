package id.rashio.android.api

import id.rashio.android.model.LoginRequest
import id.rashio.android.model.LoginResponse
import id.rashio.android.model.RegisterRequest
import id.rashio.android.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("authentications")
    fun login(@Body loginRequest: LoginRequest,
              @Header("Content-Type") contentType: String = "application/json"
    ): Call<LoginResponse>

    @POST("users")
    fun register(@Body registerRequst: RegisterRequest,
                 @Header("Content-Type") contentType: String = "application/json"
    ): Call<RegisterResponse>
}