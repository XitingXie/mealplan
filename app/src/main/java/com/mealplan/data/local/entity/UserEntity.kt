package com.mealplan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mealplan.domain.model.*
import java.time.DayOfWeek

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val displayName: String,
    val healthGoal: String,
    val dietaryRestrictions: String, // JSON list
    val cookingSkill: String,
    val mealPrepDays: String, // JSON list
    val householdSize: Int,
    val budgetPreference: String,
    val onboardingCompleted: Boolean,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomainModel(): UserProfile = UserProfile(
        uid = uid,
        email = email,
        displayName = displayName,
        healthGoal = HealthGoal.valueOf(healthGoal),
        dietaryRestrictions = UserConverters.toDietaryRestrictionList(dietaryRestrictions),
        cookingSkill = CookingSkill.valueOf(cookingSkill),
        mealPrepDays = UserConverters.toDayOfWeekList(mealPrepDays),
        householdSize = householdSize,
        budgetPreference = BudgetPreference.valueOf(budgetPreference),
        onboardingCompleted = onboardingCompleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomainModel(user: UserProfile): UserEntity = UserEntity(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            healthGoal = user.healthGoal.name,
            dietaryRestrictions = UserConverters.fromDietaryRestrictionList(user.dietaryRestrictions),
            cookingSkill = user.cookingSkill.name,
            mealPrepDays = UserConverters.fromDayOfWeekList(user.mealPrepDays),
            householdSize = user.householdSize,
            budgetPreference = user.budgetPreference.name,
            onboardingCompleted = user.onboardingCompleted,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}

class UserConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    companion object {
        private val gson = Gson()

        fun toDietaryRestrictionList(json: String): List<DietaryRestriction> {
            val listType = object : TypeToken<List<String>>() {}.type
            val stringList: List<String> = gson.fromJson(json, listType) ?: emptyList()
            return stringList.mapNotNull {
                try { DietaryRestriction.valueOf(it) } catch (e: Exception) { null }
            }
        }

        fun fromDietaryRestrictionList(list: List<DietaryRestriction>): String {
            return gson.toJson(list.map { it.name })
        }

        fun toDayOfWeekList(json: String): List<DayOfWeek> {
            val listType = object : TypeToken<List<String>>() {}.type
            val stringList: List<String> = gson.fromJson(json, listType) ?: emptyList()
            return stringList.mapNotNull {
                try { DayOfWeek.valueOf(it) } catch (e: Exception) { null }
            }
        }

        fun fromDayOfWeekList(list: List<DayOfWeek>): String {
            return gson.toJson(list.map { it.name })
        }
    }
}
