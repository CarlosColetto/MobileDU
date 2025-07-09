package com.mobile.mobiledu
import android.app.Activity
import android.view.ViewGroup

object DUSettingsApplier {

fun applyToActivity(activity: Activity) {
    val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
    utils.TextColorManager.loadPreferences(activity)  //carrega configurações salvas
    rootView.post {
        utils.TextColorManager.updateTextAppearance(rootView)
    }
}

}

