package id.rashio.android.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.rashio.android.api.ApiConfig
import id.rashio.android.api.ApiService
import id.rashio.android.data.repository.ArticleRepository
import id.rashio.android.data.repository.DefaultArticleRepository

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModules {
    @Provides
    fun provideApiService() : ApiService = ApiConfig.getApiService()

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("RashIO", 0)

    @Provides
    fun provideArticleRepository(api: ApiService, sharedPreferences: SharedPreferences) : ArticleRepository =
        DefaultArticleRepository(api, sharedPreferences)
}