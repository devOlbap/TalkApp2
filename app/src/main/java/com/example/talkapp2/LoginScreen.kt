package com.example.talkapp2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.google.firebase.database.*

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    var rut by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

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
        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text(text = "RUT") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text(text = "Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (rut.trim().isEmpty()) {
                    Toast.makeText(context, "Debe ingresar un RUT", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (pass.trim().isEmpty()) {
                    Toast.makeText(context, "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Verificar en Firebase si existe un usuario con el RUT y la contraseña ingresados
                usersRef.orderByChild("rut").equalTo(rut).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var userFound: User? = null
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            if (user?.password == pass) {
                                userFound = user
                                break
                            }
                        }

                        if (userFound != null) {
                            Toast.makeText(context, "Bienvenid@! ${userFound.username}.", Toast.LENGTH_SHORT).show()
                            UserManager.setUserLog(userFound)
                            // Aquí puedes guardar al usuario en alguna variable global o clase de sesión
                            navController.navigate(Routes.listen) // Redirigir al home
                        } else {
                            Toast.makeText(context, "No se registra usuario con las credenciales ingresadas", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error en la consulta: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                })
            },
            modifier = Modifier
                .height(50.dp)
                .width(250.dp)
        ) {
            Text(text = "Ingresar")
        }

        Spacer(modifier = Modifier.height(60.dp))

        TextButton(
            onClick = { navController.navigate(Routes.register) }
        ) {
            Text(
                text = "Registrate",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        TextButton(onClick = { /* Aquí iría la recuperación de contraseña */ }) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
