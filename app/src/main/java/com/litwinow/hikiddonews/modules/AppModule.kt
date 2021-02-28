package com.litwinow.hikiddonews.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.litwinow.hikiddonews.dao.PostDao
import com.litwinow.hikiddonews.database.PostsDatabase
import com.litwinow.hikiddonews.repository.PostRepository
import com.litwinow.hikiddonews.api.PostApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(loggingInterceptor)
        return Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideNewsApi(retrofit: Retrofit): PostApi = retrofit.create(PostApi::class.java)

    @Singleton
    @Provides
    fun provideRepository(postApi: PostApi, postDao: PostDao) = PostRepository(postApi, postDao)

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): PostsDatabase {
        return PostsDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideNewsDao(postsDatabase: PostsDatabase): PostDao {
        return postsDatabase.postDao()
    }
}