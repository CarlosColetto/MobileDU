package com.mobile.mobiledu

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.mobile.mobiledu.activities.SettingsActivity
import utils.ShakeDetector
import android.content.Context
import android.hardware.Sensor
import android.view.KeyEvent
import android.os.SystemClock
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.SearchEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import utils.DUVolumeInterceptor

class DUInitializer : Application.ActivityLifecycleCallbacks {
    private var sensorManager: SensorManager? = null
    private var shakeDetector: ShakeDetector? = null
    private var lastVolumeUpTime = 0L

 override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
     if (activity is com.mobile.mobiledu.activities.SettingsActivity) {
         return
     }
     // Ícone flutuante
     val icon = ImageView(activity).apply {
         setImageResource(R.drawable.dutransparente)
         setOnClickListener {
             val intent = Intent(activity, SettingsActivity::class.java)
             activity.startActivity(intent)
         }
     }

     val rootView = activity.findViewById<FrameLayout>(android.R.id.content)
     rootView.post {
         val size = activity.resources.getDimensionPixelSize(R.dimen.fab_icon_size)
         val margin = activity.resources.getDimensionPixelSize(R.dimen.fab_margin)

         val params = FrameLayout.LayoutParams(size, size)
         params.marginEnd = margin
         params.topMargin = margin
         params.gravity = Gravity.TOP or Gravity.END
         icon.layoutParams = params
         rootView.addView(icon)
     }

     // Interceptar teclas com proxy de Window.Callback
     val originalCallback = activity.window.callback
     activity.window.callback = object : Window.Callback {
         override fun dispatchKeyEvent(event: KeyEvent): Boolean {
             DUVolumeInterceptor.interceptKeyEvent(activity, event)
             return originalCallback?.dispatchKeyEvent(event) ?: false
         }

         override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
             return originalCallback?.dispatchKeyShortcutEvent(event) ?: false
         }

         override fun dispatchTouchEvent(event: MotionEvent): Boolean {
             return originalCallback?.dispatchTouchEvent(event) ?: false
         }

         override fun dispatchTrackballEvent(event: MotionEvent): Boolean {
             return originalCallback?.dispatchTrackballEvent(event) ?: false
         }

         override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
             return originalCallback?.dispatchGenericMotionEvent(event) ?: false
         }

         override fun dispatchPopulateAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent): Boolean {
             return originalCallback?.dispatchPopulateAccessibilityEvent(event) ?: false
         }

         override fun onCreatePanelView(featureId: Int): View? {
             return originalCallback?.onCreatePanelView(featureId)
         }

         override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
             return originalCallback?.onCreatePanelMenu(featureId, menu) ?: false
         }

         override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
             return originalCallback?.onPreparePanel(featureId, view, menu) ?: false
         }

         override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
             return originalCallback?.onMenuOpened(featureId, menu) ?: false
         }

         override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
             return originalCallback?.onMenuItemSelected(featureId, item) ?: false
         }

         override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) {
             originalCallback?.onWindowAttributesChanged(attrs)
         }

         override fun onContentChanged() {
             originalCallback?.onContentChanged()
         }

         override fun onWindowFocusChanged(hasFocus: Boolean) {
             originalCallback?.onWindowFocusChanged(hasFocus)
         }

         override fun onAttachedToWindow() {
             originalCallback?.onAttachedToWindow()
         }

         override fun onDetachedFromWindow() {
             originalCallback?.onDetachedFromWindow()
         }

         override fun onPanelClosed(featureId: Int, menu: Menu) {
             originalCallback?.onPanelClosed(featureId, menu)
         }

         override fun onSearchRequested(): Boolean {
             return originalCallback?.onSearchRequested() ?: false
         }

         override fun onSearchRequested(searchEvent: SearchEvent): Boolean {
             return originalCallback?.onSearchRequested(searchEvent) ?: false
         }

         override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? {
             return originalCallback?.onWindowStartingActionMode(callback)
         }

         override fun onWindowStartingActionMode(callback: ActionMode.Callback, type: Int): ActionMode? {
             return originalCallback?.onWindowStartingActionMode(callback, type)
         }

         override fun onActionModeStarted(mode: ActionMode) {
             originalCallback?.onActionModeStarted(mode)
         }

         override fun onActionModeFinished(mode: ActionMode) {
             originalCallback?.onActionModeFinished(mode)
         }
     }
 }

    override fun onActivityResumed(activity: Activity) {
        sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        shakeDetector = ShakeDetector {
            if (activity !is SettingsActivity) {
                val intent = Intent(activity, SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                activity.startActivity(intent)
            }
        }

        accelerometer?.let {
            sensorManager?.registerListener(shakeDetector, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        sensorManager?.unregisterListener(shakeDetector)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}