package com.example.myapplication.Projects.Instagram.Pages

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun EditProfileScreen(email: String?, navController: NavController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    var currentUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(email){
        currentUser = userDao.getUserByEmail(email!!)
    }

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            email = it.email
            username = it.username
            userBio = it.userBio
            avatar = it.avatar
        }
    }
//  ========================Gallery=================================

    fun saveImageToInternalStorage(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "user_avatar_${System.currentTimeMillis()}.png")

        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file.absolutePath
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null){
            val path = saveImageToInternalStorage(context, uri)
            avatar = path
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(21.dp).clickable{
                        navController.popBackStack()
                    },
                    painter = painterResource(R.drawable.left_arrow),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Edit Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Done",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0091EA),
                    modifier = Modifier.clickable{
                        currentUser?.let {
                            val updatedUser = it.copy(
                                username = username,
                                userBio = userBio,
                                email = email,
                                avatar = avatar
                            )
                            if (username != "" && userBio != "" && email != ""){
                                CoroutineScope(Dispatchers.IO).launch {
                                    userDao.updateUser(updatedUser)

                                    withContext(Dispatchers.Main){
                                        navController.navigate("MainScreenProfile/${email}"){
                                            popUpTo("MainScreenProfile/${email}"){
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
        Divider()
        Column(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(120.dp).clip(CircleShape).padding(top = 10.dp),
                painter =
                    if (avatar != null)
                        rememberAsyncImagePainter(avatar)
                    else
                        painterResource(R.drawable.avatar_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Change Profile Photo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0091EA),
                modifier = Modifier.clickable{
                    galleryLauncher.launch("image/*")
                }
            )
        }
        Divider()
        Column(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Email",
                    fontSize = 21.sp,
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {email = it},
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        unfocusedIndicatorColor = Color(0xFFC4C4C4),
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 21.sp,
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Name",
                    fontSize = 21.sp,
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = {username = it},
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        unfocusedIndicatorColor = Color(0xFFC4C4C4),
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 21.sp,
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp, end = 20.dp),
                    text = "Bio",
                    fontSize = 21.sp,
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userBio,
                    onValueChange = {userBio = it},
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        unfocusedIndicatorColor = Color(0xFFC4C4C4),
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 21.sp,
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, device = "id:pixel_7")
fun Show(){
    EditProfileScreen(email = "email", navController = rememberNavController())
}
