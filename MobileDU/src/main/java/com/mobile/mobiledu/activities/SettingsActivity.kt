package com.mobile.mobiledu.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import adapters.SettingsPagerAdapter
import com.mobile.mobiledu.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // Cria o adapter que fornecerá os fragmentos para cada aba
        val adapter = SettingsPagerAdapter(this)
        viewPager.adapter = adapter

        val tabIcons = arrayOf(
            R.drawable.texto,       // Aba Texto
            R.drawable.ambiente,    // Aba Ambiente
            R.drawable.som,         // Aba Som
            R.drawable.contraste,   // Aba Contraste
            R.drawable.entrada,     // Aba Entrada
            R.drawable.notificacoes, // Aba Notificações
            R.drawable.gestos,       // Aba Gestos
            R.drawable.zoom          // Aba Zoom
        )

        // Configura o TabLayoutMediator para definir os ícones nas abas
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Obtém o drawable usando ContextCompat para compatibilidade
            val iconDrawable = ContextCompat.getDrawable(this, tabIcons[position])
            tab.icon = iconDrawable
        }.attach()

    }
}