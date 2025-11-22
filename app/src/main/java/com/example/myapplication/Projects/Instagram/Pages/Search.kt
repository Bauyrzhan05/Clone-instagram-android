package com.example.myapplication.Projects.Instagram.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.Projects.Instagram.RoodDB.Post.Post
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.R

@Composable
fun SearchScreen(onUserClick: (String) -> Unit, email: String?) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val postDao = db.postDao()

    var searchText by remember { mutableStateOf("") }
    var searchResults by remember {mutableStateOf(listOf<User>())}
    var allPosts by remember { mutableStateOf(listOf<Post>()) }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()){
            searchResults = userDao.getAllUsers().filter { it.username.contains(searchText) }
        }else "No results found"

        allPosts = postDao.getAllPosts()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 25.dp).background(Color.White)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {searchText = it},
            placeholder = {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Search",
                        fontSize = 20.sp
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0x9FDCDCDC),
                unfocusedIndicatorColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color(0xFFFFFFFF),
                focusedContainerColor = Color(0x9FDCDCDC)
            ),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))

        if (searchText.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            ) {
                items(searchResults.size){
                    val user = searchResults[it]
                    if (email != user.email){
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable{
                                onUserClick(user.email)
                            }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(user.avatar?: R.drawable.avatar_icon),
                                contentDescription = null,
                                modifier = Modifier.size(70.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = user.username,
                                fontSize = 20.sp,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        }else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(allPosts){ post ->
                    Image(
                        painter = rememberAsyncImagePainter(post.imageUrl),
                        contentDescription = null,
                        modifier = Modifier.height(150.dp).fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, device = "id:pixel_8")
fun ShowSearchScreen(){
    SearchScreen(onUserClick = {}, email = "email")
}