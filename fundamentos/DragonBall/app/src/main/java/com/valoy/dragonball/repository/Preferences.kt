package com.valoy.dragonball.repository

import android.content.Context
import androidx.core.content.edit

class Preferences(private val context: Context) {

    private val sharedPref = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        sharedPref.edit {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPref.getString(TOKEN_KEY, null)
    }

    companion object {
        private const val PREFERENCES_NAME = "dragonball_preferences"
        private val TOKEN_KEY = "token"
    }
}