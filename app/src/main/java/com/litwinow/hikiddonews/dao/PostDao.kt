package com.litwinow.hikiddonews.dao

import androidx.room.*
import com.litwinow.hikiddonews.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPostById(id: Int): Flow<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(posts: List<Post>)

    @Update
    suspend fun updatePost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(post: Post): Long
}