package com.fit2081.rhea32570988.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.rhea32570988.model.dao.FoodIntakeDao
import com.fit2081.rhea32570988.model.dao.NutriCoachTipsDao
import com.fit2081.rhea32570988.model.dao.PatientDao
import com.fit2081.rhea32570988.model.entity.FoodIntake
import com.fit2081.rhea32570988.model.entity.NutriCoachTips
import com.fit2081.rhea32570988.model.entity.Patient

/**
 * Main database class for the application
 *
 * This abstract class provides an interface for accessing the application's database.
 * It uses Room persistence library to handle database operations and defines the database configuration including entities and DAOs.
 */
@Database(
    entities = [Patient::class, FoodIntake::class, NutriCoachTips::class],
    version = 1,
    exportSchema = false
)
abstract class NutriTrackDB: RoomDatabase() {
    /**
     * Abstract method to get the PatientDao instance.
     * @return An instance of PatientDao for accessing patient data.
     */
    abstract fun patientDao(): PatientDao

    /**
     * Abstract method to get the FoodIntakeDao instance.
     * @return An instance of FoodIntakeDao for accessing food intake data.
     */
    abstract fun foodIntakeDao(): FoodIntakeDao

    /**
     * Abstract method to get the NutriCoachTipsDao instance.
     * @return An instance of NutriCoachTipsDao for accessing NutriCoach tips data.
     */
    abstract fun nutriCoachTipsDao(): NutriCoachTipsDao

    /**
     * Companion object to manage database instance using the Singleton pattern.
     * This ensures that only one instance of the database is created and used throughout the application.
     * which is an important consideration for resource management.
     */
    companion object {
        // Volatile annotation ensure the INTSTANCE is always up to date and the same for all execution threads
        @Volatile
        private var INSTANCE: NutriTrackDB? = null

        /**
         * Function to get the singleton instance of the database.
         * If the instance is null, it creates a new instance using Room.databaseBuilder.
         * @param context The application context.
         * @return The singleton instance of NutriTrackDB.
         */
        fun getDatabase(context: Context): NutriTrackDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriTrackDB::class.java,
                    "nutritrack_database"
                ).build()
                // Assign the newly created instance to INSTANCE
                INSTANCE = instance
                instance    // Return the instance
            }
        }
    }
}