package com.example.artbook.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.artbook.api.RetrofitAPI
import com.example.artbook.model.ImageResponse
import com.example.artbook.repo.ArtRepository
import com.example.artbook.repo.ArtRepositoryInterface
import com.example.artbook.roomdb.ArtDao
import com.example.artbook.roomdb.ArtDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Response
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class] // Ensure it replaces the production module
)
object TestAppModule {

    @Singleton
    @Provides
    @Named("testDataBase")
    fun injectInMemoryRoom(@ApplicationContext context : Context) =
        Room.inMemoryDatabaseBuilder(context, ArtDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideDao(@Named("testDataBase") database: ArtDatabase): ArtDao {
        return database.artDao()
    }

    @Singleton
    @Provides
    fun provideTestGlide(@ApplicationContext context: Context): RequestManager {
        return Glide.with(context)
    }

    @Singleton
    @Provides
    fun provideArtRepository(dao: ArtDao, api: RetrofitAPI): ArtRepositoryInterface {
        return ArtRepository(dao, api) // Explicitly returning an instance
    }

    @Singleton
    @Provides
    fun provideFakeRetrofitAPI(): RetrofitAPI {
        return object : RetrofitAPI {
            override suspend fun imageSearch(searchQuery: String, apiKey: String): Response<ImageResponse> {
                return Response.success(ImageResponse(listOf(), 0, 0)) // Return a fake response
            }
        }
    }
}

    /*fun injectInMemoryRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ArtDatabase::class.java,
        "ArtBookDB",
    ).build()*/
