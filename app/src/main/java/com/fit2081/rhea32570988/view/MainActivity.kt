package com.fit2081.rhea32570988.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.rhea32570988.R
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.view.theme.Rhea32570988Theme
import com.fit2081.rhea32570988.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If this is the first run, load the CSV data into the database
        if (mainViewModel.isFirstRun()) {
            mainViewModel.loadCSVData()
        }

        enableEdgeToEdge()
        setContent {
            Rhea32570988Theme {
                // check if there is a user currently logged in
                var user by remember { mutableStateOf<Patient?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                val context = LocalContext.current

                // Fetch user once on composition
                LaunchedEffect(Unit) {
                    user = mainViewModel.getCurrentUser()

                    if (user != null){
                        if (user?.hasCompletedQuestionnaire == true) {
                            // If user exists and has completed the questionnaire, navigate to Navigation screen
                            context.startActivity(Intent(context, Navigation::class.java))
                        } else {
                            // If user exists but has not completed the questionnaire, navigate to Questionnaire screen
                            context.startActivity(Intent(context, Questionnaire::class.java))
                        }
                    }

                    isLoading = false
                }

                // While loading, show progress
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // If no user is logged in, show the Welcome Screen
                    if (user == null) {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            WelcomeScreen(innerPadding)
                        }
                    }
                }
            }
        }
    }
}

// Welcome Screen UI
@Composable
fun WelcomeScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDF2DE)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "NutriTrack Logo",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(250.dp)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))

            // Disclaimer Text
            Text(
                text = "This app provides general health and nutrition information for educational purposes only. " +
                        "It is not intended as medical advice, diagnosis, or treatment. " +
                        "Always consult a qualified healthcare professional before making any changes to your diet, exercise,or health regimen.\n" +
                        "Use this app at your own risk.\n" +
                        "If you’d like to an Accredited Practicing Dietitian (APD), " +
                        "please visit the Monash Nutrition/Dietetics Clinic \n(discounted rates for students):\n" +
                        "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition ",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))

            // Button to go to Login Screen
            Button(
                onClick = { context.startActivity(Intent(context, Login::class.java)) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp)
            ) {
                Text("Login", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(110.dp))

            // Credits Text
            Text(
                text = "Designed by: Rhea Devasia (32570988)",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}