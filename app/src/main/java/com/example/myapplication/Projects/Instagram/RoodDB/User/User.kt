package com.example.myapplication.Projects.Instagram.RoodDB.User

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username:String,
    val userBio: String,
    val email: String,
    val password: String,
    val avatar: String? = null,
    val followers: List<Int>? =  emptyList(),
    val following: List<Int>? = emptyList(),
)