package adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import fragments.ContrastSettingsFragment
import fragments.EnvironmentSettingsFragment
import fragments.TextSettingsFragment
import fragments.SoundSettingsFragment

class SettingsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TextSettingsFragment()           // Texto
            1 -> EnvironmentSettingsFragment()    // Ambiente
            2 -> SoundSettingsFragment()          // Som
            3 -> ContrastSettingsFragment()       //Contraste
//            3 -> VideoSettingsFragment()          // Vídeo
//            4 -> InputSettingsFragment()          // Entrada
//            5 -> NotificationsSettingsFragment()  // Notificações
//            6 -> GestureSettingsFragment()       // Gestos
            else -> Fragment()
        }
    }
}
