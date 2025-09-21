package com.fit2081.rhea32570988.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.rhea32570988.model.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the data operations and logic related to the Register Activity.\
 * @property application The application instance provided by the Android framework
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance for handling all data operations
    private val patientRepository: PatientRepository = PatientRepository(application.applicationContext)

    // Get all patient IDs in the database
    val allPatientIds: Flow<List<Int>> = patientRepository.getAllPatientIds()

    // Check whether a patient is registered by their ID
    suspend fun validateRegistration(patientId: Int, phoneNo: String): Boolean {
        val patient = patientRepository.getPatientByID(patientId)
        return patient.phoneNo == phoneNo
    }

    // Register a patient by updating their registration status in the database
    fun registerPatient(patientId: Int, patientName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val patient = patientRepository.getPatientByID(patientId)
            val updatedPatient = patient.copy(
                isRegistered = true,
                name = patientName,
                password = password
            )
            patientRepository.updatePatient(updatedPatient)
        }
    }
}