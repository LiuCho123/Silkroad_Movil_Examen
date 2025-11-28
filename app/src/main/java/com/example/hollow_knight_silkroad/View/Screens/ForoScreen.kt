package com.example.hollow_knight_silkroad.View.Screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.View.Components.rememberBase64Painter
import com.example.hollow_knight_silkroad.ViewModel.ForoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForoScreen(viewModel: ForoViewModel, navController: NavController){
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text("Foro de Hallownest")},
                navigationIcon = {
                    IconButton(onClick = {/* TODO: Abrir menú lateral */}) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                },
                actions ={
                    IconButton(onClick = {navController.navigate("crearHilo")}){
                        Icon(Icons.Filled.Add, contentDescription = "Crear nuevo hilo")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.8f),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ){ paddingValues ->
        AppBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    uiState.error != null ->{
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp)
                        )
                    }

                    uiState.hilosConConteo.isNotEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.hilosConConteo){ (hilo, conteoRespuestas) ->
                                HiloItem(hilo = hilo, numRespuestas = conteoRespuestas){ hiloId ->
                                    navController.navigate("hiloDetalle/$hiloId")
                                }
                            }
                        }
                    }

                    else ->{
                        Text(
                            text = "No hay hilos disponibles",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HiloItem(hilo: Hilo, numRespuestas: Int, onClick: (Int) -> Unit){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick(hilo.idHilo)},
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column (modifier = Modifier.padding(16.dp)){
            if (!hilo.imagenUri.isNullOrBlank()){
                Image(
                    painter = rememberBase64Painter(base64String = hilo.imagenUri),
                    contentDescription = "Imagen del hilo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = hilo.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Autor: ${hilo.autor}", fontSize = 14.sp, color = Color.LightGray)
                Text("Resp: $numRespuestas", fontSize = 14.sp, color = Color.LightGray)

                Text(
                    text = formatearFecha(hilo.fechaCreacion),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun formatearFecha(timestamp: Long): String{
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "CL"))
    sdf.timeZone = TimeZone.getTimeZone("America/Santiago")
    val fecha = Date(timestamp)
    return sdf.format((fecha))
}

