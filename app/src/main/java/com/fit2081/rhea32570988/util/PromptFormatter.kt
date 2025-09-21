package com.fit2081.rhea32570988.util

import com.fit2081.rhea32570988.model.entity.FoodIntake
import com.fit2081.rhea32570988.model.entity.Patient

// Function to format Patient data as a prompt string
fun Patient.toPromptString(): String = """
    Patient Overview:
    Name: $name
    Sex: $sex
    Total HEIFA Score: $totalScore
    Vegetable Score: $vegetableScore
    Fruit Score: $fruitScore
    Grains & Cereal Score: $grainsCerealScore
    Whole Grains Score: $wholeGrainsScore
    Meat & Alternatives Score: $meatScore
    Dairy Score: $dairyScore
    Water Score: $waterScore
    Saturated Fats Score: $saturatedFatsScore
    Unsaturated Fats Score: $unsaturatedFatsScore
    Sodium Score: $sodiumScore
    Sugar Score: $sugarScore
    Alcohol Score: $alcoholScore
    Discretionary Foods Score: $discretionaryScore
""".trimIndent()

// Function to format FoodIntake data as a prompt string
fun FoodIntake.toPromptString(): String = """
    Food Intake Questionnaire:
    Eats Fruits: $fruitsChk
    Eats Vegetables: $vegetablesChk
    Eats Grains: $grainsChk
    Eats Red Meat: $redMeatChk
    Eats Seafood: $seafoodChk
    Eats Poultry: $poultryChk
    Eats Fish: $fishChk
    Eats Eggs: $eggsChk
    Eats Nuts/Seeds: $nutsChk
    Persona Type: $selectedPersona
    Typical Eat Time: $eatTime
    Typical Sleep Time: $sleepTime
    Typical Wake Time: $wakeTime
""".trimIndent()

// Function on List<Patient> to format as prompt string
fun List<Patient>.toPromptString(): String {
    return joinToString(separator = "\n") { patient ->
        patient.toPromptString()
    }
}