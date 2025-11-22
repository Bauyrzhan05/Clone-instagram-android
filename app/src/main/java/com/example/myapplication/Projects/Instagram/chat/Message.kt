package com.example.myapplication.Projects.Instagram.chat

data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiveId: String = "",
    val text: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
