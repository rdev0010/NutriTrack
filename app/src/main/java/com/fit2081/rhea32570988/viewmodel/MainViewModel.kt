package com.fit2081.rhea32570988.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.model.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * ViewModel for loading CSV into the database on the first run.
 *
 * This class extends AndroidViewModel to have access to the Application context which is required for the repository.
 * It serves as the communication center between the UI (MainActivity) and the data layer (PatientRepository).
 *
 * @property application The application instance provided by the Android framework
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance for handling all data operations
    private val patientRepository: PatientRepository = PatientRepository(application.applicationContext)

    // Get the context from the application
    val context: Context = application.applicationContext

    // Get shared preferences
    val sharedPref = context.getSharedPreferences("NutriTrack_sp", Context.MODE_PRIVATE)

    // Parse userdata and get list of all users/patients
    val patients = getAllPatients(context, "userdata.csv")

    // Check if this is the first run of the app
    fun isFirstRun(): Boolean {
        return sharedPref.getBoolean("is_first_run", true)
    }

    // Load all patients from the CSV into the database
    fun loadCSVData() {
        viewModelScope.launch(Dispatchers.IO) {
            // Insert all patients into the database
            patientRepository.insertMany(patients)
            // Set the first run complete flag in shared preferences
            sharedPref.edit().putBoolean("is_first_run", false).apply()
        }
    }

    // Get current user
    suspend fun getCurrentUser(): Patient {
        return patientRepository.getCurrentPatient()
    }

    // Get all the patients from the CSV
    fun getAllPatients(context: Context, fileName: String): List<Patient> {
        val patients = mutableListOf<Patient>()
        val assets = context.assets // Get the asset manager

        try {
            // Open the file from assets
            val inputStream = assets.open(fileName)
            // Create a buffered reader for efficient reading
            val reader = BufferedReader(InputStreamReader(inputStream))
            // Read the lines from the file
            val lines = reader.readLines()

            // Check if the file is empty
            if (lines.isEmpty()) return emptyList()

            // Split the first line to get the header
            val header = lines.first().split(",")
            val dataLines = lines.drop(1)

            // Iterate through each line of data
            for (line in dataLines) {
                val values = line.split(",")
                if (values.size < header.size) continue

                // Create a map of header to values for easy access
                val userMap = header.zip(values).toMap()

                // Extract necessary fields from the map
                val sex = userMap["Sex"] ?: continue
                val id = userMap["User_ID"]?.toIntOrNull() ?: continue
                val phone = userMap["PhoneNumber"] ?: continue
                val totalScoreLabel = if (sex == "Female") "HEIFAtotalscoreFemale" else "HEIFAtotalscoreMale"
                val totalScore = userMap[totalScoreLabel]?.toDoubleOrNull()?: 0.0

                // Function to get the score for a specific label
                fun getScore(label: String): Double {
                    val columnName = if (sex == "Female") "${label}HEIFAscoreFemale" else "${label}HEIFAscoreMale"
                    return userMap[columnName]?.toDoubleOrNull()?: 0.0
                }

                // Create a Patient object from the data
                val patient = Patient(
                    id = id,
                    name = "",
                    phoneNo = phone,
                    password = "",
                    isRegistered = false,
                    hasCompletedQuestionnaire = false,
                    isLoggedIn = false,
                    sex = sex,
                    totalScore = totalScore,
                    discretionaryScore = getScore("Discretionary"),
                    vegetableScore = getScore("Vegetables"),
                    fruitScore = getScore("Fruit"),
                    grainsCerealScore = getScore("Grainsandcereals"),
                    wholeGrainsScore = getScore("Wholegrains"),
                    meatScore = getScore("Meatandalternatives"),
                    dairyScore = getScore("Dairyandalternatives"),
                    waterScore = getScore("Water"),
                    saturatedFatsScore = getScore("SaturatedFat"),
                    unsaturatedFatsScore = getScore("UnsaturatedFat"),
                    sodiumScore = getScore("Sodium"),
                    sugarScore = getScore("Sugar"),
                    alcoholScore = getScore("Alcohol")
                )

                // Add the patient to the list
                patients.add(patient)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return patients
    }

}