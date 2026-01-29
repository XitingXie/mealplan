package com.mealplan.data.repository

import com.mealplan.data.local.dao.CheckInDao
import com.mealplan.data.local.dao.RecipeDao
import com.mealplan.data.local.entity.CheckInEntity
import com.mealplan.data.local.entity.RecipeEntity
import com.mealplan.data.local.entity.WellnessCheckInEntity
import com.mealplan.data.mock.MockRecipeData
import com.mealplan.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealPlanRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val checkInDao: CheckInDao
) {
    // Recipes
    suspend fun getAllRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes().map { it.toDomainModel() }
    }

    fun getAllRecipesFlow(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipesFlow().map { list -> list.map { it.toDomainModel() } }
    }

    suspend fun getRecipeById(id: String): Recipe? {
        return recipeDao.getRecipeById(id)?.toDomainModel()
    }

    fun getRecipeByIdFlow(id: String): Flow<Recipe?> {
        return recipeDao.getRecipeByIdFlow(id).map { it?.toDomainModel() }
    }

    suspend fun getRecipesByMealType(mealType: MealType): List<Recipe> {
        return recipeDao.getRecipesByMealType(mealType.name).map { it.toDomainModel() }
    }

    suspend fun getRecipesByHealthGoal(goal: HealthGoal): List<Recipe> {
        return recipeDao.getRecipesByHealthGoal(goal.name).map { it.toDomainModel() }
    }

    suspend fun getQuickRecipes(maxMinutes: Int = 30): List<Recipe> {
        return recipeDao.getQuickRecipes(maxMinutes).map { it.toDomainModel() }
    }

    suspend fun initializeMockRecipes() {
        val count = recipeDao.getRecipeCount()
        if (count == 0) {
            val recipes = MockRecipeData.getAllRecipes()
            val entities = recipes.map { RecipeEntity.fromDomainModel(it) }
            recipeDao.insertRecipes(entities)
        }
    }

    // Meal Plan Generation
    suspend fun generateWeeklyMealPlan(
        userId: String,
        userProfile: UserProfile,
        startDate: LocalDate = LocalDate.now()
    ): WeeklyMealPlan {
        val allRecipes = getAllRecipes().ifEmpty {
            initializeMockRecipes()
            getAllRecipes()
        }

        // Filter recipes based on user preferences
        val suitableRecipes = filterRecipesForUser(allRecipes, userProfile)

        val days = (0 until 7).map { dayOffset ->
            val date = startDate.plusDays(dayOffset.toLong())
            generateDailyMealPlan(date, suitableRecipes, userProfile)
        }

        return WeeklyMealPlan(
            id = UUID.randomUUID().toString(),
            userId = userId,
            weekStartDate = startDate,
            days = days,
            generatedAt = System.currentTimeMillis()
        )
    }

    private fun filterRecipesForUser(recipes: List<Recipe>, profile: UserProfile): List<Recipe> {
        return recipes.filter { recipe ->
            // Check dietary restrictions
            val meetsRestrictions = if (profile.dietaryRestrictions.contains(DietaryRestriction.NONE) ||
                profile.dietaryRestrictions.isEmpty()) {
                true
            } else {
                profile.dietaryRestrictions.all { restriction ->
                    recipe.dietaryInfo.contains(restriction) || restriction == DietaryRestriction.NONE
                }
            }

            // Check cooking skill - beginners only get beginner recipes
            val meetsSkill = when (profile.cookingSkill) {
                CookingSkill.BEGINNER -> recipe.difficulty == CookingSkill.BEGINNER
                CookingSkill.INTERMEDIATE -> recipe.difficulty != CookingSkill.ADVANCED
                CookingSkill.ADVANCED -> true
            }

            // Check health goal compatibility
            val meetsGoal = recipe.healthGoals.contains(profile.healthGoal) ||
                    recipe.healthGoals.contains(HealthGoal.GENERAL_WELLNESS)

            meetsRestrictions && meetsSkill && meetsGoal
        }
    }

    private fun generateDailyMealPlan(
        date: LocalDate,
        recipes: List<Recipe>,
        profile: UserProfile
    ): DailyMealPlan {
        val breakfastRecipes = recipes.filter { it.mealType == MealType.BREAKFAST }.shuffled()
        val lunchRecipes = recipes.filter { it.mealType == MealType.LUNCH }.shuffled()
        val dinnerRecipes = recipes.filter { it.mealType == MealType.DINNER }.shuffled()
        val snackRecipes = recipes.filter { it.mealType == MealType.SNACK }.shuffled()

        return DailyMealPlan(
            date = date,
            breakfast = breakfastRecipes.firstOrNull(),
            lunch = lunchRecipes.firstOrNull(),
            dinner = dinnerRecipes.firstOrNull(),
            snacks = snackRecipes.take(1)
        )
    }

    suspend fun getEasierAlternative(recipe: Recipe, userProfile: UserProfile): Recipe? {
        val allRecipes = getAllRecipes()
        return allRecipes
            .filter { it.mealType == recipe.mealType }
            .filter { it.difficulty == CookingSkill.BEGINNER }
            .filter { it.id != recipe.id }
            .filter { it.totalTimeMinutes < recipe.totalTimeMinutes }
            .shuffled()
            .firstOrNull()
    }

    // Check-ins
    suspend fun createCheckIn(
        userId: String,
        date: LocalDate,
        mealType: MealType,
        status: CheckInStatus,
        recipeId: String? = null,
        photoUrl: String? = null,
        notes: String? = null
    ): Result<MealCheckIn> {
        return try {
            val checkIn = MealCheckIn(
                id = UUID.randomUUID().toString(),
                userId = userId,
                date = date,
                mealType = mealType,
                status = status,
                plannedRecipeId = recipeId,
                actualPhotoUrl = photoUrl,
                notes = notes,
                timestamp = System.currentTimeMillis()
            )
            checkInDao.insertCheckIn(CheckInEntity.fromDomainModel(checkIn))
            Result.success(checkIn)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCheckInsForDate(userId: String, date: LocalDate): List<MealCheckIn> {
        return checkInDao.getCheckInsForDate(userId, date.toEpochDay())
            .map { it.toDomainModel() }
    }

    fun getCheckInsForDateFlow(userId: String, date: LocalDate): Flow<List<MealCheckIn>> {
        return checkInDao.getCheckInsForDateFlow(userId, date.toEpochDay())
            .map { list -> list.map { it.toDomainModel() } }
    }

    suspend fun getCheckInsForWeek(userId: String, startDate: LocalDate): List<MealCheckIn> {
        val endDate = startDate.plusDays(6)
        return checkInDao.getCheckInsForDateRange(
            userId,
            startDate.toEpochDay(),
            endDate.toEpochDay()
        ).map { it.toDomainModel() }
    }

    suspend fun hasCheckedInMeal(userId: String, date: LocalDate, mealType: MealType): Boolean {
        return checkInDao.getCheckInForMeal(userId, date.toEpochDay(), mealType.name) != null
    }

    // Wellness Check-ins
    suspend fun createWellnessCheckIn(wellness: WellnessCheckIn): Result<Unit> {
        return try {
            checkInDao.insertWellnessCheckIn(WellnessCheckInEntity.fromDomainModel(wellness))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWellnessCheckIns(userId: String): List<WellnessCheckIn> {
        return checkInDao.getWellnessCheckIns(userId).map { it.toDomainModel() }
    }

    fun getWellnessCheckInsFlow(userId: String): Flow<List<WellnessCheckIn>> {
        return checkInDao.getWellnessCheckInsFlow(userId)
            .map { list -> list.map { it.toDomainModel() } }
    }

    suspend fun getLatestWellnessCheckIn(userId: String): WellnessCheckIn? {
        return checkInDao.getLatestWellnessCheckIn(userId)?.toDomainModel()
    }

    // Statistics
    suspend fun getTotalHealthyDays(userId: String): Int {
        return checkInDao.getTotalHealthyDays(userId)
    }

    suspend fun getTotalCheckIns(userId: String): Int {
        return checkInDao.getTotalCheckIns(userId)
    }

    suspend fun getAverageEnergyLevel(userId: String): Double? {
        return checkInDao.getAverageEnergyLevel(userId)
    }

    suspend fun getAverageMood(userId: String): Double? {
        return checkInDao.getAverageMood(userId)
    }
}
