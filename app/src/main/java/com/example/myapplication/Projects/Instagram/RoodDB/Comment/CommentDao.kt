package com.example.myapplication.Projects.Instagram.RoodDB.Comment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommentDao {

    @Insert
    suspend fun addComment(comment: Comment)

    @Query("SELECT * FROM comments WHERE postId = :postId")
    suspend fun getCommentsByPostId(postId: Int): List<Comment>

    @Query("DELETE FROM comments WHERE id = :commentId AND userId = :userId")
    suspend fun deleteComment(commentId: Int, userId: Int)

}