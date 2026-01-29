package com.mealplan.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mealplan.data.local.dao.CheckInDao
import com.mealplan.data.local.dao.RecipeDao
import com.mealplan.data.local.dao.UserDao
import com.mealplan.data.repository.AuthRepository
import com.mealplan.data.repository.MealPlanRepository
import com.mealplan.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        userDao: UserDao
    ): AuthRepository {
        return AuthRepository(firebaseAuth, userDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepository(userDao, firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun provideMealPlanRepository(
        recipeDao: RecipeDao,
        checkInDao: CheckInDao
    ): MealPlanRepository {
        return MealPlanRepository(recipeDao, checkInDao)
    }
}
