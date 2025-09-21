package com.fit2081.rhea32570988.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.model.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the data operations and logic related to the Login Activity.\
 * @property application The application instance provided by the Android framework
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance for handling all data operations
    private val patientRepository: PatientRepository = PatientRepository(application.applicationContext)

    // Get the IDs of all patients registered in the database
    val allRegisteredPatientIds: Flow<List<Int>> = patientRepository.getRegisteredPatientIds()

    // Validate login credentials
    suspend fun validateLogin(patientId: Int, password: String): Boolean {
        val patient = patientRepository.getPatientByID(patientId)
        return patient.password == password
    }

    // Get the patient by id
    suspend fun getPatientById(patientId: Int): Patient {
        return patientRepository.getPatientByID(patientId)
    }

    // Set the isLoggedIn flag to true
    fun login(patientId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val patient = patientRepository.getPatientByID(patientId)
            val updatedPatient = patient.copy( isLoggedIn = true )
            patientRepository.updatePatient(updatedPatient)
        }
    }
}