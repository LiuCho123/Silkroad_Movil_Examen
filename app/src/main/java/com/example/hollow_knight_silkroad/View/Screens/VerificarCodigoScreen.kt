package com.example.hollow_knight_silkroad.View.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.RecuperarContrasenaViewModel
import androidx.compose.ui.text.font.FontStyle

@Composable
fun VerificarCodigoScreen(viewModel: RecuperarContrasenaViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navegacion) {
        if (uiState.navegacion == "recuperarPassword") {
            navController.navigate("recuperarPassword")
            viewModel.onNavegacionCompletada()
        }
    }

    val customTextFieldColors = TextFieldDefaults.colors(/*...*/)
    val estiloTituloBrillante = TextStyle(/*...*/)

    AppBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                    .background(color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Verificar Código",
                    style = estiloTituloBrillante,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingresa el código que \"recibiste\" en tu correo.",
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
                )

                uiState.codigoGenerado?.let { codigo ->
                    Text(
                        text = "(Código simulado: $codigo)",
                        color = Color.Yellow,
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )
                }

                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                    )
                }

                Text("Código de Verificación", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.codigoIngresado,
                    onValueChange = { viewModel.onCodigoChange(it) },
                    placeholder = { Text("123456") },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = { viewModel.verificarCodigo() },
                    modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                ) {
                    Text("Verificar", fontSize = 16.sp)
                }
            }
        }
    }
}