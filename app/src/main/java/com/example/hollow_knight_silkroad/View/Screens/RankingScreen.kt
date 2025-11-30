package com.example.hollow_knight_silkroad.View.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.RankingUser
import com.example.hollow_knight_silkroad.ViewModel.RankingViewModel

@Composable
fun RankingScreen(
    viewModel: RankingViewModel
) {
    val rankingState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarRanking()
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Ranking de Hallownest",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                "Top Exploradores",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )

            if (rankingState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFFFFD700))
                        Text(
                            "Consultando archivos...",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        RankingRowHeader()
                        HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                    }
                    itemsIndexed(rankingState.topUsers) { index, user ->
                        RankingRow(rank = index + 1, user = user)
                        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2C003E).copy(alpha = 0.8f)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFFFD700)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🌍 Récord Mundial (Speedrun.com)",
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = rankingState.speedrunRecord,
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Categoría: Any% No Major Glitches",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RankingRowHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "#",
            modifier = Modifier.width(40.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            "Jugador",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            "Items",
            modifier = Modifier.width(80.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun RankingRow(rank: Int, user: RankingUser) {
    val backgroundColor = if (user.isCurrentUser) Color(0xFFD0FF00).copy(alpha = 0.15f) else Color.Transparent
    val rankColor = if (user.isCurrentUser) Color(0xFFD0FF00) else MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$rank",
            modifier = Modifier.width(40.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = rankColor
        )
        Text(
            user.name,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1
        )
        Text(
            text = user.textoProgreso,
            modifier = Modifier.width(90.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFFD0FF00),
            textAlign = TextAlign.End
        )
    }
}