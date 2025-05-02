package com.mobile.libdu
import android.app.Application
object DUAppInitializer {
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(DUInitializer())
    }
}