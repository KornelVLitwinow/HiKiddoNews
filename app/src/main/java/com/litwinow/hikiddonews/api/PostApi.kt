package com.litwinow.hikiddonews.api

import com.litwinow.hikiddonews.model.Posts
import retrofit2.Response
import retrofit2.http.GET

interface PostApi {
    @GET("v3/6125f2d0-0688-4547-aae8-0295d984f196")
    suspend fun getListNewsApi(): Response<Posts>
}