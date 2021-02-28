package com.litwinow.hikiddonews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.litwinow.hikiddonews.dao.PostDao
import com.litwinow.hikiddonews.model.Post

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: PostsDatabase? = null

        fun getDatabase(context: Context): PostsDatabase {
            return (instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext, PostsDatabase::class.java,
                    "posts_database"
                ).build()
                instance = newInstance
                newInstance
            })
        }
    }
}