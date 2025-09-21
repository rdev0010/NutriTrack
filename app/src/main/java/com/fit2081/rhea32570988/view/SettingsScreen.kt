package com.fit2081.rhea32570988.view

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.viewmodel.NavigationViewModel

@Composable
fun SettingsScreen(
    user: Patient?,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
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
            text = "Settings",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Account
        Text(
            text = "ACCOUNT",
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(16.dp))

        // User name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Name", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("${user?.name}")
        }
        Spacer(modifier = Modifier.height(10.dp))

        // User phone number
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Phone, contentDescription = "Phone Number", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("${user?.phoneNo}")
        }
        Spacer(modifier = Modifier.height(10.dp))

        // User ID
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "User ID", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("${user?.id}")
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Section Divider
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        // Other Settings
        Text(
            text = "OTHER SETTINGS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Handle logout action
                    navigationViewModel.logout(user?.id ?: 0)
                    // Navigate to Login screen
                    context.startActivity(Intent(context, Login::class.java))
                }
                .padding(8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Logout", modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Clinician Login
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Navigate to Clinician Login screen
                    navController.navigate("Settings/ClinicianLogin/${user?.id}")
                }
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Clinician Login", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clinician Login")
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Clinician Login", modifier = Modifier.size(16.dp))
        }
    }
}