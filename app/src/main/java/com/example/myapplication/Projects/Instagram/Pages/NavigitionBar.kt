package com.example.myapplication.Projects.Instagram.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.R

data class NavItem(
    val label: String,
    val image: Painter
)

@Composable
fun MainScreen(username: String?, email: String?, startIndex: Int = 0, navController: NavHostController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    var currentUser by remember { mutableStateOf<User?>(null) }
    var selectedUserEmail by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(email) {
        currentUser = userDao.getUserByEmail(email!!)
    }

    var selectedIndex by remember { mutableIntStateOf(startIndex) }

    val navItemList = listOf(
        NavItem("Home", painterResource(R.drawable.home)),
        NavItem("Search", painterResource(R.drawable.search)),
        NavItem("Story", painterResource(R.drawable.story)),
        NavItem("Video", painterResource(R.drawable.video)),
        NavItem("Profile", rememberAsyncImagePainter(currentUser?.avatar?: R.drawable.avatar_icon)
        ),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(120.dp),
                containerColor = Color(0xFFFFFFFF),
            ){
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            if (navItem.label == "Profile"){
                                Image(
                                    painter = navItem.image,
                                    contentDescription = null,
                                    modifier = Modifier.clip(CircleShape).size(26.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }else{
                                Icon(
                                    modifier = Modifier.size(23.dp),
                                    painter = navItem.image,
                                    contentDescription = null,
                                    tint = Color(0xFF181818)
                                )
                            }
                        },
                        label = {
                            navItem.label
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ConnectScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            username = username,
            email = email,
            navController = navController,
            selectedUserEmail = selectedUserEmail,
            onUserClick = {selectedUserEmail = it},
            onBackClick = {selectedUserEmail = null}
        )
    }
}

@Composable
fun ConnectScreen(
    modifier: Modifier,
    selectedIndex: Int,
    username: String?,
    email: String?,
    navController: NavHostController,
    selectedUserEmail: String?,
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit
){
    when(selectedIndex){
        0 -> HomeScreen(navController = navController, username = username, email = email)
        1 -> if (selectedUserEmail != null){
                UserScreen(email = selectedUserEmail, currentUserEmail = email, onBackClick = onBackClick, navController = navController)
            }else{
                SearchScreen(onUserClick = onUserClick, email = email)
            }
        2 -> PostScreen(email = email, navController = navController)
        4 -> ProfileScreen(email = email, navController = navController)
    }
}

@Composable
@Preview(showBackground = true, device = "id:pixel_8")
fun ShowMainScreen(){
    MainScreen(username = "username", email = "email", navController = rememberNavController())
}
