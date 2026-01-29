package com.mealplan.domain.model

import java.time.LocalDate

data class MealCheckIn(
    val id: String = "",
    val userId: String = "",
    val date: LocalDate = LocalDate.now(),
    val mealType: MealType = MealType.LUNCH,
    val status: CheckInStatus = CheckInStatus.FOLLOWED_PLAN,
    val plannedRecipeId: String? = null,
    val actualPhotoUrl: String? = null,
    val analyzedNutrition: NutritionInfo? = null,
    val notes: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class CheckInStatus(val displayName: String) {
    FOLLOWED_PLAN("Followed Plan"),
    UPLOADED_PHOTO("Logged with Photo"),
    SKIPPED("Skipped")
}
