package com.fit2081.rhea32570988.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fit2081.rhea32570988.model.entity.FoodIntake

/**
 * Data Access Object (DAO) for the FoodIntake entity.
 * This interface defines methods to access and manipulate food intakes in the database.
 */
@Dao
interface FoodIntakeDao {
    // Insert/Update a single new questionnaire into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionnaire(foodIntake: FoodIntake)

    // Get the questionnaire by the patient ID from the database
    @Query("SELECT * FROM FoodIntake WHERE patientId = :patientId")
    suspend fun getQuestionnaireByPatientID(patientId: Int): FoodIntake?
}