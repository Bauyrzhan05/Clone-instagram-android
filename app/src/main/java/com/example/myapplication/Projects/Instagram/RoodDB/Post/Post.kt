package com.example.myapplication.Projects.Instagram.RoodDB.Post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val caption: String,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis(),
    val likes: List<Int>? = emptyList()
)
