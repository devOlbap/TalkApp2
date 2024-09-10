
package com.example.talkapp2

import android.widget.ImageButton
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController: NavController) {

    val user_log = UserManager.getUserLog()


    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { /*TODO*/ },


            ) {
            Text(
                text = "Hablar",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(35.dp))
        Button(
            onClick = { /*TODO*/ },


            ) {
            Text(
                text = "Escuchar",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(150.dp))

        Button(onClick = {
            UserManager.delUserLog()
            navController.navigate(Routes.login)
        }
        ) {
            Text(
                text = "Salir",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

        }

    }


//    Row(
//        modifier = Modifier.padding(20.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(
//            modifier = Modifier
//                .background(color = Color.White)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            Button(onClick = {
//                UserManager.delUserLog()
//                navController.navigate(Routes.login)
//            }
//            ) {
//                Text(
//                    text = "Salir",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.DarkGray,
//                    textDecoration = TextDecoration.Underline
//                )
//
//            }
//            Button(
//                onClick = {
//
//                }
//            )
//            {
//                Text(
//                    text = "FAQ",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.DarkGray,
//                    textDecoration = TextDecoration.Underline
//                )
//
//            }
//
//        }
//    }
//
//    Spacer(modifier = Modifier.height(50.dp))
//
//    Row(
//        modifier = Modifier.padding(20.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ){
//        Column(
//            modifier = Modifier
//                .background(color = Color.White)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(
//                onClick = { /*TODO*/ },
//
//
//                ) {
//                Text(
//                    text = "Hablar",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.DarkGray,
//                    textDecoration = TextDecoration.Underline
//                )
//            }
//            Spacer(modifier = Modifier.height(35.dp))
//            Button(
//                onClick = { /*TODO*/ },
//
//
//                ) {
//                Text(
//                    text = "Escuchar",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.DarkGray,
//                    textDecoration = TextDecoration.Underline
//                )
//            }
//        }
//    }



}


@Composable
fun HeaderApp() {

}

@Composable
fun UsersList(navController: NavController){
    val users = UserManager.getUsers() //OBTENEMOS USUARIOS

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lista de Usuarios",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserRow(user = user, onClick = {
                    //PODEMOS MOSTRAR O EDITAR DETALLES
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

//FUNCION PARA CREAR TARJETA DE USUARIO
@Composable
fun UserRow(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "RUT: ${user.rut}", fontSize = 16.sp)
            Text(text = "Nombre: ${user.username}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}