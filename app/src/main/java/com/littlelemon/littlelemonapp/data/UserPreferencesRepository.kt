package com.littlelemon.littlelemonapp.data

import android.content.Context
import androidx.core.content.edit

class UserPreferencesRepository(context: Context) : UserPreferences {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveUserData(firstName: String, lastName: String, email: String) {
        sharedPreferences.edit {
            putString(KEY_FIRST_NAME, firstName)
            putString(KEY_LAST_NAME, lastName)
            putString(KEY_EMAIL, email)
            putBoolean(KEY_LOGGED_IN, true)
            apply()
        }
    }

    override fun getUserData(): UserData? {
        val firstName = sharedPreferences.getString(KEY_FIRST_NAME, null)
        val lastName = sharedPreferences.getString(KEY_LAST_NAME, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)

        return if (firstName != null && lastName != null && email != null) {
            UserData(firstName, lastName, email)
        } else null
    }

    override fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }

    override fun clearUserData() {
        sharedPreferences.edit() { clear() }
    }

    companion object {
        private const val PREFS_NAME = "UserPrefs"
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_EMAIL = "email"
        private const val KEY_LOGGED_IN = "isLoggedIn"
    }
}