package com.example.hollow_knight_silkroad.View.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment // Para createImageUri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.hollow_knight_silkroad.R
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.CrearHiloViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearHiloScreen(viewModel: CrearHiloViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val contexto = LocalContext.current

    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.onImagenSeleccionada(uri)
        }
    )

    var uriCamara by remember { mutableStateOf<Uri?>(null) }
    val lanzadorCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { exito ->
            if (exito) {
                viewModel.onImagenSeleccionada(uriCamara)
            }
        }
    )

    val pedirPermisoCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { fueConcedido ->
        if (fueConcedido) {
            val uri = crearUriImagenParaRespuesta(contexto)
            uriCamara = uri
            lanzadorCamara.launch(uri)
        } else {
            println("Permiso de cámara denegado")
        }
    }

    LaunchedEffect(uiState.hiloCreadoExitoso) {
        if (uiState.hiloCreadoExitoso) {
            navController.popBackStack()
            viewModel.onNavegacionCompletada()
        }
    }

    val coloresTextField = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
        focusedTextColor = Color.White, unfocusedTextColor = Color.White, cursorColor = Color.White,
        focusedIndicatorColor = Color.White, unfocusedIndicatorColor = Color.Gray,
        focusedTrailingIconColor = Color.White, unfocusedTrailingIconColor = Color.Gray,
        focusedLeadingIconColor = Color.White, unfocusedLeadingIconColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray, focusedPlaceholderColor = Color.DarkGray
    )
    val estiloTitulo = TextStyle(
        color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.White.copy(alpha = 0.7f), offset = Offset(0f, 0f), blurRadius = 10f)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Hilo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.8f),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        AppBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                        .background(color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {


                    uiState.error?.let { error ->
                        Text(
                            text = error, color = Color.Red, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                        )
                    }

                    Text("Título del hilo", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = uiState.titulo,
                        onValueChange = { viewModel.onTituloChange(it) },
                        placeholder = { Text("Escribe un título corto y descriptivo") },
                        colors = coloresTextField,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Mensaje", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = uiState.contenido,
                        onValueChange = { viewModel.onContenidoChange(it) },
                        placeholder = { Text("Escribe aquí el contenido de tu hilo...") },
                        colors = coloresTextField,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        maxLines = 10
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val uriImagenMostrada = remember(uiState.imagenUriSeleccionada) {
                            uiState.imagenUriSeleccionada?.let { Uri.parse(it) }
                        }
                        Image(
                            painter = if (uriImagenMostrada != null) rememberAsyncImagePainter(model = uriImagenMostrada)
                            else painterResource(id = R.drawable.knight_head_icon), // Placeholder
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.5f)),
                            contentScale = ContentScale.Crop
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { lanzadorGaleria.launch("image/*") }) {
                                Icon(Icons.Filled.PhotoLibrary, contentDescription = "Galería", modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Galería")
                            }
                            Button(onClick = {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) -> {
                                        val uri = crearUriImagenParaRespuesta(contexto)
                                        uriCamara = uri
                                        lanzadorCamara.launch(uri)
                                    }
                                    else -> {
                                        pedirPermisoCamara.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            }) {
                                Icon(Icons.Filled.PhotoCamera, contentDescription = "Cámara", modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Cámara")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedButton(
                        onClick = { viewModel.publicarHilo() },
                        modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(Modifier.size(24.dp), color=Color.White, strokeWidth=2.dp)
                        } else {
                            Text("Publicar Hilo", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

fun crearUriImagenParaRespuesta(contexto: Context): Uri {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val directorioAlmacenamiento = contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val archivo = File.createTempFile("JPEG_${timestamp}_", ".jpg", directorioAlmacenamiento)
    return FileProvider.getUriForFile(
        Objects.requireNonNull(contexto),
        "${contexto.packageName}.fileprovider",
        archivo
    )
}
