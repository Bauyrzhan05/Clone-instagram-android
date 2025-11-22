package com.example.myapplication.Projects.Instagram.Authorization

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myapplication.Projects.Instagram.RoodDB.AppDatabase
import com.example.myapplication.R
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    var email by remember { mutableStateOf("")}
    var userBio by remember {mutableStateOf("")}
    var userName by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    Column(
        modifier =Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(contentAlignment = Alignment.Center) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.instagram_logo)
            )
            LottieAnimation(
                composition = composition,
                iterations = 1,
                modifier = Modifier.size(230.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            placeholder = { Text(text = "Email")},
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD0D0D0),
                unfocusedBorderColor = Color(0xFFFFFFFF),
            )
        )
        OutlinedTextField(
            value = userBio,
            onValueChange = {userBio = it},
            placeholder = { Text(text = "Bio")},
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD0D0D0),
                unfocusedBorderColor = Color(0xFFFFFFFF),
            )
        )
        OutlinedTextField(
            value = userName,
            onValueChange = {userName = it},
            placeholder = { Text(text = "Username")},
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD0D0D0),
                unfocusedBorderColor = Color(0xFFFFFFFF),
            )
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            placeholder = { Text(text = "Password")},
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD0D0D0),
                unfocusedBorderColor = Color(0xFFFFFFFF),
            )
        )
        Button(
            onClick = {
                val user = User(
                    username = userName,
                    userBio = userBio,
                    email = email,
                    password = password
                )

                if (userName != "" && userBio != "" && email != "" && password != ""){
                    CoroutineScope(Dispatchers.IO).launch {
                        userDao.addUser(user)
                    }
                    Toast.makeText(context, "Successfully registered ${user.username}!", Toast.LENGTH_SHORT).show()
                    navController.navigate("Login")
                    userName = ""
                    userBio = ""
                    email = ""
                    password = ""
                }else{
                    Toast.makeText(context, "Enter all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .size(height = 45.dp, width = 150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF264CD9)
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                "Sing up",
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("Login")
                }.padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)

        ) {
            Text(
                "Already have an account?",
                fontSize = 16.sp
            )
            Text(
                "Login.",
                fontSize = 16.sp,
                color = Color(0xFF2D49D9),
                style = TextStyle(fontWeight = FontWeight.Bold)
                )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_8_pro")
@Composable
fun ShowRegisterScreen(){
    RegisterScreen(navController = rememberNavController())
}