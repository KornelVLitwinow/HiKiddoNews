package com.litwinow.hikiddonews.repository

import com.litwinow.hikiddonews.dao.PostDao
import com.litwinow.hikiddonews.model.Post
import com.litwinow.hikiddonews.api.PostApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val postApi: PostApi,
    private val postDao: PostDao
) {
    suspend fun getListPosts() = postApi.getListNewsApi()

    suspend fun insertAllPosts(posts: List<Post>?) {
        if (posts != null)
            postDao.insertAllPosts(posts)
    }

    suspend fun updatePost(post: Post) {
        postDao.updatePost(post)
    }

    suspend fun addPost(post: Post): Long = postDao.addPost(post)

    fun getAllPostsDatabase() = postDao.getPosts()

    fun getPostDatabaseById(id: Int) = postDao.getPostById(id)
}