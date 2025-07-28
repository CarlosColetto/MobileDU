package com.mobile.du

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import config.AppConfig
import utils.VideoPlayerManager

class MainActivity : AppCompatActivity() {

    private lateinit var btnPause: Button
    private lateinit var btnResume: Button
    private lateinit var videoName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Garante padding adequado para status bar e gestos
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        AppConfig.load(this)
        videoName = "video_exemplo"

        btnPause = findViewById(R.id.btnPauseVideo)
        btnResume = findViewById(R.id.btnResumeVideo)

        btnPause.setOnClickListener {
            VideoPlayerManager.pause()
        }

        btnResume.setOnClickListener {
            VideoPlayerManager.resume(this)
        }

        VideoPlayerManager.start(this, videoName)
    }

    override fun onResume() {
        super.onResume()
        try {
            com.mobile.mobiledu.DUSettingsApplier.applyToActivity(this)
            com.mobile.mobiledu.EnvironmentApplier.applyToActivity(this)
            com.mobile.mobiledu.SoundApplier.applyToActivity(this)

            VideoPlayerManager.start(this, videoName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
