package com.mealplan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mealplan.domain.model.*
import java.time.LocalDate

@Entity(tableName = "check_ins")
@TypeConverters(CheckInConverters::class)
data class CheckInEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val dateEpochDay: Long, // LocalDate as epoch day for easy querying
    val mealType: String,
    val status: String,
    val plannedRecipeId: String?,
    val actualPhotoUrl: String?,
    val calories: Int?,
    val proteinGrams: Double?,
    val carbsGrams: Double?,
    val fatGrams: Double?,
    val fiberGrams: Double?,
    val sodiumMg: Double?,
    val notes: String?,
    val timestamp: Long
) {
    fun toDomainModel(): MealCheckIn = MealCheckIn(
        id = id,
        userId = userId,
        date = LocalDate.ofEpochDay(dateEpochDay),
        mealType = MealType.valueOf(mealType),
        status = CheckInStatus.valueOf(status),
        plannedRecipeId = plannedRecipeId,
        actualPhotoUrl = actualPhotoUrl,
        analyzedNutrition = if (calories != null) NutritionInfo(
            calories = calories,
            proteinGrams = proteinGrams ?: 0.0,
            carbsGrams = carbsGrams ?: 0.0,
            fatGrams = fatGrams ?: 0.0,
            fiberGrams = fiberGrams ?: 0.0,
            sodiumMg = sodiumMg ?: 0.0
        ) else null,
        notes = notes,
        timestamp = timestamp
    )

    companion object {
        fun fromDomainModel(checkIn: MealCheckIn): CheckInEntity = CheckInEntity(
            id = checkIn.id,
            userId = checkIn.userId,
            dateEpochDay = checkIn.date.toEpochDay(),
            mealType = checkIn.mealType.name,
            status = checkIn.status.name,
            plannedRecipeId = checkIn.plannedRecipeId,
            actualPhotoUrl = checkIn.actualPhotoUrl,
            calories = checkIn.analyzedNutrition?.calories,
            proteinGrams = checkIn.analyzedNutrition?.proteinGrams,
            carbsGrams = checkIn.analyzedNutrition?.carbsGrams,
            fatGrams = checkIn.analyzedNutrition?.fatGrams,
            fiberGrams = checkIn.analyzedNutrition?.fiberGrams,
            sodiumMg = checkIn.analyzedNutrition?.sodiumMg,
            notes = checkIn.notes,
            timestamp = checkIn.timestamp
        )
    }
}

@Entity(tableName = "wellness_check_ins")
data class WellnessCheckInEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val weekNumber: Int,
    val dateEpochDay: Long,
    val energyLevel: Int,
    val digestionQuality: String,
    val postMealFeeling: String,
    val sleepQuality: Int,
    val receivedCompliments: Boolean,
    val complimentNote: String?,
    val overallMood: Int,
    val timestamp: Long
) {
    fun toDomainModel(): WellnessCheckIn = WellnessCheckIn(
        id = id,
        userId = userId,
        weekNumber = weekNumber,
        date = LocalDate.ofEpochDay(dateEpochDay),
        energyLevel = energyLevel,
        digestionQuality = TrendRating.valueOf(digestionQuality),
        postMealFeeling = PostMealFeeling.valueOf(postMealFeeling),
        sleepQuality = sleepQuality,
        receivedCompliments = receivedCompliments,
        complimentNote = complimentNote,
        overallMood = overallMood,
        timestamp = timestamp
    )

    companion object {
        fun fromDomainModel(wellness: WellnessCheckIn): WellnessCheckInEntity = WellnessCheckInEntity(
            id = wellness.id,
            userId = wellness.userId,
            weekNumber = wellness.weekNumber,
            dateEpochDay = wellness.date.toEpochDay(),
            energyLevel = wellness.energyLevel,
            digestionQuality = wellness.digestionQuality.name,
            postMealFeeling = wellness.postMealFeeling.name,
            sleepQuality = wellness.sleepQuality,
            receivedCompliments = wellness.receivedCompliments,
            complimentNote = wellness.complimentNote,
            overallMood = wellness.overallMood,
            timestamp = wellness.timestamp
        )
    }
}

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val oderId: String = "progress",
    val totalHealthyDays: Int,
    val totalCheckIns: Int,
    val recipesCompleted: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val journeyStartDateEpochDay: Long,
    val lastActiveDateEpochDay: Long
) {
    fun toDomainModel(): UserProgress = UserProgress(
        totalHealthyDays = totalHealthyDays,
        totalCheckIns = totalCheckIns,
        recipesCompleted = recipesCompleted,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        journeyStartDate = LocalDate.ofEpochDay(journeyStartDateEpochDay),
        lastActiveDate = LocalDate.ofEpochDay(lastActiveDateEpochDay)
    )

    companion object {
        fun fromDomainModel(progress: UserProgress): UserProgressEntity = UserProgressEntity(
            totalHealthyDays = progress.totalHealthyDays,
            totalCheckIns = progress.totalCheckIns,
            recipesCompleted = progress.recipesCompleted,
            currentStreak = progress.currentStreak,
            longestStreak = progress.longestStreak,
            journeyStartDateEpochDay = progress.journeyStartDate.toEpochDay(),
            lastActiveDateEpochDay = progress.lastActiveDate.toEpochDay()
        )
    }
}

class CheckInConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)
}
