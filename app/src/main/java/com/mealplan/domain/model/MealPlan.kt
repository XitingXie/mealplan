package com.mealplan.domain.model

import java.time.LocalDate

data class WeeklyMealPlan(
    val id: String = "",
    val userId: String = "",
    val weekStartDate: LocalDate = LocalDate.now(),
    val days: List<DailyMealPlan> = emptyList(),
    val generatedAt: Long = System.currentTimeMillis()
)

data class DailyMealPlan(
    val date: LocalDate = LocalDate.now(),
    val breakfast: Recipe? = null,
    val lunch: Recipe? = null,
    val dinner: Recipe? = null,
    val snacks: List<Recipe> = emptyList()
) {
    val allMeals: List<Pair<MealType, Recipe?>>
        get() = listOf(
            MealType.BREAKFAST to breakfast,
            MealType.LUNCH to lunch,
            MealType.DINNER to dinner
        ) + snacks.map { MealType.SNACK to it }

    val completedMeals: Int
        get() = listOfNotNull(breakfast, lunch, dinner).size + snacks.size
}
