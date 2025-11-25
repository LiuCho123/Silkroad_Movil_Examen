package com.example.hollow_knight_silkroad.View.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hollow_knight_silkroad.R
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.RecuperarContrasenaViewModel

@Composable
fun RecuperarPasswordScreen(viewModel: RecuperarContrasenaViewModel, navController: NavController) {
    // Obtenemos el estado actual del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // --- Navegación Automática (al login) ---
    LaunchedEffect(uiState.navegacion) {
        if (uiState.navegacion == "login") {
            navController.navigate("login") {
                popUpTo("home") { inclusive = false }
            }
            viewModel.onNavegacionCompletada()
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
    val estiloTituloBrillante = TextStyle(
        color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.White.copy(alpha = 0.7f), offset = Offset(0f, 0f), blurRadius = 10f)
    )

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
                    text = "Crear Nueva Contraseña",
                    style = estiloTituloBrillante,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                    )
                }

                Text("Nueva Contraseña", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.nuevaPassword,
                    onValueChange = { viewModel.onNuevaPasswordChange(it) },
                    placeholder = { Text("Ingrese nueva contraseña") },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.knight_head_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    visualTransformation = if (uiState.showNuevaPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.mostrarNuevaPassword() }) {
                            Icon(
                                imageVector = if (uiState.showNuevaPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Mostrar/Ocultar nueva contraseña"
                            )
                        }
                    },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Confirmar Nueva Contraseña", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.confirmarPassword,
                    onValueChange = { viewModel.onConfirmarPasswordChange(it) },
                    placeholder = { Text("Confirme nueva contraseña") },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.knight_head_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    visualTransformation = if (uiState.showConfirmarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.mostrarConfirmarNuevaPassword() }) {
                            Icon(
                                imageVector = if (uiState.showConfirmarPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Mostrar/Ocultar confirmación"
                            )
                        }
                    },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = { viewModel.actualizarPassword() },
                    modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(Modifier.size(24.dp), color=Color.White, strokeWidth=2.dp)
                    } else {
                        Text("Actualizar Contraseña", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}