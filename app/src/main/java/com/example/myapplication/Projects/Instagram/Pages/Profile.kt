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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.Projects.Instagram.RoodDB.Comment.Comment
import com.example.myapplication.Projects.Instagram.RoodDB.Post.Post
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, email: String?) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val postDao = db.postDao()
    val commentDao = db.commentDao()

    var currentUser by remember { mutableStateOf<User?>(null) }
    var userPosts by remember { mutableStateOf(listOf<Post>()) }
    var showBottomSheetLogout by remember { mutableStateOf(false) }
    var showBottomSheetDeletePost by remember { mutableStateOf(false) }
    var openDialogDeletePost by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }
    var showDeletedSuccessDialog by remember { mutableStateOf(false) }
    var postComments by remember { mutableStateOf(listOf<Comment>()) }

    LaunchedEffect(email) {
        currentUser = userDao.getUserByEmail(email!!)
        userPosts = postDao.getPostsByUserId(currentUser!!.id).reversed()
    }

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 25.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentUser?.username ?: "Unknown user",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Icon(
                            modifier = Modifier.size(13.dp),
                            painter = painterResource(R.drawable.down_arrow),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.story),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.padding(13.dp))
                        Icon(
                            modifier = Modifier
                                .size(25.dp)
                                .clickable { showBottomSheetLogout = true },
                            painter = painterResource(R.drawable.menu),
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.padding(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            painter = rememberAsyncImagePainter(
                                currentUser?.avatar ?: R.drawable.avatar_icon
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.padding(start = 15.dp, top = 26.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(90.dp),
                            ) {
                                Text(
                                    text = userPosts.size.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = (currentUser?.followers?.size ?: 0).toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = (currentUser?.following?.size ?: 0).toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(23.dp)
                            ) {
                                Text("Posts", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Followers", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Following", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = currentUser?.userBio ?: "", fontSize = 18.sp)
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "@${currentUser?.username}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xE9E3E3E3)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            onClick = { navController.navigate("EditProfile/${email}") }
                        ) {
                            Text(
                                text = "Edit Profile",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
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
                    Spacer(modifier = Modifier.padding(10.dp))
                    Divider()
                }
            }

            if (userPosts.isNotEmpty()) {
                items(userPosts.size) { index ->
                    val post = userPosts[index]

                    LaunchedEffect(post.id) {
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
                                    currentUser?.avatar ?: R.drawable.avatar_icon
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = currentUser?.username ?: "Unknown user",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(R.drawable.more_dot),
                                contentDescription = null,
                                modifier = Modifier.size(42.dp).padding(end = 20.dp)
                                    .clickable{
                                        selectedPost = post
                                        showBottomSheetDeletePost = true
                                    }
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
                                        if (post.likes?.contains(currentUser?.id) == true)
                                            R.drawable.favorite_red
                                            else R.drawable.favorite,
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
                                Text(postComments.size.toString(), fontSize = 18.sp)
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
                                text = currentUser?.username ?: "Unknown user",
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

        if (showBottomSheetLogout) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = { showBottomSheetLogout = false },
                containerColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(120.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.log_out),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Log out",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF0000),
                        modifier = Modifier.clickable {
                            navController.navigate("Login")
                        }
                    )
                }
            }
        }

        if (showBottomSheetDeletePost) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = { showBottomSheetDeletePost = false },
                containerColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(120.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trash),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                    Text(
                        text = "Delete",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF0000),
                        modifier = Modifier.clickable {
                            showBottomSheetDeletePost = false
                            openDialogDeletePost = true
                        }
                    )
                }
            }
        }

        if (openDialogDeletePost) {
            AlertDialog(
                onDismissRequest = { openDialogDeletePost = false },
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                title = {
                    Text(
                        text = "Delete post?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 80.dp)
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "You can restore this post from Recently deleted in your activity within 30 days. After that it will be permanently deleted.",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Any content using your original audio will be muted. If you restore your post, this content will be unmuted.",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                },
                confirmButton = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Divider()
                        TextButton(
                            onClick = {
                                selectedPost?.let { post ->
                                    CoroutineScope(Dispatchers.IO).launch {
                                        postDao.deletePost(post)
                                    }
                                    userPosts = userPosts.filter { it.id != post.id }
                                }
                                openDialogDeletePost = false

                                showDeletedSuccessDialog = true

                                scope.launch {
                                    delay(2300L)
                                    showDeletedSuccessDialog = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Delete",
                                fontSize = 18.sp,
                                color = Color(0xFFFF0000),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Divider()
                        TextButton(
                            onClick = { openDialogDeletePost = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                dismissButton = {}
            )
        }

        if (showDeletedSuccessDialog) {
            Dialog(onDismissRequest = { showDeletedSuccessDialog = false }) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.success_check)
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = 1,
                        modifier = Modifier.size(130.dp)
                    )
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true, device = "id:pixel_8")
fun ShowProfileScreen(){
    ProfileScreen(navController = rememberNavController(), email = "email")
}