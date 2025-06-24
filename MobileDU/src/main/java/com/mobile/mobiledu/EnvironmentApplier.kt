package com.mobile.mobiledu

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup

object EnvironmentApplier {

    fun applyToActivity(activity: Activity) {
        val prefs = activity.getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
        var color = prefs.getInt("envColor", Color.WHITE)
        val invert = prefs.getBoolean("envInvert", false)
        val intensity = prefs.getInt("envIntensity", 0)
        val brightness = prefs.getInt("envBrightness", 100)
        val orientation = prefs.getInt("envOrientation", -1)

        if (invert) {
            color = invertColor(color)
        }

        val alpha = (255 * (100 - intensity) / 100).coerceIn(0, 255)
        val brightnessFactor = brightness / 100f
        val r = (Color.red(color) * brightnessFactor).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * brightnessFactor).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * brightnessFactor).toInt().coerceIn(0, 255)
        val finalColor = Color.argb(alpha, r, g, b)

        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        rootView.post {
            rootView.setBackgroundColor(finalColor)
            if (orientation != -1) {
                activity.requestedOrientation = orientation
            }
        }
    }

    private fun invertColor(color: Int): Int {
        val red = 255 - Color.red(color)
        val green = 255 - Color.green(color)
        val blue = 255 - Color.blue(color)
        return Color.rgb(red, green, blue)
    }
}
