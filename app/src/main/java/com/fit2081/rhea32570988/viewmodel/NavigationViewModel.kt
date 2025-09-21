package com.fit2081.rhea32570988.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.rhea32570988.BuildConfig
import com.fit2081.rhea32570988.model.entity.Fruit
import com.fit2081.rhea32570988.model.entity.NutriCoachTips
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.model.repository.FoodIntakeRepository
import com.fit2081.rhea32570988.model.repository.FruityViceRepository
import com.fit2081.rhea32570988.model.repository.NutriCoachTipsRepository
import com.fit2081.rhea32570988.model.repository.PatientRepository
import com.fit2081.rhea32570988.util.toPromptString
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NavigationViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance for handling all data operations
    private val patientRepository: PatientRepository = PatientRepository(application.applicationContext)
    private val foodIntakeRepository: FoodIntakeRepository = FoodIntakeRepository(application.applicationContext)
    private val nutriCoachTipsRepository: NutriCoachTipsRepository = NutriCoachTipsRepository(application.applicationContext)
    private val fruityViceRepository: FruityViceRepository = FruityViceRepository(application.applicationContext)

    // Get the ui state for tips and pattern generation
    private val _tipUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val tipUiState: StateFlow<UiState> = _tipUiState.asStateFlow()
    private val _patternUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val patternUiState: StateFlow<UiState> = _patternUiState.asStateFlow()

    // Generative model instance for AI interactions
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey= BuildConfig.apiKey
    )

    // Admin key for validation
    companion object {
        private const val ADMIN_KEY = "dollar-entry-apples"
    }

    // Function to validate the admin key
    fun isAdminKeyValid(input: String): Boolean {
        return input.trim() == ADMIN_KEY
    }

    // Get Current User
    suspend fun getCurrentUser(): Patient {
        return patientRepository.getCurrentPatient()
    }

    // Get Current User ID
    suspend fun getCurrentUserID(): Int {
        return patientRepository.getCurrentPatientID()
    }

    // Get the fruit details from FruityVice API
    suspend fun getFruitDetails(fruitName: String): Fruit {
        return fruityViceRepository.getFruitDetails(fruitName)
    }

    // Insert a new tip into the database
    fun insertTip(tip: String, patientId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Get the current date-time in the specified format
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val now = LocalDateTime.now()
            val formatDateTime = now.format(formatter)

            // Create a new NutriCoachTips object with the provided tip and current date-time
            val newTip = NutriCoachTips(patientId = patientId, content = tip, createdAt = formatDateTime)

            // Insert the new tip into the database
            nutriCoachTipsRepository.insertTip(newTip)
        }
    }

    // Get all tips for the specified patient ID from the database
    fun getTipsByPatient(patientId: Int): Flow<List<NutriCoachTips>> {
        return nutriCoachTipsRepository.getTipsByPatient(patientId)
    }

    // Get the average male score
    suspend fun getAverageMaleScore(): Double {
        return patientRepository.getAverageMaleScore()
    }

    // Get the average female score
    suspend fun getAverageFemaleScore(): Double {
        return patientRepository.getAverageFemaleScore()
    }

    // Function to send a prompt to the generative AI model and update the UI state
    fun generateTip() {
        _tipUiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val patient = patientRepository.getCurrentPatient()
                val intake = foodIntakeRepository.getQuestionnaireByPatientID(patient.id)

                val prompt = buildString {
                    appendLine(patient.toPromptString())
                    intake?.let {
                        appendLine()
                        appendLine(it.toPromptString())
                    }
                    appendLine()
                    appendLine("Now generate a (2-3 lines) short, helpful, fun, and motivational food tip or short encouraging message specifically to help this person improve their fruit intake.")
                }

                // Generate content using the generative model
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )

                // Update the UI state with the generate content if successful
                response.text?.let { outputContent ->
                    _tipUiState.value = UiState.Success(listOf(outputContent))
                }
            } catch (e: Exception) {
                // Update the UI state with an error message if an exception occurs
                _tipUiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    // Function to send patients data to AI and get patterns
    fun findDataPatterns() {
        _patternUiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Get the current snapshot of all patients
                val patientsList = patientRepository.getAllPatients().first()

                // Build prompt with your patient data
                val prompt = """
                    Analyze the following patient dataset and identify ONLY 3 key patterns or insights:

                    ${patientsList.toPromptString()}
                    
                    Please list the patterns and the explanations clearly and concisely. 
                    Please return each pattern on a new line, starting with a special delimiter like '###'. For example:
    
                    ### Pattern 1
                    ### Pattern 2
                    ### Pattern 3
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )

                val aiOutput = response.text ?: throw Exception("No response from GenAI")

                val patternList = aiOutput
                    .split("###")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                _patternUiState.value = UiState.Success(patternList)
            } catch (e: Exception) {
                // Update the UI state with an error message if an exception occurs
                _patternUiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    // Set the isLoggedIn flag to false
    fun logout(patientId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val patient = patientRepository.getPatientByID(patientId)
            val updatedPatient = patient.copy( isLoggedIn = false )
            patientRepository.updatePatient(updatedPatient)
        }
    }
}
