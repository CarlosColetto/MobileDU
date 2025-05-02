package com.mobile.libdu
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.EditText

object DUSettingsApplier {

    fun applyToActivity(activity: Activity) {
        val prefs = activity.getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
        val color = prefs.getInt("textColor", Color.BLACK)
        val textSize = prefs.getFloat("textSize", 16f)

        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        rootView.post {
            applyRecursively(rootView, color, textSize)
        }
    }

    private fun applyRecursively(view: View, color: Int, textSize: Float) {
        if (view is TextView) {
            view.setTextColor(color)
            view.textSize = textSize
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                applyRecursively(view.getChildAt(i), color, textSize)
            }
        }
    }

}

