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
    val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
    rootView.post {
        utils.TextColorManager.updateTextAppearance(rootView)
    }
}

}

