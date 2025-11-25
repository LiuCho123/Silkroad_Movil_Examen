package com.example.hollow_knight_silkroad.View.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hollow_knight_silkroad.R

@Composable
fun HomeScreen(navController: androidx.navigation.NavController){
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.background_hollow),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_silk_road),
                contentDescription = "Logo Hollow Knight Silk Road",
                modifier = Modifier.fillMaxWidth(0.8f) // ¡CAMBIO CLAVE!
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally){
                val estiloTextoBrillante = TextStyle(
                    color = Color.White,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    shadow = Shadow(
                        color = Color.White.copy(alpha = 0.7f),
                        offset = Offset(0f, 0f),
                        blurRadius = 10f
                    )
                )
                Text(
                    text = "Tu Hogar en el Corazón de Hallownest.",
                    style = estiloTextoBrillante
                )
                Spacer(modifier = Modifier.height(20.dp))
                // 2. Caballero MÁS grande
                Image(
                    painter = painterResource(id = R.drawable.the_knight),
                    contentDescription = "The Knight",
                    modifier = Modifier.size(150.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                val modifierBoton = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                OutlinedButton(
                    onClick = {navController.navigate("login")},
                    modifier = modifierBoton,
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Iniciar Sesión", fontSize = 16.sp)
                }

                OutlinedButton(
                    onClick = {navController.navigate("register")},
                    modifier = modifierBoton,
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Registrarse", fontSize = 16.sp)
                }
            }
        }
    }
}