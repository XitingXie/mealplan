package com.mealplan.domain.model

data class Recipe(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<String> = emptyList(),
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 1,
    val nutrition: NutritionInfo = NutritionInfo(),
    val tags: List<String> = emptyList(),
    val difficulty: CookingSkill = CookingSkill.BEGINNER,
    val healthGoals: List<HealthGoal> = emptyList(),
    val dietaryInfo: List<DietaryRestriction> = emptyList(),
    val mealType: MealType = MealType.LUNCH,
    val substitutions: Map<String, String> = emptyMap() // ingredient -> substitute
) {
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes
    val isQuickMeal: Boolean get() = totalTimeMinutes <= 30
    val isEasyMeal: Boolean get() = difficulty == CookingSkill.BEGINNER && ingredients.size <= 8
}

data class Ingredient(
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = ""
) {
    val displayString: String
        get() = if (amount > 0) {
            val amountStr = if (amount == amount.toLong().toDouble()) {
                amount.toLong().toString()
            } else {
                String.format("%.1f", amount)
            }
            "$amountStr $unit $name".trim()
        } else {
            name
        }
}

data class NutritionInfo(
    val calories: Int = 0,
    val proteinGrams: Double = 0.0,
    val carbsGrams: Double = 0.0,
    val fatGrams: Double = 0.0,
    val fiberGrams: Double = 0.0,
    val sodiumMg: Double = 0.0
)

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack")
}
