package com.mealplan.domain.model

import java.time.LocalDate

data class WellnessCheckIn(
    val id: String = "",
    val userId: String = "",
    val weekNumber: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val energyLevel: Int = 3,              // 1-5 scale
    val digestionQuality: TrendRating = TrendRating.SAME,
    val postMealFeeling: PostMealFeeling = PostMealFeeling.LIGHT_SATISFIED,
    val sleepQuality: Int = 3,             // 1-5 scale
    val receivedCompliments: Boolean = false,
    val complimentNote: String? = null,
    val overallMood: Int = 3,              // 1-5 scale
    val timestamp: Long = System.currentTimeMillis()
)

enum class TrendRating(val displayName: String, val emoji: String) {
    BETTER("Better", "\u2B06\uFE0F"),
    SAME("Same", "\u27A1\uFE0F"),
    WORSE("Worse", "\u2B07\uFE0F")
}

enum class PostMealFeeling(val displayName: String, val description: String) {
    LIGHT_SATISFIED("Light & Satisfied", "Feeling good after meals"),
    BLOATED("Bloated", "Feeling heavy or uncomfortable"),
    STILL_HUNGRY("Still Hungry", "Not feeling satisfied")
}

data class UserProgress(
    val totalHealthyDays: Int = 0,        // Never resets - always grows
    val totalCheckIns: Int = 0,
    val recipesCompleted: Int = 0,
    val currentStreak: Int = 0,           // Optional - not emphasized
    val longestStreak: Int = 0,           // Personal best, celebrate once
    val journeyStartDate: LocalDate = LocalDate.now(),
    val lastActiveDate: LocalDate = LocalDate.now()
)

data class Achievement(
    val id: String = "",
    val type: AchievementType = AchievementType.FIRST_CHECKIN,
    val unlockedAt: Long? = null,
    val title: String = "",
    val description: String = "",
    val iconName: String = ""
) {
    val isUnlocked: Boolean get() = unlockedAt != null
}

enum class AchievementType(val title: String, val description: String, val iconName: String) {
    // Streak achievements (framed positively)
    STREAK_7_DAYS("Week Warrior", "7 healthy days completed!", "ic_streak_7"),
    STREAK_14_DAYS("Two Week Champion", "14 healthy days completed!", "ic_streak_14"),
    STREAK_30_DAYS("Monthly Master", "30 healthy days completed!", "ic_streak_30"),
    STREAK_60_DAYS("Two Month Hero", "60 healthy days completed!", "ic_streak_60"),
    STREAK_90_DAYS("Quarter Champion", "90 healthy days completed!", "ic_streak_90"),

    // First-time achievements
    FIRST_CHECKIN("Getting Started", "Logged your first meal!", "ic_first_checkin"),
    FIRST_PHOTO_LOG("Picture Perfect", "Logged a meal with a photo!", "ic_photo"),
    FIRST_WELLNESS_CHECKIN("Self-Aware", "Completed your first wellness check-in!", "ic_wellness"),

    // Wellness improvements
    ENERGY_IMPROVED("Energy Boost", "Your energy levels have improved!", "ic_energy"),
    DIGESTION_IMPROVED("Feeling Good", "Your digestion has improved!", "ic_digestion"),
    GOT_COMPLIMENT("Looking Great", "Someone noticed your healthy glow!", "ic_compliment"),

    // Milestones (cumulative, never lost)
    WEEK_1_COMPLETE("Week 1 Complete", "You've completed your first week!", "ic_week1"),
    MONTH_1_COMPLETE("Month 1 Complete", "One month of healthy eating!", "ic_month1"),
    MONTH_3_COMPLETE("3 Month Milestone", "Three months of dedication!", "ic_month3")
}

// Benefits timeline for the Benefits Tracker screen
data class ExpectedBenefit(
    val weekRange: IntRange,
    val title: String,
    val description: String,
    val iconName: String
)

object BenefitsTimeline {
    val benefits = listOf(
        ExpectedBenefit(
            weekRange = 1..2,
            title = "Less Bloating",
            description = "You may notice less bloating after meals",
            iconName = "ic_bloating"
        ),
        ExpectedBenefit(
            weekRange = 2..3,
            title = "Better Digestion",
            description = "Digestion often improves - easier bathroom visits",
            iconName = "ic_digestion"
        ),
        ExpectedBenefit(
            weekRange = 3..4,
            title = "More Energy",
            description = "Energy levels typically start increasing",
            iconName = "ic_energy"
        ),
        ExpectedBenefit(
            weekRange = 4..8,
            title = "Clearer Skin",
            description = "Skin clarity may improve",
            iconName = "ic_skin"
        ),
        ExpectedBenefit(
            weekRange = 8..12,
            title = "Visible Changes",
            description = "People may start noticing you look healthier",
            iconName = "ic_visible"
        ),
        ExpectedBenefit(
            weekRange = 12..52,
            title = "Long-term Benefits",
            description = "Sustained energy, better mood, clearer thinking",
            iconName = "ic_longterm"
        )
    )
}
