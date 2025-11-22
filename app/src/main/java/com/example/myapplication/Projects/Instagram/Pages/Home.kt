package com.example.myapplication.Projects.Instagram.Pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class User(
    val avatar: Painter,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, username: String?, email: String?){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val postDao = db.postDao()
    val commentDao = db.commentDao()

    var currentUser by remember { mutableStateOf<User?>(null) }
    var allPosts by remember { mutableStateOf(listOf<Post>()) }
    val likedPosts = remember { mutableStateMapOf<Int, Boolean>() }
    var openBottomSheetCommentPostId by remember { mutableStateOf<Int?>(null) }
    var commentText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDeleteCommentDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteCommentSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(email) {
        currentUser = userDao.getUserByEmail(email!!)
        val posts = postDao.getAllPosts()
        allPosts = posts.filter { it.userId != currentUser!!.id }.reversed()
    }

    val scope = rememberCoroutineScope()

    val allUsers = listOf(
        User(painterResource(R.drawable.flag_narxoz), "narxozKz"),
        User(painterResource(R.drawable.barcelona), "barcelona"),
        User(painterResource(R.drawable.kaspi_kz), "kaspi.kz"),
        User(painterResource(R.drawable.shavkat), "shavkat94"),
        User(painterResource(R.drawable.netflix), "netflix"),
        User(painterResource(R.drawable.zuck), "zuck")
    )

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
                        .fillMaxSize()
                        .padding(top = 30.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            modifier = Modifier.size(180.dp, 60.dp),
                            painter = painterResource(R.drawable.insta_icon),
                            contentDescription = null
                        )
                        Row(modifier = Modifier.padding(top = 23.dp)) {
                            Icon(
                                modifier = Modifier.size(13.dp),
                                painter = painterResource(R.drawable.down_arrow),
                                contentDescription = null
                            )
                        }
                        Row(
                            modifier = Modifier.padding(top = 16.dp, start = 115.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                modifier = Modifier.size(27.dp),
                                painter = painterResource(R.drawable.favorite),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(30.dp))
                            Icon(
                                painter = painterResource(R.drawable.chat),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp).clickable{
                                    navController.navigate("chats/${currentUser?.id}")
                                }
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                    ) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = 12.dp)
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
                                    Icon(
                                        painter = painterResource(R.drawable.add_plus),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(30.dp)
                                            .background(Color.White, CircleShape)
                                            .padding(2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Your story",
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
                    Divider()
                }
            }

            if (allPosts.isNotEmpty() ) {
                items(allPosts.size) { index ->
                    val post = allPosts[index]
                    var targetUser by remember { mutableStateOf<User?>(null) }
                    var postComments by remember { mutableStateOf(listOf<Comment>()) }

                    LaunchedEffect(post.userId) {
                        targetUser = userDao.getUserById(post.userId)
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
                            if (currentUser?.following?.contains(targetUser?.id) == false){
                                Button(onClick = {},
                                    modifier = Modifier
                                        .height(40.dp)
                                        .padding(horizontal = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xE9F1F1F1)
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                ) {
                                    Text(
                                        text = "Follow",
                                        color = Color.Black,
                                        fontSize = 17.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                            Icon(
                                modifier = Modifier
                                    .size(40.dp)
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
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Icon(
                                        painter = painterResource(
                                            if (post.likes?.contains(currentUser?.id) == true)
                                                R.drawable.favorite_red
                                            else R.drawable.favorite),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(27.dp)
                                            .clickable {
                                                if (currentUser != null) {
                                                    val isLiked = likedPosts[post.id] == true

                                                    val updatedPost = if (isLiked) {
                                                        post.copy(
                                                            likes = post.likes?.minus(
                                                                currentUser!!.id
                                                            ) ?: emptyList()
                                                        )
                                                    } else {
                                                        post.copy(
                                                            likes = post.likes?.plus(
                                                                currentUser!!.id
                                                            ) ?: listOf(currentUser!!.id)
                                                        )
                                                    }

                                                    likedPosts[post.id] = !isLiked
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        postDao.updatePost(updatedPost)
                                                    }

                                                    allPosts = allPosts.map {
                                                        if (it.id == post.id) updatedPost else it
                                                    }
                                                }
                                            }
                                    )
                                    Text(
                                        text = post.likes?.size.toString(),
                                        fontSize = 18.sp
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.comment),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .clickable {
                                                openBottomSheetCommentPostId = post.id
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    val comments = commentDao.getCommentsByPostId(post.id)
                                                    withContext(Dispatchers.Main) {
                                                        postComments = comments
                                                    }
                                                }
                                            }
                                    )
                                    Text(
                                        text = postComments.size.toString(),
                                        fontSize = 18.sp
                                    )
                                }
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
                    if (openBottomSheetCommentPostId == post.id) {
                        ModalBottomSheet(
                            sheetState = sheetState,
                            onDismissRequest = {openBottomSheetCommentPostId = null},
                            containerColor = Color.White
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.85f)
                                    .background(Color.White)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Comments",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.send),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(26.dp)
                                            .align(Alignment.CenterEnd)
                                    )
                                }
                                Divider(thickness = 0.8.dp, color = Color(0xFFE0E0E0))

                                if (postComments.isNotEmpty()){
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(horizontal = 20.dp)
                                    ) {
                                        items(postComments) { comment ->
                                            var targetCommentUser by remember { mutableStateOf<User?>(null) }

                                            LaunchedEffect(comment.userId) {
                                                targetCommentUser = userDao.getUserById(comment.userId)
                                            }

                                            Row(modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .combinedClickable(
                                                    onClick = {},
                                                    onLongClick = {
                                                        if (currentUser?.id == targetCommentUser?.id)
                                                            showDeleteCommentDialog = comment.id
                                                    }
                                                )
                                            ) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(targetCommentUser?.avatar ?: R.drawable.avatar_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        text = targetCommentUser?.username ?: "Unknown user",
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                    Text(
                                                        text = comment.text,
                                                        fontSize = 16.sp
                                                    )
                                                }
                                            }
                                            Divider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)

                                            if(showDeleteCommentDialog == comment.id){
                                                AlertDialog(
                                                    onDismissRequest =  { showDeleteCommentDialog = null},
                                                    title = { Text("Delete comment") },
                                                    text = { Text("Are you sure you want to delete this comment?") },
                                                    confirmButton = {
                                                        Row {
                                                            TextButton(
                                                                onClick = {
                                                                    CoroutineScope(Dispatchers.IO).launch {
                                                                        commentDao.deleteComment(comment.id, currentUser!!.id)
                                                                        val updatedComments = commentDao.getCommentsByPostId(post.id)
                                                                        withContext(Dispatchers.Main) {
                                                                            postComments = updatedComments
                                                                        }
                                                                    }
                                                                    showDeleteCommentDialog = null

                                                                    showDeleteCommentSuccessDialog = true
                                                                    scope.launch {
                                                                        delay(2300)
                                                                        showDeleteCommentSuccessDialog = false
                                                                    }
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = "Delete",
                                                                    color = Color.Red
                                                                )
                                                            }
                                                        }
                                                    },
                                                    dismissButton = {
                                                        TextButton(
                                                            onClick = {showDeleteCommentDialog = null}
                                                        ) {
                                                            Text("Cancel")
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                                else{
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "No comments yet",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Start the conversation.",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp, 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    val emotions = listOf("❤️", "🙌", "🔥", "👏", "😭", "😍", "😮", "😂")
                                    emotions.forEach {
                                        Text(
                                            text = it,
                                            fontSize = 24.sp
                                        )
                                    }
                                }
                                Divider(thickness = 0.8.dp, color = Color(0xFFE0E0E0))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(currentUser?.avatar ?: R.drawable.avatar_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(50))
                                            .background(Color(0xFFF2F2F2))
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                    ) {
                                        BasicTextField(
                                            value = commentText,
                                            onValueChange = { commentText = it },
                                            textStyle = TextStyle(fontSize = 14.sp),
                                            modifier = Modifier.fillMaxWidth(),
                                            decorationBox = { innerTextField ->
                                                if (commentText.isEmpty()) {
                                                    Text(
                                                        text = "add a comment...",
                                                        color = Color.Gray,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))

                                    if (commentText.isNotEmpty()){
                                        IconButton(
                                            onClick = {
                                                val comment = Comment(
                                                    userId = currentUser!!.id,
                                                    postId = post.id,
                                                    text = commentText
                                                )

                                                CoroutineScope(Dispatchers.IO).launch {
                                                    commentDao.addComment(comment)
                                                    val updatedComments = commentDao.getCommentsByPostId(post.id)
                                                    withContext(Dispatchers.Main) {
                                                        postComments = updatedComments
                                                    }
                                                }

                                                commentText = ""
                                            },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(Color.Blue, RoundedCornerShape(100))
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.arrow_up),
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }else {
                                        Icon(
                                            painter = painterResource(R.drawable.gif),
                                            contentDescription = null,
                                            modifier = Modifier.size(38.dp)
                                        )
                                    }
                                }
                            }
                        }
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
                        Text(
                            text = "No posts",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
    if (showDeleteCommentSuccessDialog){
        Dialog(onDismissRequest = { showDeleteCommentSuccessDialog = false }) {
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


@Composable
@Preview(showBackground = true, device = "id:pixel_8")
fun ShowHomeScreen(){
    HomeScreen(navController = rememberNavController(), username = "username", email = "email")

}