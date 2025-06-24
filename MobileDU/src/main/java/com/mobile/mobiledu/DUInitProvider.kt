package com.mobile.mobiledu
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.util.Log

class DUInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        val context = context ?: return false

        if (context is Application) {
            DUAppInitializer.init(context)
            Log.d("DUInitProvider", "MobileDU inicializada via ContentProvider.")
        } else {
            // Em alguns casos pode ser um ContextThemeWrapper, então forçamos o applicationContext
            val app = context.applicationContext as? Application
            app?.let { DUAppInitializer.init(it) }
        }

        return true
    }

    override fun insert(uri: android.net.Uri, values: ContentValues?): android.net.Uri? = null
    override fun query(uri: android.net.Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: android.net.Uri): String? = null
    override fun update(uri: android.net.Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun delete(uri: android.net.Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
