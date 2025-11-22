package com.example.myapplication.Projects.Instagram.Pages

import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.Projects.Instagram.RoodDB.Comment.Comment
import com.example.myapplication.Projects.Instagram.RoodDB.Post.Post
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserScreen(email: String?, currentUserEmail: String?, onBackClick: () -> Unit, navController: NavController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val postDao = db.postDao()
    val commentDao = db.commentDao()

    var targetUser by remember { mutableStateOf<User?>(null) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var isFollowing by remember { mutableStateOf(false) }
    var targetUserPosts by remember { mutableStateOf(listOf<Post>()) }
    var postComments by remember { mutableStateOf(listOf<Comment>()) }

    LaunchedEffect(email) {
        targetUser = userDao.getUserByEmail(email!!)
        currentUser = userDao.getUserByEmail(currentUserEmail!!)
        isFollowing = currentUser?.following?.contains(targetUser?.id) ?: false
        targetUserPosts = postDao.getPostsByUserId(targetUser!!.id)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 25.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(
                            modifier = Modifier.size(22.dp).clickable {
                                onBackClick()
                            },
                            painter = painterResource(R.drawable.left_arrow),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = targetUser?.username ?: "Username",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.size(26.dp),
                            painter = painterResource(R.drawable.notification),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.padding(13.dp))
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.more_dot),
                            contentDescription = null,
                        )
                    }
                    Spacer(modifier = Modifier.padding(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            modifier = Modifier.size(90.dp).clip(CircleShape),
                            painter = rememberAsyncImagePainter(
                                targetUser?.avatar ?: R.drawable.avatar_icon
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.padding(start = 15.dp, top = 26.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(90.dp),
                            ) {
                                Text(
                                    text = targetUserPosts.size.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = (targetUser?.followers?.size ?: 0).toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = (targetUser?.following?.size ?: 0).toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(23.dp)
                            ) {
                                Text(
                                    text = "Posts",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Followers",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Following",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = targetUser?.userBio ?: "bio",
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.padding(7.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            modifier = Modifier.size(160.dp, 40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing) Color(0xE9E3E3E3) else Color(
                                    0xFF5102EA
                                )
                            ),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                if (currentUser != null && targetUser != null) {
                                    if (isFollowing) {
                                        currentUser = currentUser?.copy(
                                            following = currentUser?.following?.filter { it != targetUser?.id }
                                        )
                                        targetUser = targetUser?.copy(
                                            followers = targetUser?.followers?.filter { it != currentUser?.id }
                                        )
                                    } else {
                                        currentUser = currentUser?.copy(
                                            following = currentUser?.following?.plus(targetUser!!.id)
                                        )
                                        targetUser = targetUser?.copy(
                                            followers = targetUser?.followers?.plus(currentUser!!.id)
                                        )
                                    }
                                    isFollowing = !isFollowing
                                    CoroutineScope(Dispatchers.IO).launch {
                                        userDao.updateUser(targetUser!!)
                                        userDao.updateUser(currentUser!!)
                                    }
                                }
                            }
                        ) {
                            if (isFollowing) {
                                Text(
                                    text = "Following",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF050505)
                                )
                            } else {
                                Text(
                                    text = "Follow",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFFFFF)
                                )
                            }
                        }
                        Button(
                            modifier = Modifier.size(160.dp, 40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xE9E3E3E3)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {}
                        ) {
                            Text(
                                text = "Message",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000000),
                                modifier = Modifier.clickable{
                                    val currentUserId = currentUser?.id.toString()
                                    val targetUserId = targetUser?.id.toString()

                                    navController.navigate("chat/${targetUserId}/${currentUserId}")
                                }
                            )
                        }
                        Box(
                            modifier = Modifier.size(40.dp)
                                .background(Color(0xFFE3E3E3), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(R.drawable.invite),
                                contentDescription = null,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(15.dp))
                    Divider()
                }
            }

            if (targetUserPosts.isNotEmpty()) {
                items(targetUserPosts.size) { index ->
                    val post = targetUserPosts[index]

                    LaunchedEffect(post.id){
                        postComments = commentDao.getCommentsByPostId(post.id)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                painter = rememberAsyncImagePainter(
                                    targetUser?.avatar ?: R.drawable.avatar_icon
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = targetUser?.username ?: "Unknown user",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier
                                    .size(42.dp)
                                    .padding(end = 20.dp),
                                painter = painterResource(R.drawable.more_dot),
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.padding(5.dp))

                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 300.dp),
                            painter = rememberAsyncImagePainter(post.imageUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(
                                    modifier = Modifier.size(27.dp),
                                    painter = painterResource(
                                        if (post.likes?.contains(targetUser?.id) == true){
                                            R.drawable.favorite_red
                                        }else{
                                            R.drawable.favorite
                                        }
                                    ),
                                    contentDescription = null,
                                    tint = Color.Unspecified
                                )
                                Text(text = post.likes?.size.toString(), fontSize = 18.sp)
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    painter = painterResource(R.drawable.comment),
                                    contentDescription = null
                                )
                                Text(text = postComments.size.toString(), fontSize = 18.sp)
                                Icon(
                                    modifier = Modifier.size(27.dp),
                                    painter = painterResource(R.drawable.send),
                                    contentDescription = null
                                )
                            }
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(R.drawable.save),
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.padding(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = targetUser?.username ?: "Unknown user",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = post.caption,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            modifier = Modifier.padding(start = 15.dp),
                            text = SimpleDateFormat(
                                "MMMM dd, yyyy",
                                Locale.getDefault()
                            ).format(Date(post.createdAt)),
                            fontSize = 15.sp,
                            color = Color(0xFF9A9A9A)
                        )
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 380.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(50.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(70.dp),
                                painter = painterResource(R.drawable.camera),
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "No posts yet",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }

    }
}

@Composable
@Preview(showBackground = true, device = "id:pixel_8")
fun ShowUser(){
    UserScreen(email = "email", onBackClick = {}, currentUserEmail = "email", navController = rememberNavController())
}
