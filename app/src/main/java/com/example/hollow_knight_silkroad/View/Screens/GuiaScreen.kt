package com.example.hollow_knight_silkroad.View.Screens
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hollow_knight_silkroad.Model.GuiaSection
import com.example.hollow_knight_silkroad.Model.ImageBlock
import com.example.hollow_knight_silkroad.Model.TextBlock
import com.example.hollow_knight_silkroad.View.Components.AppBackground
import com.example.hollow_knight_silkroad.ViewModel.GuiaViewModel

@Composable
fun GuiaScreen(
    viewModel: GuiaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AppBackground {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text("Cargando guía...", modifier = Modifier.padding(top = 80.dp), color = MaterialTheme.colorScheme.onPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(uiState.sections, key = { _, section -> section.id }) { index, section ->
                    GuiaSectionItem(
                        section = section,
                        isExpanded = uiState.expandedSectionId == section.id,
                        onToggle = { viewModel.toggleSection(section.id) },
                        onNext = { viewModel.expandNextSection(section.id) },
                        isLastItem = index == uiState.sections.lastIndex
                    )
                    if (index < uiState.sections.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuiaSectionItem(
    section: GuiaSection,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onNext: () -> Unit,
    isLastItem: Boolean
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = section.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)) {
                section.contentBlocks.forEach { block ->
                    when (block) {
                        is TextBlock -> {
                            Text(
                                text = block.text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 8.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        is ImageBlock -> {
                            Image(
                                painter = painterResource(id = block.resourceId),
                                contentDescription = "Imagen de la guía",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(bottom = 8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }


                if (!isLastItem) {
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp)
                    ) {
                        Text("Siguiente")
                    }
                }
            }
        }
    }
}