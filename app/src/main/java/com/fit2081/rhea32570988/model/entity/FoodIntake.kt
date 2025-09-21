package com.fit2081.rhea32570988.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a food intake questionnaire in the system.
 * This class is used to define the structure of the Food Intake entity in the database.
 */
@Entity(
    tableName = "FoodIntake",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["patientId"], unique = true)]
)
data class FoodIntake(
    /** The unique identifier for the food intake record. */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    /** The ID of the patient associated with this food intake record. */
    val patientId: Int,
    /** The patient's food category - fruits */
    val fruitsChk: Boolean,
    /** The patient's food category - vegetables */
    val vegetablesChk: Boolean,
    /** The patient's food category - grains */
    val grainsChk: Boolean,
    /** The patient's food category - red meat */
    val redMeatChk: Boolean,
    /** The patient's food category - seafood */
    val seafoodChk: Boolean,
    /** The patient's food category - poultry */
    val poultryChk: Boolean,
    /** The patient's food category - fish */
    val fishChk: Boolean,
    /** The patient's food category - eggs */
    val eggsChk: Boolean,
    /** The patient's food category - nuts/seeds */
    val nutsChk: Boolean,
    /** The patient's selected persona */
    val selectedPersona: String,
    /** The eat time of the patient. */
    val eatTime: String,
    /** The sleep time of the patient. */
    val sleepTime: String,
    /** The wake time of the patient. */
    val wakeTime: String
)
