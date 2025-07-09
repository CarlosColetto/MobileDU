package com.mobile.mobiledu

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.mobile.mobiledu.R
import com.mobile.mobiledu.activities.SettingsActivity

class DUInitializer : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        if (activity is com.mobile.mobiledu.activities.SettingsActivity) return

        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

        val icon = ImageView(activity).apply {
            setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.dutransparente))
            setOnClickListener {
                Log.d("DUInitializer", "Ícone clicado! Abrindo SettingsActivity...")
                val intent = Intent(activity, SettingsActivity::class.java)
                activity.startActivity(intent)
            }

            val size = (48 * resources.displayMetrics.density).toInt()
            layoutParams = FrameLayout.LayoutParams(size, size, Gravity.END or Gravity.TOP).apply {
                topMargin = 32
                marginEnd = 32
            }

            isClickable = true
            isFocusable = true
        }

        Log.d("DUInitializer", "Adicionando ícone à tela da activity: ${activity.localClassName}")
        rootView.post {
            rootView.addView(icon)
        }
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
