package com.mealplan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mealplan.data.local.dao.CheckInDao
import com.mealplan.data.local.dao.RecipeDao
import com.mealplan.data.local.dao.UserDao
import com.mealplan.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        RecipeEntity::class,
        CheckInEntity::class,
        WellnessCheckInEntity::class,
        UserProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    RecipeConverters::class,
    CheckInConverters::class
)
abstract class MealPlanDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao
    abstract fun checkInDao(): CheckInDao

    companion object {
        const val DATABASE_NAME = "mealplan_database"
    }
}
