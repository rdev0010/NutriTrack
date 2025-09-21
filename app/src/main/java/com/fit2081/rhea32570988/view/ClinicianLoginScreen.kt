package com.fit2081.rhea32570988.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun ClinicianLoginScreen(
    user: Patient?,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
    var clinicianKey by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            text = "Clinician Login",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Clinician key input field
        OutlinedTextField(
            value = clinicianKey,
            onValueChange = { clinicianKey = it },
            label = { Text(text = "Clinician Key") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                if (clinicianKey.isNotEmpty()) {
                    // Validate clinician key
                    if (navigationViewModel.isAdminKeyValid(clinicianKey)) {
                        // Navigate to Clinician Dashboard
                        navController.navigate("Settings/ClinicianDashboard/${user?.id}")
                    } else {
                        // if incorrect show a toast message
                        Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle empty clinician key case
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp)
        ) {
            Text("Clinician Login", fontSize = 16.sp)
        }
    }
}