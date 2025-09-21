package com.fit2081.rhea32570988.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.fit2081.rhea32570988.R
import com.fit2081.rhea32570988.model.entity.Fruit
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.viewmodel.NavigationViewModel
import com.fit2081.rhea32570988.viewmodel.UiState
import kotlinx.coroutines.launch

@Composable
fun NutriCoachScreen(user: Patient?, navigationViewModel: NavigationViewModel) {
    val userId = user?.id ?: 0
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Heading
        Text(
            text = "NutriCoach",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Top 50%: Fruits Section
        Box(modifier = Modifier.weight(1f)) {
            FruitSection(user, navigationViewModel)
        }

        // Section Divider
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
//        Spacer(modifier = Modifier.height(8.dp))

        // Bottom 50%: GenAI Section
        Box(modifier = Modifier.weight(1f)) {
            GenAISection(userId, navigationViewModel)
        }
    }
}

@Composable
fun FruitSection(user: Patient?, navigationViewModel: NavigationViewModel) {
    val fruitScore = user?.fruitScore
    var fruitName by remember { mutableStateOf("") }
    var fruitDetails  by remember { mutableStateOf<Fruit?>(null) }
    var showFruitDetails  by remember { mutableStateOf(false) }

    // Coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()

    if ((fruitScore ?: 0.0) >= 10.0) {
        // Optimal Score: Show random image
        Image(
            painter = rememberAsyncImagePainter("https://picsum.photos/400/600"),
            contentDescription = "Random Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    } else {
        // Non-optimal Score: Show fruit search and details
        Column(modifier = Modifier.padding(16.dp)) {
            // Search fruit name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Fruit name input field
                OutlinedTextField(
                    value = fruitName,
                    onValueChange = { fruitName = it },
                    label = { Text(text = "Fruit Name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Fruit details button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            fruitDetails = navigationViewModel.getFruitDetails(fruitName)
                            showFruitDetails = true
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Details", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Details")
                }
            }

            if (showFruitDetails) {
                ShowFruitDetails(fruitDetails)
            }
        }
    }
}

@Composable
fun ShowFruitDetails(fruitDetails: Fruit?) {
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // General fruit details
        item {
            FruitCard("Name", fruitDetails?.name)
        }
        item {
            FruitCard("Family", fruitDetails?.family)
        }
        item {
            FruitCard("Order", fruitDetails?.order)
        }
        item {
            FruitCard("Genus", fruitDetails?.genus)
        }

        // Nutrition details
        item {
            FruitCard("Calories", fruitDetails?.nutritions?.calories.toString())
        }
        item {
            FruitCard("Fat", fruitDetails?.nutritions?.fat.toString())
        }
        item {
            FruitCard("Sugar", fruitDetails?.nutritions?.sugar.toString())
        }
        item {
            FruitCard("Carbohydrates", fruitDetails?.nutritions?.carbohydrates.toString())
        }
        item {
            FruitCard("Protein", fruitDetails?.nutritions?.protein.toString())
        }
    }
}

// Fruit Card with details
@Composable
fun FruitCard(fieldName: String, fieldValue: String?) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = fieldName,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "\t\t:\t\t",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = fieldValue ?: "-",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun GenAISection(userId: Int, navigationViewModel: NavigationViewModel) {
    var showModal  by remember { mutableStateOf(false) }
    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by navigationViewModel.tipUiState.collectAsState()

    // When a new tip is generated, save it
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val tipText = (uiState as UiState.Success).outputText.firstOrNull()
            if (!tipText.isNullOrBlank() && tipText != placeholderResult) {
                navigationViewModel.insertTip(
                    tip = tipText,
                    patientId = userId
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 16.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Button to generate AI tip
                Button(
                    onClick = {
                        navigationViewModel.generateTip()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Text("Generate AI Tip", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                // Show the generated tip
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier)
                } else {
                    var textColor = MaterialTheme.colorScheme.onSurface
                    if (uiState is UiState.Error) {
                        textColor = MaterialTheme.colorScheme.error
                        result = (uiState as UiState.Error).errorMessage
                    } else if (uiState is UiState.Success) {
                        textColor = MaterialTheme.colorScheme.onSurface
                        result = (uiState as UiState.Success).outputText.firstOrNull() ?: ""
                    }
                    Text(
                        text = result,
                        textAlign = TextAlign.Start,
                        color = textColor,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        // Button to show all tips
        Button(onClick = {
            showModal = true
        },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(0.7f)
                .height(45.dp)
        ){
            Text("Show All Tips")
        }

        // Show the modal
        if (showModal) {
            TipsModal(
                userId = userId,
                navigationViewModel = navigationViewModel,
                showModal = showModal,
                onDismiss = { showModal = false }
            )
        }
    }
}

@Composable
fun TipsModal(
    userId: Int,
    navigationViewModel: NavigationViewModel,
    showModal: Boolean,
    onDismiss: () -> Unit
) {
    if (!showModal) return

    val tips by navigationViewModel.getTipsByPatient(userId).collectAsState(initial = emptyList())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("AI Tips") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                items(tips) { tip ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = tip.content,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dismiss")
            }
        },
        confirmButton = {}
    )
}
