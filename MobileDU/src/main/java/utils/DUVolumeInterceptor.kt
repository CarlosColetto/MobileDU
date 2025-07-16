package utils

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.view.KeyEvent
import com.mobile.mobiledu.activities.SettingsActivity

object DUVolumeInterceptor {
    private var lastVolumeUpTime = 0L
    private var clickCount = 0

    fun interceptKeyEvent(activity: Activity, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP && event.action == KeyEvent.ACTION_DOWN) {
            val now = SystemClock.uptimeMillis()

            // Ignorar se o botão estiver sendo mantido pressionado
            if (event.repeatCount > 0) return false

            if (now - lastVolumeUpTime < 500) {
                clickCount++
                if (clickCount >= 2) {
                    // Duplo clique confirmado
                    if (activity !is SettingsActivity) {
                        val intent = Intent(activity, SettingsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        activity.startActivity(intent)
                    }
                    // Resetar contagem
                    clickCount = 0
                    lastVolumeUpTime = 0L
                }
            } else {
                // Clique foi espaçado demais — começar nova contagem
                clickCount = 1
            }

            lastVolumeUpTime = now
        }

        return false // não consome o evento
    }
}
