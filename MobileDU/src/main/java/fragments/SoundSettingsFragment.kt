package fragments

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobile.mobiledu.R
import config.AppConfig

class SoundSettingsFragment : Fragment() {

    private lateinit var switchMute: Switch
    private lateinit var switchScreenReader: Switch
    private lateinit var switchEnableLibras: Switch
    private lateinit var seekBarSpeed: SeekBar
    private lateinit var tvSpeedValue: TextView
    private lateinit var tvCurrentVolume: TextView
    private lateinit var btnVolumeUp: ImageButton
    private lateinit var btnVolumeDown: ImageButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnStop: ImageButton
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var audioManager: AudioManager
    private var currentVolume: Int = 5
    private var currentSpeed: Float = 1.0f
    private var isMuted: Boolean = false
    private var screenReaderEnabled: Boolean = false
    private var librasEnabled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_sound_settings,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        switchMute = view.findViewById(R.id.switchMute)
        switchScreenReader = view.findViewById(R.id.switchScreenReader)
        switchEnableLibras = view.findViewById(R.id.switchEnableLibras)
        seekBarSpeed = view.findViewById(R.id.seekBarSpeed)
        tvSpeedValue = view.findViewById(R.id.tvSpeedValue)
        tvCurrentVolume = view.findViewById(R.id.tvCurrentVolume)
        btnVolumeUp = view.findViewById(R.id.btnVolumeUp)
        btnVolumeDown = view.findViewById(R.id.btnVolumeDown)
        btnPause = view.findViewById(R.id.btnPause)
        btnStop = view.findViewById(R.id.btnStop)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)

        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentVolume = AppConfig.volume
        currentSpeed = AppConfig.speechRate
        isMuted = AppConfig.isMuted
        screenReaderEnabled = AppConfig.screenReaderEnabled
        librasEnabled = AppConfig.librasEnabled

        switchMute.isChecked = isMuted
        switchScreenReader.isChecked = screenReaderEnabled
        switchEnableLibras.isChecked = librasEnabled

        tvCurrentVolume.text = "Volume: $currentVolume"
        seekBarSpeed.progress = (currentSpeed * 100).toInt()
        tvSpeedValue.text = "${String.format("%.1f", currentSpeed)}x"

        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentSpeed = progress / 100f
                tvSpeedValue.text = "${String.format("%.1f", currentSpeed)}x"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnVolumeUp.setOnClickListener {
            currentVolume = (currentVolume + 1).coerceAtMost(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
            tvCurrentVolume.text = "Volume: $currentVolume"
        }

        btnVolumeDown.setOnClickListener {
            currentVolume = (currentVolume - 1).coerceAtLeast(0)
            tvCurrentVolume.text = "Volume: $currentVolume"
        }

        btnSave.setOnClickListener {
            AppConfig.isMuted = switchMute.isChecked
            AppConfig.screenReaderEnabled = switchScreenReader.isChecked
            AppConfig.speechRate = currentSpeed
            AppConfig.volume = currentVolume
            AppConfig.librasEnabled = switchEnableLibras.isChecked

            AppConfig.save(requireContext())
            Toast.makeText(requireContext(), "Configurações salvas", Toast.LENGTH_SHORT).show()
        }

        btnCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
