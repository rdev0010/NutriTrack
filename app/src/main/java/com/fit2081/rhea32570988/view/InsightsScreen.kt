package com.fit2081.rhea32570988.view

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.viewmodel.NavigationViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

// Insights Screen UI
@Composable
fun InsightsScreen(user: Patient?, navController: NavHostController) {
    // Current Context
    val context = LocalContext.current

    // Food scores
    val foodQualityScore = user?.totalScore
    val vegetables = user?.vegetableScore
    val fruits = user?.fruitScore
    val grainsCereal = user?.grainsCerealScore
    val wholeGrains = user?.wholeGrainsScore
    val meat = user?.meatScore
    val dairy = user?.dairyScore
    val water = user?.waterScore
    val saturatedFats = user?.saturatedFatsScore
    val unsaturatedFats = user?.unsaturatedFatsScore
    val sodium = user?.sodiumScore
    val sugar = user?.sugarScore
    val alcohol = user?.alcoholScore
    val discretionary = user?.discretionaryScore

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
            text = "Insights: Food Source",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Progress Bars Score
        CategoryProgressBar("Vegetables", vegetables)
        CategoryProgressBar("Fruits", fruits)
        CategoryProgressBar("Grains & Cereals", grainsCereal)
        CategoryProgressBar("Whole Grains", wholeGrains)
        CategoryProgressBar("Meat & Alternatives", meat)
        CategoryProgressBar("Dairy", dairy)
        CategoryProgressBar("Water", water)
        CategoryProgressBar("Saturated Fats", saturatedFats)
        CategoryProgressBar("Unsaturated Fats", unsaturatedFats)
        CategoryProgressBar("Sodium", sodium)
        CategoryProgressBar("Sugar", sugar)
        CategoryProgressBar("Alcohol", alcohol)
        CategoryProgressBar("Discretionary Foods", discretionary)

        // Spacer
        Spacer(modifier = Modifier.height(24.dp))

        // Total Food Quality Score
        Text(
            text = "Total Food Quality Score: ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )
        // Progress Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { ((foodQualityScore ?: 0.0) / 100).toFloat() },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Score Text
            Text(
                text = "${foodQualityScore}/100",
                textAlign = TextAlign.End,
                modifier = Modifier.width(80.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Share Button
            Button(
                onClick = {
                    // create a intent to share the text
                    val shareIntent = Intent(ACTION_SEND)
                    // set the type of data to share
                    shareIntent.type = "text/plain"
                    // set the data to share, in this case, the text
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "My Food Quality Score: $foodQualityScore")
                    // start the activity to share the text, with a chooser to select the app
                    context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp)
            ) {
                Text("Share With \nSomeone", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Improve my Diet Button - navigate to NutriCoach Screen
            Button(
                onClick = { navController.navigate("nutricoach/${user?.id}") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp)
            ) {
                Text("Improve \nMy Diet", fontSize = 16.sp)
            }
        }
    }
}

// Create progress bars for the given category
@Composable
fun CategoryProgressBar(category: String, score: Double?) {
    // Set the total score for the progress bar based on the category
    val totalScore = if (category == "Water" || category == "Alcohol") 5 else 10
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Text
        Text(
            text = category,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(130.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = { ((score ?: 0.0) / totalScore).toFloat() },
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Score Text
        Text(
            text = "${score}/${totalScore}",
            textAlign = TextAlign.End,
            modifier = Modifier.width(60.dp)
        )

    }
}