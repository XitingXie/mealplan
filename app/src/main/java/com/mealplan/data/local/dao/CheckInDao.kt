package com.mealplan.data.local.dao

import androidx.room.*
import com.mealplan.data.local.entity.CheckInEntity
import com.mealplan.data.local.entity.WellnessCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    // Meal Check-ins
    @Query("SELECT * FROM check_ins WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getCheckInsByUser(userId: String): List<CheckInEntity>

    @Query("SELECT * FROM check_ins WHERE userId = :userId ORDER BY timestamp DESC")
    fun getCheckInsByUserFlow(userId: String): Flow<List<CheckInEntity>>

    @Query("SELECT * FROM check_ins WHERE userId = :userId AND dateEpochDay = :dateEpochDay")
    suspend fun getCheckInsForDate(userId: String, dateEpochDay: Long): List<CheckInEntity>

    @Query("SELECT * FROM check_ins WHERE userId = :userId AND dateEpochDay = :dateEpochDay")
    fun getCheckInsForDateFlow(userId: String, dateEpochDay: Long): Flow<List<CheckInEntity>>

    @Query("""
        SELECT * FROM check_ins
        WHERE userId = :userId
        AND dateEpochDay BETWEEN :startEpochDay AND :endEpochDay
        ORDER BY dateEpochDay ASC, timestamp ASC
    """)
    suspend fun getCheckInsForDateRange(userId: String, startEpochDay: Long, endEpochDay: Long): List<CheckInEntity>

    @Query("""
        SELECT * FROM check_ins
        WHERE userId = :userId
        AND dateEpochDay = :dateEpochDay
        AND mealType = :mealType
    """)
    suspend fun getCheckInForMeal(userId: String, dateEpochDay: Long, mealType: String): CheckInEntity?

    @Query("SELECT COUNT(DISTINCT dateEpochDay) FROM check_ins WHERE userId = :userId")
    suspend fun getTotalHealthyDays(userId: String): Int

    @Query("SELECT COUNT(*) FROM check_ins WHERE userId = :userId")
    suspend fun getTotalCheckIns(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Update
    suspend fun updateCheckIn(checkIn: CheckInEntity)

    @Delete
    suspend fun deleteCheckIn(checkIn: CheckInEntity)

    @Query("DELETE FROM check_ins WHERE id = :id")
    suspend fun deleteCheckInById(id: String)

    // Wellness Check-ins
    @Query("SELECT * FROM wellness_check_ins WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getWellnessCheckIns(userId: String): List<WellnessCheckInEntity>

    @Query("SELECT * FROM wellness_check_ins WHERE userId = :userId ORDER BY timestamp DESC")
    fun getWellnessCheckInsFlow(userId: String): Flow<List<WellnessCheckInEntity>>

    @Query("SELECT * FROM wellness_check_ins WHERE userId = :userId AND weekNumber = :weekNumber")
    suspend fun getWellnessCheckInForWeek(userId: String, weekNumber: Int): WellnessCheckInEntity?

    @Query("SELECT * FROM wellness_check_ins WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestWellnessCheckIn(userId: String): WellnessCheckInEntity?

    @Query("SELECT AVG(energyLevel) FROM wellness_check_ins WHERE userId = :userId")
    suspend fun getAverageEnergyLevel(userId: String): Double?

    @Query("SELECT AVG(overallMood) FROM wellness_check_ins WHERE userId = :userId")
    suspend fun getAverageMood(userId: String): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellnessCheckIn(wellness: WellnessCheckInEntity)

    @Update
    suspend fun updateWellnessCheckIn(wellness: WellnessCheckInEntity)

    @Delete
    suspend fun deleteWellnessCheckIn(wellness: WellnessCheckInEntity)
}
