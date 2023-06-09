package id.rashio.android.api

import id.rashio.android.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        const val BASE_URL = "https://www.rashio.software/beta/"

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val contentLengthRemovingInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .removeHeader("Content-Length")
                .build()
            chain.proceed(modifiedRequest)
        }

        fun getApiService(): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(contentLengthRemovingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}