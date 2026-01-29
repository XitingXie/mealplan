package com.mealplan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mealplan.ui.screens.auth.AuthScreen
import com.mealplan.ui.screens.home.HomeScreen
import com.mealplan.ui.screens.onboarding.OnboardingScreen
import com.mealplan.ui.screens.profile.ProfileScreen
import com.mealplan.ui.screens.quicklog.QuickLogScreen
import com.mealplan.ui.screens.recipe.RecipeDetailScreen
import com.mealplan.ui.screens.scanner.ScannerScreen
import com.mealplan.ui.screens.splash.SplashScreen
import com.mealplan.ui.screens.weeklyplan.WeeklyPlanScreen
import com.mealplan.ui.screens.wellness.WellnessCheckInScreen
import com.mealplan.ui.screens.wellness.BenefitsTimelineScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object WeeklyPlan : Screen("weekly_plan")
    object RecipeDetail : Screen("recipe/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe/$recipeId"
    }
    object Scanner : Screen("scanner")
    object QuickLog : Screen("quick_log")
    object Profile : Screen("profile")
    object WellnessCheckIn : Screen("wellness_checkin")
    object BenefitsTimeline : Screen("benefits_timeline")
}

@Composable
fun MealPlanNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                onSignInSuccess = { needsOnboarding ->
                    if (needsOnboarding) {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToRecipe = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onNavigateToWeeklyPlan = {
                    navController.navigate(Screen.WeeklyPlan.route)
                },
                onNavigateToScanner = {
                    navController.navigate(Screen.Scanner.route)
                },
                onNavigateToQuickLog = {
                    navController.navigate(Screen.QuickLog.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.WeeklyPlan.route) {
            WeeklyPlanScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecipe = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                }
            )
        }

        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            RecipeDetailScreen(
                recipeId = recipeId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Scanner.route) {
            ScannerScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecipe = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                }
            )
        }

        composable(Screen.QuickLog.route) {
            QuickLogScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToWellness = {
                    navController.navigate(Screen.WellnessCheckIn.route)
                },
                onNavigateToBenefits = {
                    navController.navigate(Screen.BenefitsTimeline.route)
                },
                onSignOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.WellnessCheckIn.route) {
            WellnessCheckInScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.BenefitsTimeline.route) {
            BenefitsTimelineScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
