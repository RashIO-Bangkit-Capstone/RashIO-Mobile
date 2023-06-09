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
import id.rashio.android.data.local.room.BookmarkArticleDatabase
import id.rashio.android.data.repository.*

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModules {
    @Provides
    fun provideApiService(): ApiService = ApiConfig.getApiService()

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("RashIO", 0)

    @Provides
    fun provideArticleRepository(
        api: ApiService,
        database: BookmarkArticleDatabase
    ): ArticleRepository =
        DefaultArticleRepository(api, database.bookmarkArticleDao())

    @Provides
    fun provideDetectionRepository(
        api: ApiService,
        sharedPreferences: SharedPreferences
    ): DetectionRepository =
        DefaultDetectionRepository(api, sharedPreferences)

    @Provides
    fun provideDiseaseRepository(
        api: ApiService
    ): DiseaseRepository = DefaultDiseaseRepository(api)

    @Provides
    fun provideDatabaseBookmark(@ApplicationContext context: Context): BookmarkArticleDatabase =
        BookmarkArticleDatabase.getDatabase(context)

    @Provides
    fun provideHistoryRepository(
        api: ApiService,
        sharedPreferences: SharedPreferences
    ): HistoryDetectionRepository = DefaultHistoryDetectionRepository(api, sharedPreferences)
}