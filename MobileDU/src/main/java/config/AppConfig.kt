package config

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface

object AppConfig {
    private const val PREF_NAME = "du_prefs"

    // === Ambiente ===
    var color: Int = Color.WHITE
    var invertColors: Boolean = false
    var intensity: Int = 0
    var autoBrightness: Boolean = true
    var brightness: Int = 100
    var orientation: Int = -1

    // === Texto ===
    var textColor: Int = Color.BLACK
    var textSize: Float = 16f
    var textFontName: String = "sans-serif"

    // === Som ===
    var speechRate: Float = 1.0f
    var pitch: Float = 1.0f
    var isVoiceEnabled: Boolean = true
    var volume: Int = 5

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Ambiente
        color = prefs.getInt("envColor", Color.WHITE)
        invertColors = prefs.getBoolean("envInvert", false)
        intensity = prefs.getInt("envIntensity", 0)
        autoBrightness = prefs.getBoolean("envAutoBrightness", true)
        brightness = prefs.getInt("envBrightness", 100)
        orientation = prefs.getInt("envOrientation", -1)

        // Texto
        textColor = prefs.getInt("textColor", Color.BLACK)
        textSize = prefs.getFloat("textSize", 16f)
        textFontName = prefs.getString("textFontName", "sans-serif") ?: "sans-serif"

        // Som
        speechRate = prefs.getFloat("speechRate", 1.0f)
        pitch = prefs.getFloat("pitch", 1.0f)
        isVoiceEnabled = prefs.getBoolean("voiceEnabled", true)
        volume = prefs.getInt("soundVolume",5)
    }

    fun save(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            // Ambiente
            .putInt("envColor", color)
            .putBoolean("envInvert", invertColors)
            .putInt("envIntensity", intensity)
            .putBoolean("envAutoBrightness", autoBrightness)
            .putInt("envBrightness", brightness)
            .putInt("envOrientation", orientation)

            // Texto
            .putInt("textColor", textColor)
            .putFloat("textSize", textSize)
            .putString("textFontName", textFontName)

            // Som
            .putFloat("speechRate", speechRate)
            .putFloat("pitch", pitch)
            .putBoolean("voiceEnabled", isVoiceEnabled)
            .putInt("soundVolume", volume)

            .apply()
    }

    fun getFont(): Typeface =
        Typeface.create(textFontName, Typeface.NORMAL)
}
