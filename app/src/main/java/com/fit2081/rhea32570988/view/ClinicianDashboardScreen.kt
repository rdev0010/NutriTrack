package com.fit2081.rhea32570988.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.rhea32570988.R
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.viewmodel.NavigationViewModel
import com.fit2081.rhea32570988.viewmodel.UiState
import org.intellij.lang.annotations.Pattern

@Composable
fun ClinicianDashboardScreen(
    user: Patient?,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Heading
        Text(
            text = "Clinician Dashboard",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Average Score Section
        AverageScoreSection(navigationViewModel)

        // Section Divider
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))

        // Data Pattern Section
        PatternSection(user?.id ?: 0, navigationViewModel, navController)

    }
}

@Composable
fun AverageScoreSection(navigationViewModel: NavigationViewModel) {
    // Average HEIFA Scores
    var avgMaleScore by remember { mutableStateOf(0.0) }
    var avgFemaleScore by remember { mutableStateOf(0.0) }
    LaunchedEffect(Unit) {
        avgMaleScore = navigationViewModel.getAverageMaleScore() ?: 0.0
        avgFemaleScore = navigationViewModel.getAverageFemaleScore() ?: 0.0
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Average HEIFA Score Cards
        ScoreCard("Average HEIFA (Male)", avgMaleScore.toString())
        ScoreCard("Average HEIFA (Female)", avgFemaleScore.toString())
    }

}

// Score Card
@Composable
fun ScoreCard(fieldName: String, fieldValue: String?) {
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

// Pattern Section for finding data patterns
@Composable
fun PatternSection(userId: Int, navigationViewModel: NavigationViewModel, navController: NavHostController) {
    var patterns by rememberSaveable { mutableStateOf(listOf<String>()) }
    val uiState by navigationViewModel.patternUiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Find Data Pattern Button
                Button(
                    onClick = {
                        navigationViewModel.findDataPatterns()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(55.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Data pattern",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Find Data Pattern", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Show the generated patterns
            if (uiState is UiState.Loading) {
                item {
                    CircularProgressIndicator(modifier = Modifier)
                }
            } else {
                if (uiState is UiState.Error) {
                    val error = (uiState as UiState.Error).errorMessage
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else if (uiState is UiState.Success) {
                    patterns = (uiState as UiState.Success).outputText

                    items(patterns) { pattern ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = pattern,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // Done Button
        Button(
            onClick = {
                navController.navigate("Settings/${userId}")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(0.7f)
                .height(45.dp)
        ) {
            Text("Done", fontSize = 16.sp)
        }
    }
}