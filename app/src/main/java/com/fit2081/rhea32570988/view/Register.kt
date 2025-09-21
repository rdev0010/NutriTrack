package com.fit2081.rhea32570988.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.rhea32570988.view.ui.theme.Rhea32570988Theme
import com.fit2081.rhea32570988.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

class Register : ComponentActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Rhea32570988Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(registerViewModel, modifier = Modifier.padding(innerPadding) )
                }
            }
        }
    }
}

// Register Screen UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, modifier: Modifier = Modifier) {
    // State variables
    var userID by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Get the registered user IDs from the ViewModel
    val userIDlist by registerViewModel.allPatientIds.collectAsState(initial = emptyList())

    // Current Context
    val context = LocalContext.current

    // Coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()

    Surface (
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFDF2DE)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Heading
            Text(
                text = "Register",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))

            // UserID dropdown field
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = userID,
                    onValueChange = {},
                    label = { Text("User ID") },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userIDlist.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id.toString()) },
                            onClick = {
                                userID = id.toString()
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Phone number input field
            OutlinedTextField(
                value = phoneNo,
                onValueChange = { input ->
                    // Filter out non-digit characters
                    val filtered = input.filter { it.isDigit() }
                    if (filtered != input) {
                        Toast.makeText(context, "Only numbers allowed", Toast.LENGTH_SHORT).show()
                    }
                    phoneNo = filtered
                },
                label = { Text(text = "Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // User name input field
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(text = "Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Password input field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Text
            Text(
                text = "This app is only for pre-registered users. " +
                        "Please enter your ID, phone number and password to claim your account.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        // Check if the all fields have been filled in
                        if ( userID == "" || phoneNo == "" || password == "" || confirmPassword == "" ) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        } else {
                            // check if password and confirmPassword match
                            if (registerViewModel.validateRegistration(userID.toInt(), phoneNo)) {
                                // Check password length
                                if (password.length >= 6) {
                                    // Check if password and confirmPassword match
                                    if (password == confirmPassword) {
                                        // if correct show a toast message
                                        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                                        // Register the patient in the database
                                        registerViewModel.registerPatient(userID.toInt(), userName, password)

                                        // Navigate to Login screen
                                        context.startActivity(Intent(context, Login::class.java))
                                    } else {
                                        // if not show a toast message
                                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // if not show a toast message
                                Toast.makeText(context, "User ID and Phone Number are invalid", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp)
            ) {
                Text("Register", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { context.startActivity(Intent(context, Login::class.java)) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp)
            ) {
                Text("Login", fontSize = 16.sp)
            }
        }
    }
}