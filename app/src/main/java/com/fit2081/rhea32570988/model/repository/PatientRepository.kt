package com.fit2081.rhea32570988.model.repository

import android.content.Context
import com.fit2081.rhea32570988.model.database.NutriTrackDB
import com.fit2081.rhea32570988.model.entity.Patient
import kotlinx.coroutines.flow.Flow

class PatientRepository(private val applicationContext: Context) {
    // Get database instance and create repository
    val database = NutriTrackDB.getDatabase(applicationContext)
    val patientDao = database.patientDao()

    // Function to insert a list of many patients into the database.
    suspend fun insertMany(patients: List<Patient>) {
        patientDao.insertMany(patients)
    }

    // Function to update a patient in the database.
    suspend fun updatePatient(patient: Patient) {
        patientDao.updatePatient(patient)
    }

    // Function to get the patient by the ID from the database
    suspend fun getPatientByID(patientId: Int): Patient {
        return patientDao.getPatientByID(patientId)
    }

    // Function to get the current patient ID from the database
    suspend fun getCurrentPatientID(): Int {
        return patientDao.getCurrentPatientID()
    }

    // Function to get the current patient from the database
    suspend fun getCurrentPatient(): Patient {
        return patientDao.getCurrentPatient()
    }

    // Function to get all patient IDs from the database as a Flow.
    fun getAllPatientIds(): Flow<List<Int>> = patientDao.getAllPatientIds()

    // Function to get all patients from the database as a Flow.
    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    // Function to get all IDs of patients who have registered from the database as a Flow.
    fun getRegisteredPatientIds(): Flow<List<Int>> = patientDao.getRegisteredPatientIds()

    // Function to get all registered patients from the database as a Flow.
    fun getRegisteredPatients(): Flow<List<Patient>> = patientDao.getRegisteredPatients()

    // Function to get the average male score
    suspend fun getAverageMaleScore(): Double {
        return patientDao.getAverageMaleScore()
    }

    // Function to get the average female score
    suspend fun getAverageFemaleScore(): Double {
        return patientDao.getAverageFemaleScore()
    }

}