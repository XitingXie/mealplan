package com.mealplan.data.mock

import com.mealplan.domain.model.*

object MockRecipeData {

    fun getAllRecipes(): List<Recipe> = breakfastRecipes + lunchRecipes + dinnerRecipes + snackRecipes

    private val breakfastRecipes = listOf(
        // BEGINNER BREAKFASTS (Easy, Quick)
        Recipe(
            id = "b1",
            name = "Greek Yogurt Parfait",
            description = "Creamy yogurt layered with fresh berries and crunchy granola",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Greek yogurt", 1.0, "cup"),
                Ingredient("Mixed berries", 0.5, "cup"),
                Ingredient("Granola", 0.25, "cup"),
                Ingredient("Honey", 1.0, "tbsp")
            ),
            instructions = listOf(
                "Add half the yogurt to a glass or bowl",
                "Layer half the berries on top",
                "Add remaining yogurt",
                "Top with remaining berries and granola",
                "Drizzle with honey"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(320, 18.0, 42.0, 8.0, 4.0, 80.0),
            tags = listOf("quick", "no-cook", "high-protein"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Greek yogurt" to "coconut yogurt", "Honey" to "maple syrup")
        ),
        Recipe(
            id = "b2",
            name = "Avocado Toast",
            description = "Creamy avocado on crispy whole grain toast with a perfect egg",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Whole grain bread", 2.0, "slices"),
                Ingredient("Avocado", 1.0, ""),
                Ingredient("Egg", 1.0, ""),
                Ingredient("Salt", 0.0, "to taste"),
                Ingredient("Red pepper flakes", 0.0, "pinch")
            ),
            instructions = listOf(
                "Toast the bread until golden",
                "Mash the avocado with a fork",
                "Fry or poach the egg to your liking",
                "Spread avocado on toast",
                "Top with egg, salt, and red pepper flakes"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 5,
            servings = 1,
            nutrition = NutritionInfo(380, 14.0, 32.0, 24.0, 10.0, 320.0),
            tags = listOf("quick", "healthy-fats"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.HEART_HEALTH, HealthGoal.MORE_ENERGY, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Whole grain bread" to "gluten-free bread", "Egg" to "cherry tomatoes")
        ),
        Recipe(
            id = "b3",
            name = "Overnight Oats",
            description = "Prep tonight, grab and go tomorrow morning",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Rolled oats", 0.5, "cup"),
                Ingredient("Milk", 0.5, "cup"),
                Ingredient("Greek yogurt", 0.25, "cup"),
                Ingredient("Chia seeds", 1.0, "tbsp"),
                Ingredient("Banana", 0.5, ""),
                Ingredient("Peanut butter", 1.0, "tbsp")
            ),
            instructions = listOf(
                "Combine oats, milk, yogurt, and chia seeds in a jar",
                "Stir well to combine",
                "Refrigerate overnight (or at least 4 hours)",
                "In the morning, top with sliced banana and peanut butter"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(420, 16.0, 58.0, 14.0, 8.0, 150.0),
            tags = listOf("meal-prep", "no-cook", "grab-and-go"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.MORE_ENERGY, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Milk" to "almond milk", "Peanut butter" to "almond butter", "Greek yogurt" to "coconut yogurt")
        ),
        Recipe(
            id = "b4",
            name = "Scrambled Eggs with Spinach",
            description = "Fluffy eggs packed with iron-rich spinach",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Eggs", 3.0, ""),
                Ingredient("Spinach", 1.0, "cup"),
                Ingredient("Butter", 1.0, "tbsp"),
                Ingredient("Salt", 0.0, "to taste"),
                Ingredient("Black pepper", 0.0, "to taste")
            ),
            instructions = listOf(
                "Whisk eggs in a bowl with salt and pepper",
                "Melt butter in a pan over medium-low heat",
                "Add spinach and cook until wilted (1 minute)",
                "Pour in eggs and gently stir with a spatula",
                "Remove from heat when still slightly wet"
            ),
            prepTimeMinutes = 3,
            cookTimeMinutes = 5,
            servings = 1,
            nutrition = NutritionInfo(280, 20.0, 2.0, 22.0, 1.0, 380.0),
            tags = listOf("quick", "high-protein", "low-carb"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.LOSE_FAT, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Butter" to "olive oil", "Spinach" to "kale")
        ),
        Recipe(
            id = "b5",
            name = "Banana Peanut Butter Smoothie",
            description = "Creamy, filling smoothie ready in minutes",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Banana", 1.0, "frozen"),
                Ingredient("Peanut butter", 2.0, "tbsp"),
                Ingredient("Milk", 1.0, "cup"),
                Ingredient("Honey", 1.0, "tsp"),
                Ingredient("Ice cubes", 0.5, "cup")
            ),
            instructions = listOf(
                "Add all ingredients to a blender",
                "Blend until smooth",
                "Pour into a glass and enjoy"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(380, 12.0, 48.0, 16.0, 5.0, 200.0),
            tags = listOf("quick", "no-cook", "grab-and-go"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.MORE_ENERGY),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Milk" to "oat milk", "Peanut butter" to "almond butter")
        ),
        Recipe(
            id = "b6",
            name = "Simple Oatmeal with Berries",
            description = "Warm, comforting oatmeal with fresh berries",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Quick oats", 0.5, "cup"),
                Ingredient("Water", 1.0, "cup"),
                Ingredient("Mixed berries", 0.5, "cup"),
                Ingredient("Honey", 1.0, "tbsp"),
                Ingredient("Cinnamon", 0.0, "pinch")
            ),
            instructions = listOf(
                "Bring water to a boil",
                "Stir in oats and reduce heat",
                "Cook for 2-3 minutes, stirring occasionally",
                "Top with berries, honey, and cinnamon"
            ),
            prepTimeMinutes = 2,
            cookTimeMinutes = 5,
            servings = 1,
            nutrition = NutritionInfo(280, 6.0, 56.0, 4.0, 6.0, 10.0),
            tags = listOf("quick", "heart-healthy", "fiber-rich"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.HEART_HEALTH, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.DAIRY_FREE, DietaryRestriction.VEGAN),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Honey" to "maple syrup", "Quick oats" to "rolled oats")
        ),

        // INTERMEDIATE BREAKFASTS
        Recipe(
            id = "b7",
            name = "Veggie Omelette",
            description = "Fluffy omelette loaded with colorful vegetables",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Eggs", 3.0, ""),
                Ingredient("Bell pepper", 0.25, "cup diced"),
                Ingredient("Onion", 0.25, "cup diced"),
                Ingredient("Mushrooms", 0.25, "cup sliced"),
                Ingredient("Cheese", 0.25, "cup shredded"),
                Ingredient("Butter", 1.0, "tbsp"),
                Ingredient("Salt", 0.0, "to taste")
            ),
            instructions = listOf(
                "Whisk eggs with salt in a bowl",
                "Sauté vegetables in butter until soft",
                "Pour eggs over vegetables",
                "Cook until edges set, then add cheese",
                "Fold omelette in half and serve"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 8,
            servings = 1,
            nutrition = NutritionInfo(380, 24.0, 8.0, 28.0, 2.0, 520.0),
            tags = listOf("high-protein", "low-carb"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.LOSE_FAT, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Cheese" to "nutritional yeast", "Butter" to "olive oil")
        ),
        Recipe(
            id = "b8",
            name = "Protein Pancakes",
            description = "Fluffy pancakes with extra protein for muscle building",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Oats", 0.5, "cup"),
                Ingredient("Banana", 1.0, ""),
                Ingredient("Eggs", 2.0, ""),
                Ingredient("Protein powder", 1.0, "scoop"),
                Ingredient("Baking powder", 0.5, "tsp"),
                Ingredient("Maple syrup", 2.0, "tbsp")
            ),
            instructions = listOf(
                "Blend oats, banana, eggs, protein powder, and baking powder",
                "Heat a non-stick pan over medium heat",
                "Pour batter to form pancakes",
                "Flip when bubbles form on surface",
                "Serve with maple syrup"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 15,
            servings = 2,
            nutrition = NutritionInfo(420, 28.0, 52.0, 12.0, 6.0, 280.0),
            tags = listOf("high-protein", "meal-prep-friendly"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN),
            mealType = MealType.BREAKFAST,
            substitutions = mapOf("Protein powder" to "extra oats", "Maple syrup" to "fresh fruit")
        )
    )

    private val lunchRecipes = listOf(
        // BEGINNER LUNCHES
        Recipe(
            id = "l1",
            name = "Chicken Caesar Salad",
            description = "Classic salad with grilled chicken and creamy dressing",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Romaine lettuce", 3.0, "cups"),
                Ingredient("Grilled chicken breast", 4.0, "oz"),
                Ingredient("Parmesan cheese", 2.0, "tbsp"),
                Ingredient("Caesar dressing", 2.0, "tbsp"),
                Ingredient("Croutons", 0.25, "cup")
            ),
            instructions = listOf(
                "Chop lettuce and place in a bowl",
                "Slice grilled chicken",
                "Top lettuce with chicken, parmesan, and croutons",
                "Drizzle with Caesar dressing",
                "Toss gently and serve"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(420, 38.0, 18.0, 22.0, 4.0, 680.0),
            tags = listOf("high-protein", "classic"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.LOSE_FAT),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Croutons" to "chickpeas", "Caesar dressing" to "olive oil and lemon")
        ),
        Recipe(
            id = "l2",
            name = "Turkey & Avocado Wrap",
            description = "Protein-packed wrap with creamy avocado",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Whole wheat tortilla", 1.0, "large"),
                Ingredient("Turkey breast slices", 4.0, "oz"),
                Ingredient("Avocado", 0.5, ""),
                Ingredient("Lettuce", 2.0, "leaves"),
                Ingredient("Tomato", 2.0, "slices"),
                Ingredient("Mustard", 1.0, "tbsp")
            ),
            instructions = listOf(
                "Lay tortilla flat",
                "Spread mashed avocado and mustard",
                "Layer turkey, lettuce, and tomato",
                "Roll tightly, tucking in the sides",
                "Cut in half diagonally"
            ),
            prepTimeMinutes = 8,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(380, 32.0, 28.0, 16.0, 8.0, 520.0),
            tags = listOf("high-protein", "portable", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.LOSE_FAT, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.DAIRY_FREE),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Whole wheat tortilla" to "lettuce wraps", "Turkey" to "chicken breast")
        ),
        Recipe(
            id = "l3",
            name = "Mediterranean Quinoa Bowl",
            description = "Colorful bowl with quinoa, veggies, and feta",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Cooked quinoa", 1.0, "cup"),
                Ingredient("Cucumber", 0.5, "cup diced"),
                Ingredient("Cherry tomatoes", 0.5, "cup halved"),
                Ingredient("Feta cheese", 2.0, "tbsp crumbled"),
                Ingredient("Kalamata olives", 6.0, ""),
                Ingredient("Olive oil", 1.0, "tbsp"),
                Ingredient("Lemon juice", 1.0, "tbsp")
            ),
            instructions = listOf(
                "Place quinoa in a bowl",
                "Top with cucumber, tomatoes, feta, and olives",
                "Drizzle with olive oil and lemon juice",
                "Toss gently and serve"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(380, 12.0, 42.0, 18.0, 6.0, 420.0),
            tags = listOf("vegetarian", "mediterranean", "heart-healthy"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.HEART_HEALTH, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Feta" to "tofu crumbles", "Quinoa" to "brown rice")
        ),
        Recipe(
            id = "l4",
            name = "Tuna Salad Lettuce Wraps",
            description = "Light, protein-rich lunch with no bread",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Canned tuna", 5.0, "oz drained"),
                Ingredient("Greek yogurt", 2.0, "tbsp"),
                Ingredient("Celery", 0.25, "cup diced"),
                Ingredient("Red onion", 2.0, "tbsp diced"),
                Ingredient("Butter lettuce", 4.0, "leaves"),
                Ingredient("Lemon juice", 1.0, "tsp")
            ),
            instructions = listOf(
                "Mix tuna, yogurt, celery, onion, and lemon juice",
                "Season with salt and pepper",
                "Spoon mixture into lettuce leaves",
                "Serve immediately"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(220, 32.0, 6.0, 8.0, 1.0, 380.0),
            tags = listOf("low-carb", "high-protein", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.LOSE_FAT, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Greek yogurt" to "mayo", "Tuna" to "canned chicken")
        ),
        Recipe(
            id = "l5",
            name = "Black Bean Quesadilla",
            description = "Cheesy quesadilla with fiber-rich black beans",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Flour tortilla", 1.0, "large"),
                Ingredient("Black beans", 0.5, "cup drained"),
                Ingredient("Shredded cheese", 0.25, "cup"),
                Ingredient("Salsa", 2.0, "tbsp"),
                Ingredient("Sour cream", 1.0, "tbsp")
            ),
            instructions = listOf(
                "Heat tortilla in a pan over medium heat",
                "Spread beans and cheese on half the tortilla",
                "Fold tortilla in half",
                "Cook 2 minutes per side until golden and cheese melts",
                "Serve with salsa and sour cream"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 5,
            servings = 1,
            nutrition = NutritionInfo(420, 18.0, 52.0, 16.0, 10.0, 680.0),
            tags = listOf("vegetarian", "quick", "comfort-food"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.GENERAL_WELLNESS, HealthGoal.MORE_ENERGY),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Sour cream" to "Greek yogurt", "Flour tortilla" to "corn tortilla")
        ),
        Recipe(
            id = "l6",
            name = "Caprese Sandwich",
            description = "Fresh mozzarella, tomato, and basil on crusty bread",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Ciabatta roll", 1.0, ""),
                Ingredient("Fresh mozzarella", 3.0, "oz sliced"),
                Ingredient("Tomato", 1.0, "sliced"),
                Ingredient("Fresh basil", 5.0, "leaves"),
                Ingredient("Balsamic glaze", 1.0, "tbsp"),
                Ingredient("Olive oil", 1.0, "tsp")
            ),
            instructions = listOf(
                "Slice ciabatta roll in half",
                "Drizzle olive oil on both halves",
                "Layer mozzarella, tomato, and basil",
                "Drizzle with balsamic glaze",
                "Close sandwich and serve"
            ),
            prepTimeMinutes = 8,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(450, 22.0, 38.0, 24.0, 3.0, 480.0),
            tags = listOf("vegetarian", "italian", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Ciabatta" to "gluten-free bread", "Mozzarella" to "vegan cheese")
        ),

        // INTERMEDIATE LUNCHES
        Recipe(
            id = "l7",
            name = "Asian Chicken Stir-Fry",
            description = "Quick stir-fry with chicken and colorful vegetables",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Chicken breast", 6.0, "oz sliced"),
                Ingredient("Broccoli florets", 1.0, "cup"),
                Ingredient("Bell pepper", 0.5, "cup sliced"),
                Ingredient("Soy sauce", 2.0, "tbsp"),
                Ingredient("Sesame oil", 1.0, "tbsp"),
                Ingredient("Garlic", 2.0, "cloves minced"),
                Ingredient("Ginger", 1.0, "tsp minced"),
                Ingredient("Brown rice", 1.0, "cup cooked")
            ),
            instructions = listOf(
                "Heat sesame oil in a wok or large pan",
                "Cook chicken until browned, set aside",
                "Stir-fry garlic and ginger for 30 seconds",
                "Add vegetables and cook until crisp-tender",
                "Return chicken, add soy sauce, toss to coat",
                "Serve over brown rice"
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 12,
            servings = 2,
            nutrition = NutritionInfo(420, 36.0, 38.0, 14.0, 4.0, 720.0),
            tags = listOf("high-protein", "asian"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.LOSE_FAT),
            dietaryInfo = listOf(DietaryRestriction.DAIRY_FREE),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Soy sauce" to "coconut aminos", "Brown rice" to "cauliflower rice")
        ),
        Recipe(
            id = "l8",
            name = "Salmon Poke Bowl",
            description = "Hawaiian-inspired bowl with fresh salmon",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Sushi-grade salmon", 4.0, "oz cubed"),
                Ingredient("Sushi rice", 1.0, "cup cooked"),
                Ingredient("Avocado", 0.5, "sliced"),
                Ingredient("Cucumber", 0.5, "cup sliced"),
                Ingredient("Edamame", 0.25, "cup"),
                Ingredient("Soy sauce", 1.0, "tbsp"),
                Ingredient("Sesame seeds", 1.0, "tsp")
            ),
            instructions = listOf(
                "Place rice in a bowl",
                "Arrange salmon, avocado, cucumber, and edamame on top",
                "Drizzle with soy sauce",
                "Sprinkle with sesame seeds"
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(520, 32.0, 48.0, 22.0, 6.0, 580.0),
            tags = listOf("omega-3", "heart-healthy", "no-cook"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.HEART_HEALTH, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.DAIRY_FREE, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.LUNCH,
            substitutions = mapOf("Salmon" to "tofu", "Soy sauce" to "coconut aminos")
        )
    )

    private val dinnerRecipes = listOf(
        // BEGINNER DINNERS
        Recipe(
            id = "d1",
            name = "Sheet Pan Chicken & Vegetables",
            description = "Easy one-pan dinner with minimal cleanup",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Chicken thighs", 4.0, ""),
                Ingredient("Broccoli", 2.0, "cups"),
                Ingredient("Sweet potato", 1.0, "cubed"),
                Ingredient("Olive oil", 2.0, "tbsp"),
                Ingredient("Italian seasoning", 1.0, "tsp"),
                Ingredient("Garlic powder", 0.5, "tsp")
            ),
            instructions = listOf(
                "Preheat oven to 425°F (220°C)",
                "Toss vegetables with olive oil and seasonings",
                "Season chicken with remaining spices",
                "Arrange everything on a sheet pan",
                "Roast 25-30 minutes until chicken is cooked through"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 30,
            servings = 4,
            nutrition = NutritionInfo(380, 28.0, 22.0, 20.0, 4.0, 380.0),
            tags = listOf("one-pan", "meal-prep", "family-friendly"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Chicken thighs" to "chicken breast", "Sweet potato" to "butternut squash")
        ),
        Recipe(
            id = "d2",
            name = "Baked Salmon with Asparagus",
            description = "Heart-healthy salmon with roasted asparagus",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Salmon fillet", 6.0, "oz"),
                Ingredient("Asparagus", 1.0, "bunch"),
                Ingredient("Lemon", 0.5, ""),
                Ingredient("Olive oil", 1.0, "tbsp"),
                Ingredient("Garlic", 2.0, "cloves minced"),
                Ingredient("Dill", 1.0, "tsp")
            ),
            instructions = listOf(
                "Preheat oven to 400°F (200°C)",
                "Place salmon and asparagus on a baking sheet",
                "Drizzle with olive oil, sprinkle garlic and dill",
                "Squeeze lemon over everything",
                "Bake 15-18 minutes"
            ),
            prepTimeMinutes = 8,
            cookTimeMinutes = 18,
            servings = 1,
            nutrition = NutritionInfo(420, 38.0, 8.0, 26.0, 4.0, 280.0),
            tags = listOf("omega-3", "heart-healthy", "one-pan"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.HEART_HEALTH, HealthGoal.LOSE_FAT),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Salmon" to "cod", "Asparagus" to "green beans")
        ),
        Recipe(
            id = "d3",
            name = "Turkey Taco Lettuce Wraps",
            description = "Low-carb tacos using lettuce as shells",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Ground turkey", 8.0, "oz"),
                Ingredient("Taco seasoning", 1.0, "tbsp"),
                Ingredient("Butter lettuce", 6.0, "leaves"),
                Ingredient("Diced tomatoes", 0.5, "cup"),
                Ingredient("Shredded cheese", 0.25, "cup"),
                Ingredient("Sour cream", 2.0, "tbsp")
            ),
            instructions = listOf(
                "Brown turkey in a pan over medium heat",
                "Add taco seasoning and a splash of water",
                "Simmer 5 minutes",
                "Spoon turkey into lettuce leaves",
                "Top with tomatoes, cheese, and sour cream"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 15,
            servings = 2,
            nutrition = NutritionInfo(320, 28.0, 8.0, 20.0, 2.0, 520.0),
            tags = listOf("low-carb", "high-protein", "family-friendly"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.LOSE_FAT, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Ground turkey" to "ground chicken", "Sour cream" to "Greek yogurt")
        ),
        Recipe(
            id = "d4",
            name = "Pasta Primavera",
            description = "Colorful vegetable pasta with garlic and olive oil",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Whole wheat pasta", 8.0, "oz"),
                Ingredient("Zucchini", 1.0, "sliced"),
                Ingredient("Cherry tomatoes", 1.0, "cup halved"),
                Ingredient("Bell pepper", 1.0, "sliced"),
                Ingredient("Garlic", 3.0, "cloves minced"),
                Ingredient("Olive oil", 3.0, "tbsp"),
                Ingredient("Parmesan cheese", 0.25, "cup grated")
            ),
            instructions = listOf(
                "Cook pasta according to package directions",
                "Sauté garlic in olive oil",
                "Add vegetables and cook until tender",
                "Toss with drained pasta",
                "Top with parmesan and serve"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 20,
            servings = 4,
            nutrition = NutritionInfo(380, 14.0, 52.0, 14.0, 8.0, 280.0),
            tags = listOf("vegetarian", "family-friendly"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.GENERAL_WELLNESS, HealthGoal.MORE_ENERGY),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN),
            mealType = MealType.DINNER,
            substitutions = mapOf("Whole wheat pasta" to "gluten-free pasta", "Parmesan" to "nutritional yeast")
        ),
        Recipe(
            id = "d5",
            name = "Grilled Chicken Breast with Quinoa",
            description = "Simple grilled chicken with protein-packed quinoa",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Chicken breast", 6.0, "oz"),
                Ingredient("Quinoa", 0.5, "cup dry"),
                Ingredient("Olive oil", 1.0, "tbsp"),
                Ingredient("Lemon juice", 2.0, "tbsp"),
                Ingredient("Dried oregano", 1.0, "tsp"),
                Ingredient("Salt", 0.0, "to taste")
            ),
            instructions = listOf(
                "Marinate chicken in olive oil, lemon, and oregano",
                "Cook quinoa according to package directions",
                "Grill or pan-sear chicken 6-7 minutes per side",
                "Let chicken rest 5 minutes, then slice",
                "Serve over quinoa"
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 20,
            servings = 1,
            nutrition = NutritionInfo(480, 44.0, 34.0, 18.0, 4.0, 320.0),
            tags = listOf("high-protein", "meal-prep"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.LOSE_FAT),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Quinoa" to "brown rice", "Chicken breast" to "tofu")
        ),
        Recipe(
            id = "d6",
            name = "Vegetable Stir-Fry with Tofu",
            description = "Quick vegetarian stir-fry packed with protein",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Firm tofu", 8.0, "oz cubed"),
                Ingredient("Broccoli", 1.0, "cup"),
                Ingredient("Snap peas", 0.5, "cup"),
                Ingredient("Carrots", 0.5, "cup sliced"),
                Ingredient("Soy sauce", 2.0, "tbsp"),
                Ingredient("Sesame oil", 1.0, "tbsp"),
                Ingredient("Rice", 1.0, "cup cooked")
            ),
            instructions = listOf(
                "Press tofu to remove excess water",
                "Pan-fry tofu until golden on all sides",
                "Remove tofu, add vegetables to pan",
                "Stir-fry vegetables until crisp-tender",
                "Return tofu, add soy sauce, toss to combine",
                "Serve over rice"
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 15,
            servings = 2,
            nutrition = NutritionInfo(380, 18.0, 42.0, 16.0, 6.0, 680.0),
            tags = listOf("vegan", "high-protein", "quick"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.GENERAL_WELLNESS, HealthGoal.HEART_HEALTH),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.VEGETARIAN, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Tofu" to "tempeh", "Rice" to "cauliflower rice")
        ),

        // INTERMEDIATE DINNERS
        Recipe(
            id = "d7",
            name = "Herb-Crusted Pork Tenderloin",
            description = "Juicy pork with a flavorful herb crust",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Pork tenderloin", 1.0, "lb"),
                Ingredient("Dijon mustard", 2.0, "tbsp"),
                Ingredient("Fresh rosemary", 1.0, "tbsp chopped"),
                Ingredient("Fresh thyme", 1.0, "tbsp chopped"),
                Ingredient("Garlic", 3.0, "cloves minced"),
                Ingredient("Olive oil", 2.0, "tbsp"),
                Ingredient("Roasted potatoes", 2.0, "cups")
            ),
            instructions = listOf(
                "Preheat oven to 425°F (220°C)",
                "Mix mustard, herbs, and garlic",
                "Coat pork with herb mixture",
                "Sear pork in olive oil on all sides",
                "Transfer to oven and roast 20-25 minutes",
                "Rest 10 minutes before slicing",
                "Serve with roasted potatoes"
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 30,
            servings = 4,
            nutrition = NutritionInfo(380, 32.0, 24.0, 18.0, 3.0, 280.0),
            tags = listOf("lean-protein", "impressive"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Pork tenderloin" to "chicken breast")
        ),
        Recipe(
            id = "d8",
            name = "Shrimp Scampi",
            description = "Garlicky shrimp in white wine sauce over pasta",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Shrimp", 1.0, "lb peeled"),
                Ingredient("Linguine", 8.0, "oz"),
                Ingredient("Butter", 3.0, "tbsp"),
                Ingredient("Garlic", 5.0, "cloves minced"),
                Ingredient("White wine", 0.5, "cup"),
                Ingredient("Lemon juice", 3.0, "tbsp"),
                Ingredient("Parsley", 0.25, "cup chopped"),
                Ingredient("Red pepper flakes", 0.25, "tsp")
            ),
            instructions = listOf(
                "Cook pasta according to package directions",
                "Sauté garlic in butter until fragrant",
                "Add shrimp, cook 2 minutes per side",
                "Add wine and lemon juice, simmer 2 minutes",
                "Toss with drained pasta",
                "Top with parsley and red pepper flakes"
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 20,
            servings = 4,
            nutrition = NutritionInfo(420, 28.0, 46.0, 14.0, 2.0, 520.0),
            tags = listOf("seafood", "date-night", "italian"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.HEART_HEALTH),
            dietaryInfo = listOf(),
            mealType = MealType.DINNER,
            substitutions = mapOf("Linguine" to "zucchini noodles", "Butter" to "olive oil")
        ),
        Recipe(
            id = "d9",
            name = "Stuffed Bell Peppers",
            description = "Colorful peppers filled with seasoned beef and rice",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Bell peppers", 4.0, "large"),
                Ingredient("Ground beef", 1.0, "lb lean"),
                Ingredient("Cooked rice", 1.0, "cup"),
                Ingredient("Diced tomatoes", 1.0, "can"),
                Ingredient("Onion", 0.5, "cup diced"),
                Ingredient("Italian seasoning", 1.0, "tsp"),
                Ingredient("Mozzarella cheese", 0.5, "cup shredded")
            ),
            instructions = listOf(
                "Preheat oven to 375°F (190°C)",
                "Cut tops off peppers and remove seeds",
                "Brown beef with onion, drain excess fat",
                "Mix beef with rice, tomatoes, and seasoning",
                "Stuff peppers with mixture",
                "Bake 35-40 minutes",
                "Top with cheese last 5 minutes"
            ),
            prepTimeMinutes = 20,
            cookTimeMinutes = 45,
            servings = 4,
            nutrition = NutritionInfo(380, 28.0, 26.0, 18.0, 4.0, 480.0),
            tags = listOf("family-friendly", "meal-prep"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Ground beef" to "ground turkey", "Rice" to "quinoa")
        ),
        Recipe(
            id = "d10",
            name = "Lemon Herb Roasted Chicken",
            description = "Perfectly roasted whole chicken with herbs",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Whole chicken", 4.0, "lb"),
                Ingredient("Lemon", 2.0, ""),
                Ingredient("Fresh thyme", 6.0, "sprigs"),
                Ingredient("Fresh rosemary", 4.0, "sprigs"),
                Ingredient("Garlic", 1.0, "head"),
                Ingredient("Butter", 4.0, "tbsp softened"),
                Ingredient("Olive oil", 2.0, "tbsp")
            ),
            instructions = listOf(
                "Preheat oven to 425°F (220°C)",
                "Pat chicken dry with paper towels",
                "Rub butter under and over the skin",
                "Stuff cavity with lemon, herbs, and garlic",
                "Drizzle with olive oil, season generously",
                "Roast 1 hour 15 minutes until golden",
                "Rest 15 minutes before carving"
            ),
            prepTimeMinutes = 20,
            cookTimeMinutes = 75,
            servings = 6,
            nutrition = NutritionInfo(380, 36.0, 2.0, 24.0, 0.0, 320.0),
            tags = listOf("sunday-dinner", "impressive", "meal-prep"),
            difficulty = CookingSkill.INTERMEDIATE,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.DINNER,
            substitutions = mapOf("Butter" to "olive oil")
        )
    )

    private val snackRecipes = listOf(
        Recipe(
            id = "s1",
            name = "Apple Slices with Almond Butter",
            description = "Sweet and satisfying snack with healthy fats",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Apple", 1.0, "medium"),
                Ingredient("Almond butter", 2.0, "tbsp")
            ),
            instructions = listOf(
                "Slice apple into wedges",
                "Serve with almond butter for dipping"
            ),
            prepTimeMinutes = 3,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(280, 6.0, 32.0, 16.0, 6.0, 5.0),
            tags = listOf("quick", "no-cook", "portable"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.MORE_ENERGY, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Almond butter" to "peanut butter")
        ),
        Recipe(
            id = "s2",
            name = "Greek Yogurt with Honey",
            description = "Creamy protein-rich snack",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Greek yogurt", 1.0, "cup"),
                Ingredient("Honey", 1.0, "tbsp"),
                Ingredient("Walnuts", 2.0, "tbsp chopped")
            ),
            instructions = listOf(
                "Place yogurt in a bowl",
                "Drizzle with honey",
                "Top with walnuts"
            ),
            prepTimeMinutes = 2,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(280, 18.0, 26.0, 12.0, 1.0, 80.0),
            tags = listOf("quick", "high-protein", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Honey" to "maple syrup", "Walnuts" to "almonds")
        ),
        Recipe(
            id = "s3",
            name = "Hummus with Veggie Sticks",
            description = "Fiber-rich vegetables with creamy hummus",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Hummus", 0.25, "cup"),
                Ingredient("Carrot sticks", 0.5, "cup"),
                Ingredient("Celery sticks", 0.5, "cup"),
                Ingredient("Cucumber slices", 0.5, "cup")
            ),
            instructions = listOf(
                "Arrange vegetables on a plate",
                "Serve with hummus for dipping"
            ),
            prepTimeMinutes = 5,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(180, 6.0, 22.0, 8.0, 6.0, 320.0),
            tags = listOf("quick", "vegan", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.LOSE_FAT, HealthGoal.HEART_HEALTH),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Hummus" to "guacamole")
        ),
        Recipe(
            id = "s4",
            name = "String Cheese and Grapes",
            description = "Classic protein and fruit combo",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("String cheese", 1.0, "stick"),
                Ingredient("Grapes", 1.0, "cup")
            ),
            instructions = listOf(
                "Serve cheese with grapes"
            ),
            prepTimeMinutes = 1,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(180, 8.0, 24.0, 6.0, 1.0, 180.0),
            tags = listOf("quick", "portable", "kid-friendly"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.GENERAL_WELLNESS, HealthGoal.MORE_ENERGY),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("String cheese" to "cheese cubes")
        ),
        Recipe(
            id = "s5",
            name = "Trail Mix",
            description = "Energy-boosting mix of nuts and dried fruit",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Almonds", 0.25, "cup"),
                Ingredient("Dried cranberries", 2.0, "tbsp"),
                Ingredient("Dark chocolate chips", 1.0, "tbsp"),
                Ingredient("Pumpkin seeds", 2.0, "tbsp")
            ),
            instructions = listOf(
                "Combine all ingredients in a container",
                "Mix well and portion as needed"
            ),
            prepTimeMinutes = 2,
            cookTimeMinutes = 0,
            servings = 2,
            nutrition = NutritionInfo(280, 8.0, 24.0, 18.0, 4.0, 5.0),
            tags = listOf("portable", "no-cook", "meal-prep"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.MORE_ENERGY, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Almonds" to "cashews", "Dried cranberries" to "raisins")
        ),
        Recipe(
            id = "s6",
            name = "Hard-Boiled Eggs",
            description = "Perfect protein snack, easy to prep ahead",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Eggs", 2.0, "large"),
                Ingredient("Salt", 0.0, "to taste")
            ),
            instructions = listOf(
                "Place eggs in a pot, cover with water",
                "Bring to a boil",
                "Remove from heat, cover, let sit 12 minutes",
                "Transfer to ice bath",
                "Peel and season with salt"
            ),
            prepTimeMinutes = 2,
            cookTimeMinutes = 15,
            servings = 1,
            nutrition = NutritionInfo(140, 12.0, 1.0, 10.0, 0.0, 280.0),
            tags = listOf("high-protein", "meal-prep", "portable"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.LOSE_FAT),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf()
        ),
        Recipe(
            id = "s7",
            name = "Cottage Cheese with Pineapple",
            description = "Tropical twist on a protein-rich snack",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Cottage cheese", 0.5, "cup"),
                Ingredient("Pineapple chunks", 0.5, "cup")
            ),
            instructions = listOf(
                "Place cottage cheese in a bowl",
                "Top with pineapple chunks"
            ),
            prepTimeMinutes = 2,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(160, 14.0, 18.0, 2.0, 1.0, 380.0),
            tags = listOf("high-protein", "quick", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGETARIAN, DietaryRestriction.GLUTEN_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Pineapple" to "peaches")
        ),
        Recipe(
            id = "s8",
            name = "Rice Cakes with Avocado",
            description = "Light and satisfying crunchy snack",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Rice cakes", 2.0, ""),
                Ingredient("Avocado", 0.5, ""),
                Ingredient("Everything bagel seasoning", 1.0, "tsp")
            ),
            instructions = listOf(
                "Mash avocado with a fork",
                "Spread on rice cakes",
                "Sprinkle with seasoning"
            ),
            prepTimeMinutes = 3,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(200, 3.0, 22.0, 12.0, 5.0, 180.0),
            tags = listOf("quick", "vegan", "no-cook"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.HEART_HEALTH, HealthGoal.GENERAL_WELLNESS),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Rice cakes" to "whole grain crackers")
        ),
        Recipe(
            id = "s9",
            name = "Edamame",
            description = "Protein-packed Japanese snack",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Frozen edamame", 1.0, "cup in pods"),
                Ingredient("Sea salt", 0.5, "tsp")
            ),
            instructions = listOf(
                "Microwave or boil edamame until heated through",
                "Drain and sprinkle with sea salt",
                "Squeeze pods to eat the beans"
            ),
            prepTimeMinutes = 1,
            cookTimeMinutes = 4,
            servings = 1,
            nutrition = NutritionInfo(120, 11.0, 10.0, 5.0, 4.0, 280.0),
            tags = listOf("high-protein", "vegan", "quick"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.BUILD_MUSCLE, HealthGoal.HEART_HEALTH),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf()
        ),
        Recipe(
            id = "s10",
            name = "Banana with Peanut Butter",
            description = "Classic energy-boosting combo",
            imageUrl = "",
            ingredients = listOf(
                Ingredient("Banana", 1.0, "medium"),
                Ingredient("Peanut butter", 2.0, "tbsp")
            ),
            instructions = listOf(
                "Slice banana or eat whole",
                "Dip or spread with peanut butter"
            ),
            prepTimeMinutes = 2,
            cookTimeMinutes = 0,
            servings = 1,
            nutrition = NutritionInfo(290, 8.0, 34.0, 16.0, 4.0, 140.0),
            tags = listOf("quick", "no-cook", "pre-workout"),
            difficulty = CookingSkill.BEGINNER,
            healthGoals = listOf(HealthGoal.MORE_ENERGY, HealthGoal.BUILD_MUSCLE),
            dietaryInfo = listOf(DietaryRestriction.VEGAN, DietaryRestriction.GLUTEN_FREE, DietaryRestriction.DAIRY_FREE),
            mealType = MealType.SNACK,
            substitutions = mapOf("Peanut butter" to "almond butter")
        )
    )
}
