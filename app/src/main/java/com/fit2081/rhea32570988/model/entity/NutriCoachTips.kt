package com.fit2081.rhea32570988.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an AI generated NutriCoach tip in the system.
 * This class is used to define the structure of the NutriCoachTips entity in the database.
 */
@Entity(
    tableName = "NutriCoachTips",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId")]
)
data class NutriCoachTips(
    /** The unique identifier for the NutriCoach tip. */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    /** The ID of the patient associated with this NutriCoach tip. */
    val patientId: Int,
    /** The content of the NutriCoach tip. */
    val content: String,
    /** The date and time when the NutriCoach tip was created. */
    val createdAt: String
)
