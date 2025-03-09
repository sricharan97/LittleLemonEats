package com.littlelemon.littlelemonapp.data

interface UserPreferences {
    fun saveUserData(firstName: String, lastName: String, email: String)
    fun getUserData(): UserData?
    fun isLoggedIn(): Boolean
    fun clearUserData()
}