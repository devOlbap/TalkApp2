package com.example.talkapp2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController


@Composable
fun LoginScreen (navController : NavController){
    val context = LocalContext.current

    var rut by remember {
        mutableStateOf("")
    }

    var pass by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TalkApp2.0",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(25.dp)
        )
        TextField(
            value = rut,
            onValueChange ={
                rut = it
            },
            label = {
                Text(text = "RUT")
            }
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        TextField(
            value = pass,
            onValueChange ={
                pass = it
            },
            label = {
                Text(text = "Contraseña")
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Button(
            onClick = {

                if(rut.trim().length == 0 ){
                    Toast.makeText(context, "Debe ingresar un RUT", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if(pass.trim().length == 0 ){
                    Toast.makeText(context, "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val user = UserManager.findUser(rut,pass)

                if(user == null){
                    Toast.makeText(context, "No se registra usuario con las credenciales ingresadas", Toast.LENGTH_LONG).show()
                    return@Button
                }

                Toast.makeText(context, "Bienvenid@! "+user.username+".", Toast.LENGTH_SHORT).show()

                UserManager.setUserLog(user)

                navController.navigate(Routes.listen) // home



            },
            modifier = Modifier.height(50.dp).width(250.dp)
            ) {
            Text(text = "Ingresar")

        }

        Spacer(
            modifier = Modifier.height(60.dp)
        )

        TextButton(
            onClick = {
                navController.navigate(Routes.register)
            }
        ) {
            Text(
                text = "Registrate",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            )
        }
        Spacer(
            modifier = Modifier.height(50.dp)
        )

        TextButton(
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        
    }
}



