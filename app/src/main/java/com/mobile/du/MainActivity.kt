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

        //ao clicar no icone do DU, abre a Activity de configurações com abas
        //val icone = findViewById<View>(R.id.ic_du)
        //icone.setOnClickListener{val intent = Intent(this, SettingsActivity::class.java)
        //    startActivity(intent)
        }
    //}
    // Atualiza a aparência dos textos quando a MainActivity voltar ao foco
    //override fun onResume() {
    //    super.onResume()
    //    if(TextColorManager.isConfigured) {
    //        val rootView = findViewById<View>(android.R.id.content)
    //        TextColorManager.updateTextAppearance(rootView)
    //    }
    //}
    override fun onResume() {
        super.onResume()
        try {
            com.mobile.libdu.DUSettingsApplier.applyToActivity(this)
        } catch (e: Exception) {
            e.printStackTrace() // ou Log.e("MainActivity", "Erro ao aplicar configurações", e)
        }
    }
    //override fun onPostResume() {
    //    super.onPostResume()
    //    com.mobile.libdu.DUSettingsApplier.applyToActivity(this)
    //}
}