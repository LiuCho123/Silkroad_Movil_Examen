package com.example.hollow_knight_silkroad.View.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hollow_knight_silkroad.Model.Pregunta
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.TriviaUIState
import com.example.hollow_knight_silkroad.ViewModel.TriviaViewModel

@Composable
fun TriviaScreen(viewModel: TriviaViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    AppBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
                uiState.juegoTerminado -> {
                    PantallaResultado(
                        puntaje = uiState.puntaje,
                        totalPreguntas = uiState.preguntas.size,
                        onReiniciar = { viewModel.reiniciarTrivia() }
                    )
                }
                uiState.preguntas.isNotEmpty() -> {
                    val preguntaActual = uiState.preguntas[uiState.preguntaActualIndex]
                    PantallaPregunta(
                        uiState = uiState,
                        pregunta = preguntaActual,
                        onRespuestaClick = { opcion ->
                            viewModel.handleRespuestaClick(opcion)
                        }
                    )
                }
                else -> {
                    Text("No hay preguntas de trivia disponibles.", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun PantallaPregunta(
    uiState: TriviaUIState,
    pregunta: Pregunta,
    onRespuestaClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pregunta ${uiState.preguntaActualIndex + 1} de ${uiState.preguntas.size}",
            color = Color.LightGray,
            fontSize = 16.sp
        )

        Text(
            text = pregunta.pregunta,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        pregunta.opciones.forEach { opcion ->
            val esLaSeleccionada = uiState.respuestaSeleccionada == opcion
            val esLaCorrecta = opcion == pregunta.respuestaCorrecta

            val colorBoton = when {
                uiState.respuestaSeleccionada != null -> {
                    if (esLaCorrecta) Color.Green
                    else if (esLaSeleccionada) Color.Red
                    else Color.DarkGray
                }
                else -> Color(0xFF555555)
            }

            Button(
                onClick = { onRespuestaClick(opcion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton,
                    disabledContainerColor = colorBoton
                ),
                enabled = uiState.respuestaSeleccionada == null
            ) {
                Text(opcion, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun PantallaResultado(puntaje: Int, totalPreguntas: Int, onReiniciar: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "¡Trivia completada!",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Obtuviste $puntaje de $totalPreguntas respuestas correctas.",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        OutlinedButton(
            onClick = onReiniciar,
            modifier = Modifier.fillMaxWidth(0.8f).height(50.dp),
            border = BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Jugar de nuevo", fontSize = 16.sp)
        }
    }
}
