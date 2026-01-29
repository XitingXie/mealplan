package com.mealplan.domain.model

import java.time.DayOfWeek

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val healthGoal: HealthGoal = HealthGoal.GENERAL_WELLNESS,
    val dietaryRestrictions: List<DietaryRestriction> = emptyList(),
    val cookingSkill: CookingSkill = CookingSkill.BEGINNER,
    val mealPrepDays: List<DayOfWeek> = emptyList(),
    val householdSize: Int = 1,
    val budgetPreference: BudgetPreference = BudgetPreference.MODERATE,
    val onboardingCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class HealthGoal(val displayName: String, val emoji: String, val description: String) {
    LOSE_FAT("Lose Fat", "\uD83D\uDD25", "Burn fat and get leaner"),
    BUILD_MUSCLE("Build Muscle", "\uD83D\uDCAA", "Gain muscle and strength"),
    HEART_HEALTH("Heart Health", "\u2764\uFE0F", "Lower LDL and improve heart health"),
    MORE_ENERGY("More Energy", "\u26A1", "Feel more energized throughout the day"),
    GENERAL_WELLNESS("General Wellness", "\uD83C\uDF4E", "Overall health and balanced nutrition")
}

enum class DietaryRestriction(val displayName: String) {
    NONE("None"),
    VEGETARIAN("Vegetarian"),
    VEGAN("Vegan"),
    GLUTEN_FREE("Gluten-Free"),
    DAIRY_FREE("Dairy-Free"),
    NUT_ALLERGY("Nut Allergy"),
    HALAL("Halal"),
    KOSHER("Kosher")
}

enum class CookingSkill(val displayName: String, val description: String) {
    BEGINNER("Beginner", "Simple recipes, under 30 minutes"),
    INTERMEDIATE("Intermediate", "Moderate complexity"),
    ADVANCED("Advanced", "Complex recipes welcome")
}

enum class BudgetPreference(val displayName: String, val description: String) {
    BUDGET("Budget-friendly", "Affordable ingredients"),
    MODERATE("Moderate", "Balance of quality and cost"),
    PREMIUM("Premium", "High-quality ingredients OK")
}
