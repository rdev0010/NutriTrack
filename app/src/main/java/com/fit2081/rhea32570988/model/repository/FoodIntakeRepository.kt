package com.fit2081.rhea32570988.model.repository

import android.content.Context
import com.fit2081.rhea32570988.model.database.NutriTrackDB
import com.fit2081.rhea32570988.model.entity.FoodIntake

class FoodIntakeRepository(private val applicationContext: Context) {
    // Get database instance and create repository
    val database = NutriTrackDB.getDatabase(applicationContext)
    val foodIntakeDao = database.foodIntakeDao()

    // Function to insert a questionnaire into the database.
    suspend fun insertQuestionnaire(foodIntake: FoodIntake) {
        foodIntakeDao.insertQuestionnaire(foodIntake)
    }

    // Function to get the questionnaire by the patient ID from the database
    suspend fun getQuestionnaireByPatientID(patientId: Int): FoodIntake? {
        return foodIntakeDao.getQuestionnaireByPatientID(patientId)
    }
}