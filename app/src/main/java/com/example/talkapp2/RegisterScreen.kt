package com.example.talkapp2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun RegisterScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text(text = "Bienvenido! AL REGISTRO")
        TextButton(
            onClick = {
                navController.navigate(Routes.login)
            }
        ) {
            Text(
                text = "VOLVER A LOGIN",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}