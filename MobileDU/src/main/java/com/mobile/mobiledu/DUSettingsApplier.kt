package com.mobile.mobiledu

import android.app.Activity
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import config.AppConfig

object DUSettingsApplier {

    fun applyToActivity(activity: Activity) {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        AppConfig.load(activity)
        rootView.post {
            applyTextAppearance(rootView)
        }
    }

    private fun applyTextAppearance(view: View) {
        if (view is TextView) {
            view.setTextColor(AppConfig.textColor)
            view.textSize = AppConfig.textSize.toFloat()
            view.typeface = Typeface.create(AppConfig.textFontName, Typeface.NORMAL)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                applyTextAppearance(view.getChildAt(i))
            }
        }
    }
}
