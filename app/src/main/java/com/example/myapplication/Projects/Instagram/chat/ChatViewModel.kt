package com.example.myapplication.Projects.Instagram.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val db: DatabaseReference = Firebase.database.getReference("chats")
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private val _uiState = MutableStateFlow(UiState())
    private val _chatList = MutableStateFlow<List<String>>(emptyList())

    val chatList = _chatList.asStateFlow()
    val messages = _messages.asStateFlow()
    val uiState = _uiState.asStateFlow()

    private var chatId: String = ""
    private var messagesListener: ValueEventListener? = null

    fun startChat(currentUserId: String, targetUserId: String){
        chatId = getChatId(currentUserId, targetUserId)
        ensureParticipants(currentUserId, targetUserId)
        listenToMessages()
    }

    fun sendMessage(text: String, senderId: String, receiverId: String){
        if (text.isEmpty() || chatId.isEmpty()) return

        viewModelScope.launch {
            val message = Message(
                senderId = senderId,
                receiveId = receiverId,
                text = text,
                createdAt = System.currentTimeMillis()
            )

            db.child(chatId)
                .child("messages")
                .push()
                .setValue(message)
                .addOnFailureListener { e ->
                    _uiState.value = UiState(error = "Failed to send message: ${e.message}")
                }
        }
    }

    fun listenToMessages(){
        removeListener()

        val messagesRef = db.child(chatId).child("messages")

        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Message>()
                for (child in snapshot.children){
                    val msg = child.getValue(Message::class.java)
                    msg?.let {
                        list.add(it.copy(id = child.key ?: ""))
                    }
                }
                _messages.value = list.sortedBy { it.createdAt }
                _uiState.value = UiState(isLoading = false)
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = UiState(error = "Failed to load messages: ${error.message}")
            }
        }

        messagesRef.addValueEventListener(messagesListener!!)
        _uiState.value = UiState(isLoading = true)

    }

    fun deleteMessage(messageId: String){
        db.child(chatId)
            .child("messages")
            .child(messageId)
            .removeValue()
    }

    fun removeListener(){
        messagesListener?.let {
            db.child("messages").removeEventListener(it)
        }
        messagesListener = null
    }

    override fun onCleared() {
        super.onCleared()
        removeListener()
    }

    fun ensureParticipants(userId1: String, userId2: String) {
        db.child(chatId).child("participants").get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                db.child(chatId).child("participants").setValue(listOf(userId1, userId2))
            }
        }
    }

    private fun getChatId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "chat_${userId1}_$userId2"
        } else {
            "chat_${userId2}_$userId1"
        }
    }




    fun loadChatList(currentUserId: String) {
        Firebase.database.getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<String>()

                    for (chatSnapshot in snapshot.children) {
                        val participants = chatSnapshot.child("participants")
                            .children
                            .mapNotNull { it.getValue(String::class.java) }

                        if (participants.contains(currentUserId)) {
                            val other = participants.first { it != currentUserId }
                            list.add(other)
                        }
                    }

                    _chatList.value = list
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}