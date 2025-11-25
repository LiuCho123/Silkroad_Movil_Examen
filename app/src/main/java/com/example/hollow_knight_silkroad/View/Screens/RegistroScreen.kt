package com.example.hollow_knight_silkroad.View.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hollow_knight_silkroad.R
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.RegistroViewModel

@Composable
fun RegisterScreen(viewModel: RegistroViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registroExitoso) {
        if(uiState.registroExitoso){
            navController.navigate("login"){
                popUpTo("home"){inclusive = false}
            }
            viewModel.onNavegacionCompletada()
        }
    }

    val customTextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.Gray,
        focusedTrailingIconColor = Color.White,
        unfocusedTrailingIconColor = Color.Gray,
        focusedLeadingIconColor = Color.White,
        unfocusedLeadingIconColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray,
        focusedPlaceholderColor = Color.DarkGray
    )

    val estiloTituloBrillante = TextStyle(
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        shadow = Shadow(
            color = Color.White.copy(alpha = 0.7f),
            offset = Offset(0f, 0f),
            blurRadius = 10f
        )
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
                    text = "Registrarse",
                    style = estiloTituloBrillante,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                    )
                }

                Text("Correo", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    placeholder = { Text("Ingrese su correo") },
                    leadingIcon = { //
                        Image(
                            painter = painterResource(id = R.drawable.knight_head_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Usuario", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    placeholder = { Text("Ingrese su usuario") },
                    leadingIcon = { //
                        Image(
                            painter = painterResource(id = R.drawable.knight_head_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Contraseña", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = { Text("Ingrese su contraseña") },
                    leadingIcon = { //
                        Image(
                            painter = painterResource(id = R.drawable.knight_head_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleShowPassword() }) {
                            Icon(
                                imageVector = if (uiState.showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Mostrar/Ocultar contraseña"
                            )
                        }
                    },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Confirmar contraseña", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                    placeholder = { Text("Confirme su contraseña") },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.knight_head_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    visualTransformation = if (uiState.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleShowConfirmPassword() }) {
                            Icon(
                                imageVector = if (uiState.showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Mostrar/Ocultar contraseña"
                            )
                        }
                    },
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = { viewModel.register() },
                    modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    enabled = !uiState.isLoading
                ) {
                    if(uiState.isLoading){
                        CircularProgressIndicator(Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else{
                        Text("Registrarse", fontSize = 16.sp)

                    }

                }

                Spacer(modifier = Modifier.height(24.dp))

                ClickableText(
                    text = AnnotatedString("¿Ya tienes una cuenta? ¡Inicia sesión!"),
                    onClick = {navController.navigate("login")},
                    style = TextStyle(color = Color(0xFF4DB6AC), textDecoration = TextDecoration.Underline),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}