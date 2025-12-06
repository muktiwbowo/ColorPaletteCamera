package com.svault.colorpalettecamera.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, value).apply()

    companion object {
        private const val PREFS_NAME = "color_palette_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
