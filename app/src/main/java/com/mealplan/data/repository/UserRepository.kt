package com.mealplan.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mealplan.data.local.dao.UserDao
import com.mealplan.data.local.entity.UserEntity
import com.mealplan.data.local.entity.UserProgressEntity
import com.mealplan.domain.model.UserProfile
import com.mealplan.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    private val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    fun getCurrentUserFlow(): Flow<UserProfile?> {
        return userDao.getCurrentUserFlow().map { it?.toDomainModel() }
    }

    suspend fun getCurrentUser(): UserProfile? {
        return userDao.getCurrentUser()?.toDomainModel()
    }

    suspend fun getUserById(uid: String): UserProfile? {
        return userDao.getUserById(uid)?.toDomainModel()
    }

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val entity = UserEntity.fromDomainModel(profile.copy(updatedAt = System.currentTimeMillis()))
            userDao.insertUser(entity)

            // Sync to Firestore
            withTimeout(10_000) {
                usersCollection.document(profile.uid).set(profile).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val entity = UserEntity.fromDomainModel(profile.copy(updatedAt = System.currentTimeMillis()))
            userDao.updateUser(entity)

            // Sync to Firestore
            withTimeout(10_000) {
                usersCollection.document(profile.uid).set(profile).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun completeOnboarding(profile: UserProfile): Result<Unit> {
        return saveUserProfile(profile.copy(onboardingCompleted = true))
    }

    // User Progress
    fun getUserProgressFlow(): Flow<UserProgress?> {
        return userDao.getUserProgressFlow().map { it?.toDomainModel() }
    }

    suspend fun getUserProgress(): UserProgress? {
        return userDao.getUserProgress()?.toDomainModel()
    }

    suspend fun initializeUserProgress(): Result<Unit> {
        return try {
            val progress = UserProgress(
                journeyStartDate = LocalDate.now(),
                lastActiveDate = LocalDate.now()
            )
            userDao.insertUserProgress(UserProgressEntity.fromDomainModel(progress))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProgress(progress: UserProgress): Result<Unit> {
        return try {
            userDao.updateUserProgress(UserProgressEntity.fromDomainModel(progress))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementHealthyDay(): Result<Unit> {
        return try {
            val current = userDao.getUserProgress() ?: return initializeUserProgress()
            val today = LocalDate.now()
            val lastActive = LocalDate.ofEpochDay(current.lastActiveDateEpochDay)

            val newStreak = if (lastActive.plusDays(1) == today || lastActive == today) {
                current.currentStreak + 1
            } else {
                1 // Streak broken, start fresh
            }

            val updated = current.copy(
                totalHealthyDays = current.totalHealthyDays + 1,
                currentStreak = newStreak,
                longestStreak = maxOf(current.longestStreak, newStreak),
                lastActiveDateEpochDay = today.toEpochDay()
            )
            userDao.updateUserProgress(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementCheckIn(): Result<Unit> {
        return try {
            val current = userDao.getUserProgress() ?: run {
                initializeUserProgress()
                userDao.getUserProgress()!!
            }
            val updated = current.copy(
                totalCheckIns = current.totalCheckIns + 1,
                lastActiveDateEpochDay = LocalDate.now().toEpochDay()
            )
            userDao.updateUserProgress(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementRecipesCompleted(): Result<Unit> {
        return try {
            val current = userDao.getUserProgress() ?: run {
                initializeUserProgress()
                userDao.getUserProgress()!!
            }
            val updated = current.copy(
                recipesCompleted = current.recipesCompleted + 1
            )
            userDao.updateUserProgress(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncFromFirestore(): Result<Unit> {
        return try {
            val uid = currentUserId ?: return Result.failure(Exception("Not logged in"))
            val doc = usersCollection.document(uid).get().await()
            if (doc.exists()) {
                val profile = doc.toObject(UserProfile::class.java)
                if (profile != null) {
                    userDao.insertUser(UserEntity.fromDomainModel(profile))
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
