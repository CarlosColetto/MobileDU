package com.mobile.libdu
import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView

object ConfigDU {

    fun applyFontStyle(
        activity: Activity,
        fontSizeSp: Float,
        fontColorHex: String
    ) {
        val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        applyStyleRecursively(rootView, fontSizeSp, fontColorHex)
    }

    private fun applyStyleRecursively(viewGroup: ViewGroup, fontSizeSp: Float, fontColorHex: String) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            when (view) {
                is TextView -> {
                    view.textSize = fontSizeSp
                    view.setTextColor(Color.parseColor(fontColorHex))
                }
                is ViewGroup -> applyStyleRecursively(view, fontSizeSp, fontColorHex)
            }
        }
    }
}


