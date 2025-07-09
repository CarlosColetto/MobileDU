package com.mobile.du
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
   //
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
            com.mobile.mobiledu.DUSettingsApplier.applyToActivity(this)
            com.mobile.mobiledu.EnvironmentApplier.applyToActivity(this)
            com.mobile.mobiledu.SoundApplier.applyToActivity(this)

        } catch (e: Exception) {
            e.printStackTrace() // ou Log.e("MainActivity", "Erro ao aplicar configurações", e)
        }
    }

}