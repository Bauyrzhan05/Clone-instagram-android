package com.example.myapplication.Projects.Instagram.RoodDB.Post

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Insert
    suspend fun addPost(post: Post)

    @Query("SELECT * FROM posts")
    suspend fun getAllPosts(): List<Post>

    @Query("SELECT * FROM posts WHERE userId = :userId")
    suspend fun getPostsByUserId(userId: Int): List<Post>

    @Delete
    suspend fun deletePost(post: Post)

    @Update
    suspend fun updatePost(post: Post)
}


