package com.fit2081.rhea32570988.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.rhea32570988.model.entity.FoodIntake
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.model.repository.FoodIntakeRepository
import com.fit2081.rhea32570988.model.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the data operations and logic related to the Questionnaire Activity.\
 * @property application The application instance provided by the Android framework
 */
class QuestionnaireViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance for handling all data operations
    private val foodIntakeRepository: FoodIntakeRepository = FoodIntakeRepository(application.applicationContext)
    private val patientRepository: PatientRepository = PatientRepository(application.applicationContext)

    // Get Current User
    suspend fun getCurrentUser(): Patient {
        return patientRepository.getCurrentPatient()
    }

    // Get Questionnaire by Patient ID
    suspend fun getQuestionnaireByPatientID(patientId: Int): FoodIntake? {
        return foodIntakeRepository.getQuestionnaireByPatientID(patientId)
    }

    // Insert/Update a questionnaire into the database
    fun insertQuestionnaire(patientId: Int,
                            fruitsChk: Boolean,
                            vegetablesChk: Boolean,
                            grainsChk: Boolean,
                            redMeatChk: Boolean,
                            seafoodChk: Boolean,
                            poultryChk: Boolean,
                            fishChk: Boolean,
                            eggsChk: Boolean,
                            nutsChk: Boolean,
                            selectedPersona: String,
                            eatTime: String,
                            sleepTime: String,
                            wakeTime: String) {

        // Create a FoodIntake object from the questionnaire data
        val questionnaire = FoodIntake(
            patientId = patientId,
            fruitsChk = fruitsChk,
            vegetablesChk = vegetablesChk,
            grainsChk = grainsChk,
            redMeatChk = redMeatChk,
            seafoodChk = seafoodChk,
            poultryChk = poultryChk,
            fishChk = fishChk,
            eggsChk = eggsChk,
            nutsChk = nutsChk,
            selectedPersona = selectedPersona,
            eatTime = eatTime,
            sleepTime = sleepTime,
            wakeTime = wakeTime
        )

        viewModelScope.launch(Dispatchers.IO) {
            // Insert the questionnaire into the database
            foodIntakeRepository.insertQuestionnaire(questionnaire)

            // Set the hasCompletedQuestionnaire flag to true for the patient
            val patient = patientRepository.getPatientByID(patientId)
            val updatedPatient = patient.copy( hasCompletedQuestionnaire = true )
            patientRepository.updatePatient(updatedPatient)
        }
    }
}