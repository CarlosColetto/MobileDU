package com.mobile.mobiledu

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import java.util.*

object SoundApplier {

    private var tts: TextToSpeech? = null

    fun applyToActivity(activity: Activity) {
        val prefs = activity.getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
        val mute = prefs.getBoolean("soundMute", false)
        val volume = prefs.getInt("soundVolume", 5)
        val screenReader = prefs.getBoolean("screenReader", false)
        val speakingSpeed = prefs.getFloat("speakingSpeed", 1.0f)

        val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (mute) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        } else {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
        }

        if (screenReader) {
            tts = TextToSpeech(activity.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.getDefault()
                    tts?.setSpeechRate(speakingSpeed)
                    tts?.speak("Leitor de tela ativado", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }
}
