package com.fit2081.rhea32570988.model.repository

import android.content.Context
import com.fit2081.rhea32570988.model.database.NutriTrackDB
import com.fit2081.rhea32570988.model.entity.NutriCoachTips
import kotlinx.coroutines.flow.Flow

class NutriCoachTipsRepository(private val applicationContext: Context) {
    // Get database instance and create repository
    val database = NutriTrackDB.getDatabase(applicationContext)
    val nutriCoachTipsDao = database.nutriCoachTipsDao()

    // Function to insert a single new tip into the database
    suspend fun insertTip(tip: NutriCoachTips) {
        nutriCoachTipsDao.insertTip(tip)
    }

    // Function to get all tips for the specified patient ID from the database as a Flow.
    fun getTipsByPatient(patientId: Int): Flow<List<NutriCoachTips>> {
        return nutriCoachTipsDao.getTipsByPatient(patientId)
    }
}