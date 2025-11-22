package com.example.myapplication.Projects.Instagram.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
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
import com.example.myapplication.Projects.Instagram.chat.ChatViewModel
import com.example.myapplication.R

data class Person(
    val avatar: Painter,
    val name: String
)

@Composable
fun ChatsScreen(id: Int?, viewModel: ChatViewModel = hiltViewModel(), navController: NavController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    var currentUser by remember { mutableStateOf<User?>(null) }
    val chatList by viewModel.chatList.collectAsState()

    LaunchedEffect(id) {
        currentUser = userDao.getUserById(id!!)
        viewModel.loadChatList(id.toString())
    }


    val allUsers = listOf(
        Person(painterResource(R.drawable.flag_narxoz), "narxozKz"),
        Person(painterResource(R.drawable.barcelona), "barcelona"),
        Person(painterResource(R.drawable.kaspi_kz), "kaspi.kz"),
        Person(painterResource(R.drawable.shavkat), "shavkat"),
        Person(painterResource(R.drawable.netflix), "netflix"),
        Person(painterResource(R.drawable.zuck), "zuck")
    )

    Scaffold() {
        Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(it)) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.left_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp).clickable{
                            navController.popBackStack()
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentUser?.username ?: "Username",
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 18.dp)
                    ) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(currentUser?.avatar ?: R.drawable.avatar_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Gray, CircleShape),
                                contentScale = ContentScale.Crop
                            )

                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your note",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                items(allUsers) { user ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 15.dp)
                    ) {
                        Image(
                            painter = user.avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color(0xFFFF003B), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = user.name,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x403A6DFF)
                    )
                ) {
                    Text(
                        text = "Messages",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3A6DFF),
                    )
                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFAE1E1E1)
                    )
                ) {
                    Text(
                        text = "Channels",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFAE1E1E1)
                    )
                ) {
                    Text(
                        text = "Requests",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }

            if (chatList.isNotEmpty()){
                LazyColumn {
                    items(chatList){ userId ->
                        var targetUser by remember { mutableStateOf<User?>(null) }
                        LaunchedEffect(userId) {
                            targetUser = userDao.getUserById(userId.toInt())
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp).clickable{
                                navController.navigate("Chat/${targetUser?.id.toString()}/${currentUser?.id.toString()}")
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(targetUser?.avatar ?: R.drawable.avatar_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = targetUser?.username ?: "Username",
                                    fontSize = 19.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                painter = painterResource(R.drawable.camera),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(
                        text = "No messages",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}



