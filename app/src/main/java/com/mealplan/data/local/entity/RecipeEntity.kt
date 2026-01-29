package com.mealplan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mealplan.domain.model.*

@Entity(tableName = "recipes")
@TypeConverters(RecipeConverters::class)
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val ingredientsJson: String,
    val instructionsJson: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val calories: Int,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double,
    val sodiumMg: Double,
    val tagsJson: String,
    val difficulty: String,
    val healthGoalsJson: String,
    val dietaryInfoJson: String,
    val mealType: String,
    val substitutionsJson: String
) {
    fun toDomainModel(): Recipe = Recipe(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        ingredients = RecipeConverters.toIngredientList(ingredientsJson),
        instructions = RecipeConverters.toStringList(instructionsJson),
        prepTimeMinutes = prepTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        servings = servings,
        nutrition = NutritionInfo(
            calories = calories,
            proteinGrams = proteinGrams,
            carbsGrams = carbsGrams,
            fatGrams = fatGrams,
            fiberGrams = fiberGrams,
            sodiumMg = sodiumMg
        ),
        tags = RecipeConverters.toStringList(tagsJson),
        difficulty = CookingSkill.valueOf(difficulty),
        healthGoals = RecipeConverters.toHealthGoalList(healthGoalsJson),
        dietaryInfo = RecipeConverters.toDietaryList(dietaryInfoJson),
        mealType = MealType.valueOf(mealType),
        substitutions = RecipeConverters.toStringMap(substitutionsJson)
    )

    companion object {
        fun fromDomainModel(recipe: Recipe): RecipeEntity = RecipeEntity(
            id = recipe.id,
            name = recipe.name,
            description = recipe.description,
            imageUrl = recipe.imageUrl,
            ingredientsJson = RecipeConverters.fromIngredientList(recipe.ingredients),
            instructionsJson = RecipeConverters.fromStringList(recipe.instructions),
            prepTimeMinutes = recipe.prepTimeMinutes,
            cookTimeMinutes = recipe.cookTimeMinutes,
            servings = recipe.servings,
            calories = recipe.nutrition.calories,
            proteinGrams = recipe.nutrition.proteinGrams,
            carbsGrams = recipe.nutrition.carbsGrams,
            fatGrams = recipe.nutrition.fatGrams,
            fiberGrams = recipe.nutrition.fiberGrams,
            sodiumMg = recipe.nutrition.sodiumMg,
            tagsJson = RecipeConverters.fromStringList(recipe.tags),
            difficulty = recipe.difficulty.name,
            healthGoalsJson = RecipeConverters.fromHealthGoalList(recipe.healthGoals),
            dietaryInfoJson = RecipeConverters.fromDietaryList(recipe.dietaryInfo),
            mealType = recipe.mealType.name,
            substitutionsJson = RecipeConverters.fromStringMap(recipe.substitutions)
        )
    }
}

class RecipeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringListType(value: String): List<String> = toStringList(value)

    @TypeConverter
    fun toStringListType(list: List<String>): String = fromStringList(list)

    companion object {
        private val gson = Gson()

        fun toStringList(json: String): List<String> {
            val type = object : TypeToken<List<String>>() {}.type
            return gson.fromJson(json, type) ?: emptyList()
        }

        fun fromStringList(list: List<String>): String = gson.toJson(list)

        fun toIngredientList(json: String): List<Ingredient> {
            val type = object : TypeToken<List<Ingredient>>() {}.type
            return gson.fromJson(json, type) ?: emptyList()
        }

        fun fromIngredientList(list: List<Ingredient>): String = gson.toJson(list)

        fun toHealthGoalList(json: String): List<HealthGoal> {
            val type = object : TypeToken<List<String>>() {}.type
            val stringList: List<String> = gson.fromJson(json, type) ?: emptyList()
            return stringList.mapNotNull {
                try { HealthGoal.valueOf(it) } catch (e: Exception) { null }
            }
        }

        fun fromHealthGoalList(list: List<HealthGoal>): String =
            gson.toJson(list.map { it.name })

        fun toDietaryList(json: String): List<DietaryRestriction> {
            val type = object : TypeToken<List<String>>() {}.type
            val stringList: List<String> = gson.fromJson(json, type) ?: emptyList()
            return stringList.mapNotNull {
                try { DietaryRestriction.valueOf(it) } catch (e: Exception) { null }
            }
        }

        fun fromDietaryList(list: List<DietaryRestriction>): String =
            gson.toJson(list.map { it.name })

        fun toStringMap(json: String): Map<String, String> {
            val type = object : TypeToken<Map<String, String>>() {}.type
            return gson.fromJson(json, type) ?: emptyMap()
        }

        fun fromStringMap(map: Map<String, String>): String = gson.toJson(map)
    }
}
