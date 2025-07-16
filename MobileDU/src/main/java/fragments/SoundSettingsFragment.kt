package fragments

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobile.mobiledu.R
import config.AppConfig
import java.util.*

class SoundSettingsFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var switchMute: Switch
    private lateinit var btnStop: ImageButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnVolumeDown: ImageButton
    private lateinit var btnVolumeUp: ImageButton
    private lateinit var tvCurrentVolume: TextView
    private lateinit var switchScreenReader: Switch
    private lateinit var seekBarSpeed: SeekBar
    private lateinit var tvSpeedValue: TextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var audioManager: AudioManager
    private var tts: TextToSpeech? = null
    private var selectedSpeakingSpeed: Float = AppConfig.speechRate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(requireContext(), this)
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        AppConfig.volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(requireContext(), "Língua não suportada para TTS", Toast.LENGTH_SHORT).show()
            }
            tts?.setSpeechRate(AppConfig.speechRate)
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sound_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        switchMute = view.findViewById(R.id.switchMute)
        btnStop = view.findViewById(R.id.btnStop)
        btnPause = view.findViewById(R.id.btnPause)
        btnVolumeDown = view.findViewById(R.id.btnVolumeDown)
        btnVolumeUp = view.findViewById(R.id.btnVolumeUp)
        tvCurrentVolume = view.findViewById(R.id.tvCurrentVolume)
        switchScreenReader = view.findViewById(R.id.switchScreenReader)
        seekBarSpeed = view.findViewById(R.id.seekBarSpeed)
        tvSpeedValue = view.findViewById(R.id.tvSpeedValue)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)

        switchMute.isChecked = false
        switchScreenReader.isChecked = AppConfig.isVoiceEnabled
        updateVolumeText()

        seekBarSpeed.max = 150
        seekBarSpeed.progress = ((selectedSpeakingSpeed - 0.5f) * 100).toInt()
        tvSpeedValue.text = String.format("%.1fx", selectedSpeakingSpeed)

        switchMute.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
            } else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
            }
        }

        btnStop.setOnClickListener {
            Toast.makeText(requireContext(), "Áudio parado", Toast.LENGTH_SHORT).show()
        }

        btnPause.setOnClickListener {
            Toast.makeText(requireContext(), "Áudio pausado", Toast.LENGTH_SHORT).show()
        }

        btnVolumeDown.setOnClickListener {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0)
            AppConfig.volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            updateVolumeText()
        }

        btnVolumeUp.setOnClickListener {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0)
            AppConfig.volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            updateVolumeText()
        }

        switchScreenReader.setOnCheckedChangeListener { _, isChecked ->
            AppConfig.isVoiceEnabled = isChecked
            if (isChecked) {
                Toast.makeText(requireContext(), "Leitor de tela ativado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Leitor de tela desativado", Toast.LENGTH_SHORT).show()
            }
        }

        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedSpeakingSpeed = 0.5f + progress / 100f
                AppConfig.speechRate = selectedSpeakingSpeed
                tvSpeedValue.text = String.format("%.1fx", selectedSpeakingSpeed)
                tts?.setSpeechRate(AppConfig.speechRate)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnSave.setOnClickListener {
            AppConfig.save(requireContext())

            if (AppConfig.isVoiceEnabled) {
                Toast.makeText(requireContext(), "Abrindo configurações de acessibilidade para ativar o TalkBack.", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }

            val rootView = requireActivity().findViewById<ViewGroup>(android.R.id.content)
            rootView.post {
                com.mobile.mobiledu.SoundApplier.applyToActivity(requireActivity())
                requireActivity().finish()
            }
        }

        btnCancel.setOnClickListener {
            Toast.makeText(requireContext(), "Configurações de Som descartadas", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    private fun updateVolumeText() {
        tvCurrentVolume.text = "Volume: ${AppConfig.volume}"
    }
}
