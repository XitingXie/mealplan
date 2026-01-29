package com.mealplan.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mealplan.data.local.dao.UserDao
import com.mealplan.data.local.entity.UserEntity
import com.mealplan.domain.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userDao: UserDao
) {
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val isLoggedIn: Boolean
        get() = currentUser != null

    fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                // Create or update local user record
                val existingUser = userDao.getUserById(user.uid)
                if (existingUser == null) {
                    // New user - create profile with defaults (onboarding not completed)
                    val newProfile = UserProfile(
                        uid = user.uid,
                        email = user.email ?: "",
                        displayName = user.displayName ?: "",
                        onboardingCompleted = false
                    )
                    userDao.insertUser(UserEntity.fromDomainModel(newProfile))
                }
                Result.success(user)
            } else {
                Result.failure(Exception("Sign in failed - no user returned"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = currentUser
            if (user != null) {
                userDao.deleteUserById(user.uid)
                user.delete().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No user logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasCompletedOnboarding(): Boolean {
        val user = currentUser ?: return false
        val userEntity = userDao.getUserById(user.uid)
        return userEntity?.onboardingCompleted == true
    }
}
