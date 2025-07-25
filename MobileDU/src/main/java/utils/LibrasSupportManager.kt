package utils

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.VideoView
import config.AppConfig

object LibrasSupportManager {

    private var overlayContainer: FrameLayout? = null
    var librasVideoView: VideoView? = null
    private var baseVideoName: String? = null

    fun enableLibrasOverlayIfConfigured(activity: Activity, baseVideoNameParam: String) {
        try {
            if (!AppConfig.librasEnabled) {
                Log.d("LIBRAS", "LIBRAS desativado")
                return
            }

            baseVideoName = baseVideoNameParam
            val librasVideoName = "${baseVideoNameParam}lb"
            val librasResId = activity.resources.getIdentifier(librasVideoName, "raw", activity.packageName)

            if (librasResId == 0) {
                Log.w("LIBRAS", "Vídeo LIBRAS não encontrado para: $librasVideoName")
                return
            }

            val uri = Uri.parse("android.resource://${activity.packageName}/$librasResId")

            val decorView = activity.window.decorView as ViewGroup
            overlayContainer?.let { decorView.removeView(it) }

            val videoView = VideoView(activity).apply {
                setVideoURI(uri)
                setOnPreparedListener { it.isLooping = true }
                start()
            }

            librasVideoView = videoView
            videoView.tag = librasVideoName

            val container = FrameLayout(activity).apply {
                addView(videoView, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ))
            }

            var dx = 0f
            var dy = 0f
            container.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dx = v.x - event.rawX
                        dy = v.y - event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        v.animate()
                            .x(event.rawX + dx)
                            .y(event.rawY + dy)
                            .setDuration(0)
                            .start()
                    }
                }
                true
            }

            val layoutParams = FrameLayout.LayoutParams(300, 300).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                bottomMargin = 32
                marginEnd = 32
            }

            decorView.addView(container, layoutParams)
            overlayContainer = container

            Log.d("LIBRAS", "Vídeo LIBRAS exibido com sucesso.")
        } catch (e: Exception) {
            Log.e("LIBRAS", "Erro ao exibir vídeo LIBRAS: ${e.message}", e)
        }
    }

    fun restartLibrasVideo(currentPositionMillis: Int, activity: Activity) {
        try {
            val rootName = baseVideoName ?: return
            val librasVideoName = "${rootName}lb"
            val librasResId = activity.resources.getIdentifier(librasVideoName, "raw", activity.packageName)

            if (librasResId == 0) {
                Log.w("LIBRAS", "Reinício falhou: vídeo LIBRAS $librasVideoName não encontrado.")
                return
            }

            val uri = Uri.parse("android.resource://${activity.packageName}/$librasResId")

            librasVideoView?.let {
                it.stopPlayback()
                it.setVideoURI(uri)
                it.setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.seekTo(currentPositionMillis)
                    it.start()
                }
            }
        } catch (e: Exception) {
            Log.e("LIBRAS", "Erro ao reiniciar vídeo LIBRAS: ${e.message}", e)
        }
    }

    fun pauseLibrasVideo() {
        try {
            librasVideoView?.pause()
        } catch (e: Exception) {
            Log.e("LIBRAS", "Erro ao pausar vídeo LIBRAS: ${e.message}", e)
        }
    }

    fun removeLibrasOverlay(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        overlayContainer?.let {
            decorView.removeView(it)
            overlayContainer = null
        }
        librasVideoView = null
        baseVideoName = null
    }
    fun prepareAndPlayLibrasVideo(currentPositionMillis: Int, activity: Activity) {
        try {
            val rootName = baseVideoName ?: return
            val librasVideoName = "${rootName}lb"
            val librasResId = activity.resources.getIdentifier(librasVideoName, "raw", activity.packageName)
            if (librasResId == 0) return

            val uri = Uri.parse("android.resource://${activity.packageName}/$librasResId")

            librasVideoView?.apply {
                stopPlayback()
                setVideoURI(uri)
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.seekTo(currentPositionMillis)
                    mp.setOnSeekCompleteListener {
                        start()
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("LIBRAS", "Erro ao preparar vídeo LIBRAS: ${e.message}", e)
        }
    }

}
