package com.fit2081.rhea32570988.view

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.rhea32570988.R
import com.fit2081.rhea32570988.model.entity.Patient

// Home Screen UI
@Composable
fun HomeScreen(user: Patient?) {
    // Get the food quality score for the current user
    val foodQualityScore = user?.totalScore

    // Current Context
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hello, ${user?.name}!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Edit the Questionnaire
        Row {
            Text(
                text = "You've already filled in your Food Intake \n" +
                        "Questionnaire, but you can change details here:",
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
            )
            Button(
                onClick = {
                    // Navigate back to the questionnaire screen
                    context.startActivity(Intent(context, Questionnaire::class.java))
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit")
            }
        }

        // Image
        Image(
            painter = painterResource(id = R.drawable.home_image),
            contentDescription = "Home Image",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(250.dp)
                .padding(16.dp)
        )

        // Food Quality Score Display
        Row {
            Text(
                text = "Your Food Quality Score: ",
                fontSize = 22.sp,
            )
            Text(
                text = "$foodQualityScore/100",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B7A0B)
            )
        }

        // Horizontal Divider
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        // Explanation Text
        Text(
            text = "What is the Food Quality Score?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )
        Text(
            text = "Your Food Quality Score provides a snapshot of how well " +
                    "your eating patterns align with established food guidelines, " +
                    "helping you identify both strengths and opportunities for improvement in your diet. \n\n" +
                    "This personalised measurement considers various food groups including " +
                    "vegetables, fruits, whole grains, and proteins to give you practical insights " +
                    "for making healthier food choices. ",
            fontSize = 14.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .weight(1f)
        )
    }
}