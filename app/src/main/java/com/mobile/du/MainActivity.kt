package com.mobile.du

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import config.AppConfig
import utils.LibrasSupportManager

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btnPause: Button
    private lateinit var btnResume: Button
    private lateinit var videoName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        AppConfig.load(this)

        videoView = findViewById(R.id.mainVideo)
        videoName = "video_exemplo"

        btnPause = findViewById(R.id.btnPauseVideo)
        btnResume = findViewById(R.id.btnResumeVideo)

        btnPause.setOnClickListener {
            if (videoView.isPlaying) videoView.pause()
            LibrasSupportManager.pauseLibrasVideo()
        }

        btnResume.setOnClickListener {
            val currentPosition = videoView.currentPosition
            videoView.setVideoURI(Uri.parse("android.resource://$packageName/raw/$videoName"))
            videoView.setOnPreparedListener { mp ->
                mp.seekTo(currentPosition)
                mp.start()
            }
            if (AppConfig.librasEnabled) {
                LibrasSupportManager.enableLibrasOverlayIfConfigured(this, videoName)
                LibrasSupportManager.prepareAndPlayLibrasVideo(currentPosition, this)
            }
        }

        startVideosFromBeginning()
    }

    override fun onResume() {
        super.onResume()
        try {
            com.mobile.mobiledu.DUSettingsApplier.applyToActivity(this)
            com.mobile.mobiledu.EnvironmentApplier.applyToActivity(this)
            com.mobile.mobiledu.SoundApplier.applyToActivity(this)

            startVideosFromBeginning()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startVideosFromBeginning() {
        val videoUri = Uri.parse("android.resource://$packageName/raw/$videoName")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mp ->
            mp.seekTo(0)
            mp.setOnSeekCompleteListener {
                mp.start()
            }
        }
        videoView.setOnCompletionListener {
            LibrasSupportManager.removeLibrasOverlay(this)
        }

        val librasResId = resources.getIdentifier("${videoName}lb", "raw", packageName)
        AppConfig.librasVideoURI = if (librasResId != 0) {
            "android.resource://$packageName/$librasResId"
        } else {
            ""
        }

        if (AppConfig.librasEnabled) {
            LibrasSupportManager.enableLibrasOverlayIfConfigured(this, videoName)
            LibrasSupportManager.prepareAndPlayLibrasVideo(0, this)
        } else {
            LibrasSupportManager.removeLibrasOverlay(this)
        }
    }
}
