package com.mealplan.data.local.dao

import androidx.room.*
import com.mealplan.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes")
    fun getAllRecipesFlow(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): RecipeEntity?

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeByIdFlow(id: String): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE mealType = :mealType")
    suspend fun getRecipesByMealType(mealType: String): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE difficulty = :difficulty")
    suspend fun getRecipesByDifficulty(difficulty: String): List<RecipeEntity>

    @Query("""
        SELECT * FROM recipes
        WHERE healthGoalsJson LIKE '%' || :healthGoal || '%'
    """)
    suspend fun getRecipesByHealthGoal(healthGoal: String): List<RecipeEntity>

    @Query("""
        SELECT * FROM recipes
        WHERE mealType = :mealType
        AND healthGoalsJson LIKE '%' || :healthGoal || '%'
    """)
    suspend fun getRecipesByMealTypeAndHealthGoal(mealType: String, healthGoal: String): List<RecipeEntity>

    @Query("""
        SELECT * FROM recipes
        WHERE prepTimeMinutes + cookTimeMinutes <= :maxMinutes
    """)
    suspend fun getQuickRecipes(maxMinutes: Int = 30): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes()

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipeCount(): Int
}
