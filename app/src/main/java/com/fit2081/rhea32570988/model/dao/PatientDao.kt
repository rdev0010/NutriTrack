package com.fit2081.rhea32570988.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fit2081.rhea32570988.model.entity.Patient
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Patient entity.
 * This interface defines methods to access and manipulate patients in the database.
 */
@Dao
interface PatientDao {
    // Insert multiple new patients into the database.
    @Insert
    suspend fun insertMany(patients: List<Patient>)

    // Updates an existing patient in the database.
    @Update
    suspend fun updatePatient(patient: Patient)

    // Get the patient by the ID from the database
    @Query("SELECT * FROM Patient WHERE id = :patientId")
    suspend fun getPatientByID(patientId: Int): Patient

    // Get the current patient ID from the database
    @Query("SELECT id FROM Patient WHERE isLoggedIn = true")
    suspend fun getCurrentPatientID(): Int

    // Get the current patient from the database
    @Query("SELECT * FROM Patient WHERE isLoggedIn = true")
    suspend fun getCurrentPatient(): Patient

    // Get all patient IDs from the database
    @Query("SELECT id FROM Patient")
    fun getAllPatientIds(): Flow<List<Int>>

    // Get all patients from the database, ordered by id
    @Query("SELECT * FROM Patient ORDER BY id ASC")
    fun getAllPatients(): Flow<List<Patient>>

    // Get all the IDs of patients who have registered from the database
    @Query("SELECT id FROM Patient WHERE isRegistered = true")
    fun getRegisteredPatientIds(): Flow<List<Int>>

    // Get all registered patients from the database
    @Query("SELECT * FROM Patient WHERE isRegistered = true")
    fun getRegisteredPatients(): Flow<List<Patient>>

    // Get average HEIFAtotalscoreMale for male patients
    @Query("SELECT AVG(totalScore) FROM Patient WHERE sex = 'Male'")
    suspend fun getAverageMaleScore(): Double

    // Get average HEIFAtotalscoreFemale for female patients
    @Query("SELECT AVG(totalScore) FROM Patient WHERE sex = 'Female'")
    suspend fun getAverageFemaleScore(): Double
}