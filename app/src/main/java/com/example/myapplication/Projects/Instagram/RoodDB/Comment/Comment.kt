package com.example.myapplication.Projects.Instagram.RoodDB.Comment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val postId: Int,
    val text: String
)
