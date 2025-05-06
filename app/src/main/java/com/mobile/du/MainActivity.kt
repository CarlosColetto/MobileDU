package com.mobile.du
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import utils.TextColorManager
import com.mobile.libdu.activities.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        }
    override fun onResume() {
        super.onResume()
        try {
            com.mobile.libdu.DUSettingsApplier.applyToActivity(this)
            com.mobile.libdu.EnvironmentApplier.applyToActivity(this)
            com.mobile.libdu.SoundApplier.applyToActivity(this)


        } catch (e: Exception) {
            e.printStackTrace() // ou Log.e("MainActivity", "Erro ao aplicar configurações", e)
        }
    }

}