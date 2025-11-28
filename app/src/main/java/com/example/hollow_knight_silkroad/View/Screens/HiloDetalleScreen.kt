package com.example.hollow_knight_silkroad.View.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.asImageBitmap // <--- IMPORTANTE
import androidx.compose.ui.graphics.painter.BitmapPainter // <--- IMPORTANTE
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.hollow_knight_silkroad.R
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.View.Components.rememberBase64Painter
import com.example.hollow_knight_silkroad.ViewModel.HiloDetalleViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.TimeZone

@Composable
fun HiloDetalleScreen(
    hiloId: Int,
    viewModel: HiloDetalleViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val contexto = LocalContext.current

    LaunchedEffect(hiloId) {
        viewModel.cargarDetallesHilo(hiloId)
    }

    LaunchedEffect(uiState.navegacion) {
        if (uiState.navegacion == "foro") {
            navController.popBackStack()
            viewModel.onNavegacionCompletada()
        }
    }

    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> viewModel.onImagenRespuestaSeleccionada(uri) }
    )

    var uriCamara by remember { mutableStateOf<Uri?>(null) }
    val lanzadorCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { exito ->
            if (exito) { viewModel.onImagenRespuestaSeleccionada(uriCamara) }
        }
    )

    val pedirPermisoCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { fueConcedido ->
        if (fueConcedido) {
            val uri = crearUriImagen(contexto)
            uriCamara = uri
            lanzadorCamara.launch(uri)
        } else {
            println("Permiso de cámara denegado")
        }
    }

    val customTextFieldColors = TextFieldDefaults.colors(
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

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .navigationBarsPadding() // Asegura que no choque con la barra de navegación
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver al foro", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                if (uiState.hilo != null && uiState.esMiHilo) {
                    IconButton(onClick = { viewModel.eliminarHiloActual() }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar Hilo", tint = Color.Red)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.isLoading && uiState.hilo == null) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                else if (uiState.error != null && uiState.hilo == null) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            Text("Error: ${uiState.error}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                else if (uiState.hilo != null) {
                    item {
                        Text(
                            text = uiState.hilo!!.titulo,
                            style = estiloTitulo.copy(fontSize = 24.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )
                    }
                    item {
                        MensajeItem(
                            autor = uiState.hilo!!.autor,
                            contenido = uiState.hilo!!.contenido,
                            fecha = uiState.hilo!!.fechaCreacion,
                            isOriginal = true,
                            imagenUriString = uiState.hilo!!.imagenUri
                        )
                    }
                    items(uiState.respuestas) { respuesta ->
                        MensajeItem(
                            autor = respuesta.autor,
                            contenido = respuesta.contenido,
                            fecha = respuesta.fechaCreacion,
                            isOriginal = false,
                            imagenUriString = respuesta.imagenUri
                        )
                    }
                    if (uiState.error != null && uiState.hilo != null) {
                        item {
                            Text(
                                text = "Error: ${uiState.error}",
                                color = Color.Red, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            if (uiState.hilo != null) {
                Divider(color = Color.Gray, thickness = 1.dp)
                FormularioRespuesta(
                    contenido = uiState.nuevaRespuestaContenido,
                    imagenUriRespuesta = uiState.imagenUriNuevaRespuesta,
                    onContenidoChange = { viewModel.onNuevaRespuestaChange(it) },
                    onPublicarClick = { viewModel.publicarRespuesta(contexto) },
                    onGaleriaClick = { lanzadorGaleria.launch("image/*") },
                    onCamaraClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) -> {
                                val uri = crearUriImagen(contexto)
                                uriCamara = uri
                                lanzadorCamara.launch(uri)
                            }
                            else -> {
                                pedirPermisoCamara.launch(Manifest.permission.CAMERA)
                            }
                        }
                    },
                    isLoading = uiState.isLoading,
                    modifier = Modifier.imePadding()
                )
            }
        }
    }
}

@Composable
fun MensajeItem(autor: String, contenido: String, fecha: Long, isOriginal: Boolean, imagenUriString: String? = null) {
    val backgroundColor = if (isOriginal) Color.DarkGray.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.5f)
    val border = if (isOriginal) BorderStroke(2.dp, Color.White) else null

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(autor, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))

            if (!imagenUriString.isNullOrBlank()) {
                Image(
                    painter = rememberBase64Painter(base64String = imagenUriString),
                    contentDescription = "Imagen adjunta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(contenido, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                formatearFechaDetalle(fecha),
                color = Color.LightGray,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun FormularioRespuesta(
    contenido: String,
    imagenUriRespuesta: String?,
    onContenidoChange: (String) -> Unit,
    onPublicarClick: () -> Unit,
    onGaleriaClick: () -> Unit,
    onCamaraClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val customTextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
        focusedTextColor = Color.White, unfocusedTextColor = Color.White, cursorColor = Color.White,
        focusedIndicatorColor = Color.White, unfocusedIndicatorColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray, focusedPlaceholderColor = Color.DarkGray
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Escribe una respuesta", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val uriImagenMostrada = remember(imagenUriRespuesta) {
                imagenUriRespuesta?.let { Uri.parse(it) }
            }
            Image(
                painter = if (uriImagenMostrada != null) rememberAsyncImagePainter(model = uriImagenMostrada)
                else painterResource(id = R.drawable.knight_head_icon),
                contentDescription = "Imagen para respuesta",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(onClick = onGaleriaClick, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = "Galería", modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Galería")
                }
                Button(onClick = onCamaraClick, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.PhotoCamera, contentDescription = "Cámara", modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Cámara")
                }
            }
        }

        OutlinedTextField(
            value = contenido,
            onValueChange = onContenidoChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            placeholder = { Text("Tu respuesta...") },
            colors = customTextFieldColors,
            maxLines = 5
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onPublicarClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isLoading && contenido.isNotBlank(),
            border = BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Publicar Respuesta", fontSize = 16.sp)
        }
    }
}

private fun formatearFechaDetalle(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale("es", "CL"))
    sdf.timeZone = TimeZone.getTimeZone("America/Santiago")
    val fecha = Date(timestamp)
    return sdf.format(fecha)
}

fun crearUriImagen(contexto: Context): Uri {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val directorioAlmacenamiento = contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val archivo = File.createTempFile("JPEG_${timestamp}_", ".jpg", directorioAlmacenamiento)

    return FileProvider.getUriForFile(
        contexto,
        "${contexto.packageName}.fileprovider",
        archivo
    )
}

