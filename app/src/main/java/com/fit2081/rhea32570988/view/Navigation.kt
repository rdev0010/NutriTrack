package com.fit2081.rhea32570988.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fit2081.rhea32570988.R
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.view.theme.Rhea32570988Theme
import com.fit2081.rhea32570988.viewmodel.NavigationViewModel

class Navigation : ComponentActivity() {
    private val navigationViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Rhea32570988Theme {
                // Initialise the NavHostController for managing the navigation within the app
                val navController: NavHostController = rememberNavController()

                // Get the current user ID from the NavigationViewModel
                var userID by remember { mutableStateOf(0) }
                LaunchedEffect(Unit) {
                    userID = navigationViewModel.getCurrentUserID()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomBar(navController, userID) }
                ) { innerPadding ->
                    Column( modifier = Modifier.padding(innerPadding)) {
                        // Calls the MyNavHost composable to define the navigation graph
                        MyNavHost(navigationViewModel, navController, innerPadding, userID)
                    }
                }
            }
        }
    }
}

@Composable
fun MyNavHost(navigationViewModel: NavigationViewModel, navController: NavHostController, innerPadding: PaddingValues, userID: Int) {
    // Get the current user  from the NavigationViewModel
    var user by remember { mutableStateOf<Patient?>(null) }
    LaunchedEffect(Unit) {
        user = navigationViewModel.getCurrentUser()
    }

    // NavHost composable to define the navigation graph
    NavHost(
        // Use the provided NavHostController
        navController = navController,
        // Set the starting destination to "home"
        startDestination = "Home/$userID"
    ) {
        // Define the composables for the routes
        composable("Home/{userID}") { backStackEntry ->
            HomeScreen(user)
        }
        composable("Insights/{userID}") { backStackEntry ->
            InsightsScreen(user, navController)
        }
        composable("NutriCoach/{userID}") { backStackEntry ->
            NutriCoachScreen(user, navigationViewModel)
        }
        composable("Settings/{userID}") { backStackEntry ->
            SettingsScreen(user, navController, navigationViewModel)
        }
        composable("Settings/ClinicianLogin/{userID}") { backStackEntry ->
            ClinicianLoginScreen(user, navController, navigationViewModel)
        }
        composable("Settings/ClinicianDashboard/{userID}") { backStackEntry ->
            ClinicianDashboardScreen(user, navController, navigationViewModel)
        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController, userID: Int) {
    // List of navigation items
    val items = listOf(
        "Home",
        "Insights",
        "NutriCoach",
        "Settings"
    )

    // Listen to current backstack entry and extract route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    // Determine which item is selected based on the current destination
    val selectedItem = items.indexOfFirst { item ->
        currentDestination?.startsWith(item) == true
    }

    // NavigationBar composable to define the bottom navigation bar
    NavigationBar {
        // Iterate through each item in the 'items' list along with its index
        items.forEachIndexed { index, item ->
            // NavigationBarItem for each item in the list
            NavigationBarItem(
                // Define the icon based on the item's name
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Insights" -> Icon(painter = painterResource(id = R.drawable.insights), contentDescription = "Insights")
                        "NutriCoach" -> Icon(painter = painterResource(id = R.drawable.nutricoach), contentDescription = "NutriCoach")
                        "Settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                // Display the item's name as the label
                label = { Text(item) },
                // Determine if this item is currently selected
                selected = selectedItem == index,
                // Actions to perform when this item is clicked
                onClick = {
                    // Navigate to the corresponding screen based on the item's name
                    navController.navigate("$item/$userID") {
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}