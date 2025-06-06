package com.example.stalcraft_companion.data.modles

import android.content.Context

object Prefs {
    private const val PREFS_NAME = "app_prefs"
    private const val LAST_UPDATE_KEY = "last_update"

    fun getLastUpdate(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(LAST_UPDATE_KEY, null)
    }

    fun setLastUpdate(context: Context, date: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(LAST_UPDATE_KEY, date)
            .apply()
    }
}