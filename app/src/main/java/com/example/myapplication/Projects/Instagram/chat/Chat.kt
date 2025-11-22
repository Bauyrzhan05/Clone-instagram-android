package com.example.myapplication.Projects.Instagram.chat

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(id: String?, currentUserId: String?, viewModel: ChatViewModel = hiltViewModel(), navController: NavController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    var messageText by remember { mutableStateOf("") }
    var targetUser by remember { mutableStateOf<User?>(null) }
    var deleteMessageDialog by remember { mutableStateOf<String?>(null) }

    val messages by viewModel.messages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(id, currentUserId) {
        if (id == null || currentUserId == null) return@LaunchedEffect

        targetUser = userDao.getUserById(id.toInt())
        viewModel.startChat(currentUserId, id)
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(it)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.left_arrow),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = rememberAsyncImagePainter(targetUser?.avatar ?: R.drawable.avatar_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(0.5.dp, Color.LightGray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = targetUser?.username ?: "username",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Active now",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.video2),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.mobile),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Divider(thickness = 0.5.dp, color = Color(0xFFE0E0E0))
//            LOADING/ERROR
            if (uiState.isLoading){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
            uiState.error?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(16.dp))
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9F9))
                    .padding(horizontal = 8.dp),
                reverseLayout = false,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(targetUser?.avatar ?: R.drawable.avatar_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = targetUser?.username ?: "username", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                items(messages){ message ->
                    val isMe = message.senderId == currentUserId.toString()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        if (!isMe) {
                            Image(
                                painter = rememberAsyncImagePainter(targetUser?.avatar),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(35.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isMe) Color(0xFF3797F0) else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(
                                        topStart = if (isMe) 20.dp else 0.dp,
                                        topEnd = if (isMe) 0.dp else 20.dp,
                                        bottomStart = 20.dp,
                                        bottomEnd = 20.dp
                                    )
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .widthIn(max = 280.dp).combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        if (isMe) deleteMessageDialog = message.id
                                    },
                                )
                        ) {
                            Text(
                                text = message.text,
                                color = if (isMe) Color.White else Color.Black,
                                fontSize = 15.sp
                            )
                        }
                    }

                    if (deleteMessageDialog == message.id){
                        AlertDialog(
                            onDismissRequest = { deleteMessageDialog = null },
                            title = { Text(text = "Delete message?") },
                            confirmButton = {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = {
                                            viewModel.deleteMessage(message.id)
                                            deleteMessageDialog = null
                                        }
                                    ) {
                                        Text(
                                            text = "Delete",
                                            fontSize = 18.sp,
                                            color = Color.Red,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    TextButton(
                                        onClick = { deleteMessageDialog = null }
                                    ) {
                                        Text(
                                            text = "Cancel",
                                            fontSize = 18.sp,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        )
                    }

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(10.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(0xFFF0F0F0))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ){
                    BasicTextField(
                        value = messageText,
                        onValueChange = {messageText = it},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        decorationBox = {innerTextField ->
                            if (messageText.isEmpty()){
                                Text(
                                    text = "Message...",
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                if (messageText.isEmpty()){
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.audio),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                        Icon(
                            painter = painterResource(R.drawable.image),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                        Icon(
                            painter = painterResource(R.drawable.sticker),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }else{
                    IconButton(
                        onClick = {
                            viewModel.sendMessage(messageText, currentUserId.toString(), id.toString())
                            messageText = ""
                        },
                        modifier = Modifier.size(40.dp).background(Color(0xFF3797F0), CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_up),
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
