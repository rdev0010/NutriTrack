package com.fit2081.rhea32570988.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a patient in the system.
 * This class is used to define the structure of the Patient entity in the database.
 */
@Entity(tableName = "Patient")
data class Patient(
    /** The unique identifier for the patient. */
    @PrimaryKey
    val id: Int = 0,
    /** The name of the patient. */
    val name: String,
    /** The phone number of the patient. */
    val phoneNo: String,
    /** The password of the patient. */
    val password: String,
    /** The registered status of the patient. */
    val isRegistered: Boolean,
    /** The logged in status of the patient. */
    val hasCompletedQuestionnaire: Boolean,
    /** The logged in status of the patient. */
    val isLoggedIn: Boolean,
    /** The sex of the patient */
    val sex: String,
    /** The HEIFA Total Score of the patient. */
    val totalScore: Double,
    /** The HEIFA Vegetables Score of the patient. */
    val vegetableScore: Double,
    /** The HEIFA Fruits Score of the patient. */
    val fruitScore: Double,
    /** The HEIFA Grains & Cereals Score of the patient. */
    val grainsCerealScore: Double,
    /** The HEIFA Whole Grains Score of the patient. */
    val wholeGrainsScore: Double,
    /** The HEIFA Meat & Alternatives Score of the patient. */
    val meatScore: Double,
    /** The HEIFA Dairy Score of the patient. */
    val dairyScore: Double,
    /** The HEIFA Water Score of the patient. */
    val waterScore: Double,
    /** The HEIFA Saturated Fats Score of the patient. */
    val saturatedFatsScore: Double,
    /** The HEIFA Unsaturated Fats Score of the patient. */
    val unsaturatedFatsScore: Double,
    /** The HEIFA Sodium Score of the patient. */
    val sodiumScore: Double,
    /** The HEIFA Sugar Score of the patient. */
    val sugarScore: Double,
    /** The HEIFA Alcohol Score of the patient. */
    val alcoholScore: Double,
    /** The HEIFA Discretionary Foods Score of the patient. */
    val discretionaryScore: Double
)
