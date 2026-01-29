package com.mealplan.data.local.dao

import androidx.room.*
import com.mealplan.data.local.entity.UserEntity
import com.mealplan.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUserById(uid: String): UserEntity?

    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserByIdFlow(uid: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteUserById(uid: String)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?>

    // User Progress
    @Query("SELECT * FROM user_progress LIMIT 1")
    suspend fun getUserProgress(): UserProgressEntity?

    @Query("SELECT * FROM user_progress LIMIT 1")
    fun getUserProgressFlow(): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(progress: UserProgressEntity)

    @Update
    suspend fun updateUserProgress(progress: UserProgressEntity)
}
