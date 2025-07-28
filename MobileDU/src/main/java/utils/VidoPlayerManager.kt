package utils

import android.app.Activity
import android.net.Uri
import android.widget.VideoView
import config.AppConfig

object VideoPlayerManager {

    private lateinit var videoView: VideoView
    private var videoName: String = ""

    fun start(activity: Activity, name: String) {
        videoName = name
        videoView = activity.findViewById(
            activity.resources.getIdentifier("mainVideo", "id", activity.packageName)
        )

        val uri = Uri.parse("android.resource://${activity.packageName}/raw/$videoName")
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mp ->
            mp.seekTo(0)
            mp.setOnSeekCompleteListener {
                mp.start()
            }
        }

        videoView.setOnCompletionListener {
            LibrasSupportManager.removeLibrasOverlay(activity)
        }

        if (AppConfig.librasEnabled) {
            LibrasSupportManager.playLibrasIfEnabled(activity, videoName)
        } else {
            LibrasSupportManager.removeLibrasOverlay(activity)
        }

        videoView.start()
    }

    fun pause() {
        if (::videoView.isInitialized && videoView.isPlaying) {
            videoView.pause()
            LibrasSupportManager.pauseLibrasVideo()
        }
    }

    fun resume(activity: Activity) {
        if (!::videoView.isInitialized) return

        val current = videoView.currentPosition
        val uri = Uri.parse("android.resource://${activity.packageName}/raw/$videoName")
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mp ->
            mp.seekTo(current)
            mp.start()
        }

        LibrasSupportManager.playLibrasIfEnabled(activity, videoName, current)
    }
}
