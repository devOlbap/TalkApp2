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
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current

    var rut by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro en TalkApp2.0",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text(text = "RUT") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Nombre de Usuario") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(text = "Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (rut.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
                    Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password != confirmPassword) {
                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val db  = FirebaseDatabase.getInstance()
                val ref = db.getReference("users")

                val user = User(
                    rut= rut,
                    username = username,
                    password = password
                )

                ref.push().setValue(user).addOnCompleteListener{
                    task ->
                        if(task.isSuccessful){
                            Toast.makeText(context, "Usuario registrado ", Toast.LENGTH_LONG).show()
                            navController.navigate(Routes.login)
                        }else{
                            Toast.makeText(context, "Usuario no registrado", Toast.LENGTH_LONG).show()
                        }
                }

//                val newUser = UserManager.addUser(rut,password,username,UserManager.getCountUsers()+1)
//
//                Toast.makeText(context, "Usuario registrado: "+newUser.username, Toast.LENGTH_LONG).show()
//
            },
            modifier = Modifier
                .height(50.dp)
                .width(250.dp)
        ) {
            Text(text = "Registrarse")
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = { navController.navigate(Routes.login) }
        ) {
            Text(
                text = "Ya tienes una cuenta? Inicia sesión",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

