package com.example.talkapp2

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.speech.RecognitionListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.speech.tts.TextToSpeech

@Composable
fun ListenScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseDatabase.getInstance()
    val messagesRef = db.getReference("messages")
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    var texto by remember { mutableStateOf("Presiona el botón y habla") }
    var listeningState by remember { mutableStateOf(false) } // Indica si está escuchando

    val userLog = UserManager.getUserLog()

    var message by remember { mutableStateOf("") }
    var messageHistory by remember { mutableStateOf(listOf<String>()) }

    val tts = remember {
        TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(context, "Error al crear text to speech", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.login)
            }
        }
    }



    // Asegurarse de liberar el TTS cuando ya no se necesite
    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown() // Liberar recursos del TextToSpeech
        }
    }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechRecognizer.startListening(recognizerIntent)
        } else {
            Toast.makeText(context, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {
            texto = "Escuchando..."
            listeningState = true
        }

        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {
            texto = "Procesando..."
            listeningState = false
        }

        override fun onError(error: Int) {
            texto = "Error al reconocer"
            listeningState = false
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val recognizedText = matches?.firstOrNull() ?: "No se pudo reconocer"
            texto = recognizedText

            // Reproducir el texto reconocido usando el TTS creado en el bloque composable
            //tts.speak(recognizedText, TextToSpeech.QUEUE_FLUSH, null, null) // lo comentamos pero esta buena la funcionalidad

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val newMessageRef = messagesRef.push()

            val messageData = mapOf(
                "rut" to userLog.rut,
                "message" to recognizedText,
                "date" to today
            )

            newMessageRef.setValue(messageData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Mensaje guardado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error al guardar mensaje", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    speechRecognizer.setRecognitionListener(recognitionListener)

    // Escuchar cambios en la base de datos de Firebase
    LaunchedEffect(Unit) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        messagesRef.orderByChild("rut").equalTo(userLog.rut).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<String>()
                snapshot.children.forEach { child ->
                    val messageDate = child.child("date").getValue(String::class.java)
                    if (messageDate == today) {
                        val messageText = child.child("message").getValue(String::class.java)
                        messageText?.let { messages.add(it) }
                    }
                }
                messageHistory = messages
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AppTalk2.0",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp)
        )

        Button(
            onClick = {
                // Verificar permiso
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(text = "Escuchar")
        }

        Text(
            text = texto,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        // Sección de historial de mensajes
        Column(
            modifier = Modifier
                .heightIn(150.dp)
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.LightGray)
        ) {
            Text(text = "Historial", fontSize = 20.sp, modifier = Modifier.padding(8.dp))

            messageHistory.forEach { msg ->
                Text(
                    text = msg,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Input para escribir y enviar el mensaje
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Aquí escribo mi texto") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (message.isNotEmpty()) {
                        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val newMessageRef = messagesRef.push()

                        val messageData = mapOf(
                            "rut" to userLog.rut,
                            "message" to message,
                            "date" to today
                        )

                        newMessageRef.setValue(messageData).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
                                message = "" // Limpiar el campo de texto después de enviar el mensaje
                            } else {
                                Toast.makeText(context, "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .size(50.dp)
            ) {
                Text("→")
            }
        }
    }
}















//---------------------------------------
//@Composable
//fun ListenScreen(navController: NavController){
//    val context = LocalContext.current
//    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
//    var texto by remember { mutableStateOf("Presiona el botón y habla") }
//
//    val recognizerIntent = remember {
//        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES") // Español
//        }
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            speechRecognizer.startListening(recognizerIntent)
//        } else {
//            Toast.makeText(context, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    val recognitionListener = object : RecognitionListener {
//        override fun onReadyForSpeech(params: Bundle?) {}
//        override fun onBeginningOfSpeech() {
//            texto = "Escuchando..."
//        }
//
//        override fun onRmsChanged(rmsdB: Float) {}
//        override fun onBufferReceived(buffer: ByteArray?) {}
//        override fun onEndOfSpeech() {
//            texto = "Procesando..."
//        }
//
//        override fun onError(error: Int) {
//            texto = "Error al reconocer"
//        }
//
//        override fun onResults(results: Bundle?) {
//            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            texto = matches?.firstOrNull() ?: "No se pudo reconocer"
//        }
//
//        override fun onPartialResults(partialResults: Bundle?) {}
//        override fun onEvent(eventType: Int, params: Bundle?) {}
//    }
//
//    speechRecognizer.setRecognitionListener(recognitionListener)
//
//    // Interfaz de usuario
//    Column(
//        modifier = Modifier
//            .background(color = Color.White)
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(
//            onClick = {
//                // Verificar permiso
//                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//            }
//        ) {
//            Text(text = "Presionar y hablar")
//        }
//        Spacer(modifier = Modifier.padding(28.dp))
//
//        Text(
//            text = "Presiona para escuchar ... ",
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        Spacer(modifier = Modifier.padding(16.dp))
//        Text(
//            text = texto,
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//
//    }

//
//
//
//    Column(
//        modifier = Modifier
//            .background(color = Color.White)
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = texto,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(
//            modifier = Modifier.height(25.dp)
//        )
//
//        Button(
//            onClick = {
//
//            },
//            modifier = Modifier.height(50.dp).width(250.dp)
//        ) {
//            Text(text = "Escuchar")
//
//        }
//
//        Spacer(
//            modifier = Modifier.height(60.dp)
//        )

//        TextButton(
//            onClick = {
//            }
//        ) {
//        }
//        Spacer(
//            modifier = Modifier.height(50.dp)
//        )

//        TextButton(
//            onClick = { /*TODO*/ }
//        ) {
//            Text(
//                text = "¿Olvidaste tu contraseña?",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }


//    }
//}