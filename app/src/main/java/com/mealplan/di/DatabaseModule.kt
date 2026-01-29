package com.mealplan.di

import android.content.Context
import androidx.room.Room
import com.mealplan.data.local.MealPlanDatabase
import com.mealplan.data.local.dao.CheckInDao
import com.mealplan.data.local.dao.RecipeDao
import com.mealplan.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMealPlanDatabase(@ApplicationContext context: Context): MealPlanDatabase {
        return Room.databaseBuilder(
            context,
            MealPlanDatabase::class.java,
            MealPlanDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: MealPlanDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(database: MealPlanDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    @Singleton
    fun provideCheckInDao(database: MealPlanDatabase): CheckInDao {
        return database.checkInDao()
    }
}
