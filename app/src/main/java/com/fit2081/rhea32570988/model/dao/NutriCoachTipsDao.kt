package com.fit2081.rhea32570988.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fit2081.rhea32570988.model.entity.NutriCoachTips
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the NutriCoachTips entity.
 * This interface defines methods to access and manipulate NutriCoachTips in the database.
 */
@Dao
interface NutriCoachTipsDao {
    // Insert a single new tip into the database
    @Insert
    suspend fun insertTip(tip: NutriCoachTips)

    // Get all tips for the specified patient ID from the database, ordered by creation time (newest first)
    @Query("SELECT * FROM NutriCoachTips WHERE patientId = :patientId ORDER BY createdAt DESC")
    fun getTipsByPatient(patientId: Int): Flow<List<NutriCoachTips>>
}