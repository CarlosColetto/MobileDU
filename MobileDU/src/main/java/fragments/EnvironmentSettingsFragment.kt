package fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobile.mobiledu.R
import config.AppConfig

class EnvironmentSettingsFragment : Fragment() {

    private lateinit var btnColorRed: Button
    private lateinit var btnColorGreen: Button
    private lateinit var btnColorBlue: Button
    private lateinit var btnColorWhite: Button
    private lateinit var switchInvertColors: Switch
    private lateinit var seekBarIntensity: SeekBar
    private lateinit var tvIntensityValue: TextView
    private lateinit var switchAutoBrightness: Switch
    private lateinit var seekBarBrightness: SeekBar
    private lateinit var tvBrightnessValue: TextView
    private lateinit var spinnerOrientation: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var viewPreview: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ambiente_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnColorRed = view.findViewById(R.id.btnColorRed)
        btnColorGreen = view.findViewById(R.id.btnColorGreen)
        btnColorBlue = view.findViewById(R.id.btnColorBlue)
        btnColorWhite = view.findViewById(R.id.btnColorWhite)
        switchInvertColors = view.findViewById(R.id.switchInvertColors)
        seekBarIntensity = view.findViewById(R.id.seekBarIntensity)
        tvIntensityValue = view.findViewById(R.id.tvIntensityValue)
        switchAutoBrightness = view.findViewById(R.id.switchAutoBrightness)
        seekBarBrightness = view.findViewById(R.id.seekBarBrightness)
        tvBrightnessValue = view.findViewById(R.id.tvBrightnessValue)
        spinnerOrientation = view.findViewById(R.id.spinnerOrientation)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)
        viewPreview = view.findViewById(R.id.viewPreview)

        AppConfig.save(requireContext())
        updatePreview()

        btnColorRed.setOnClickListener {
            AppConfig.color = Color.parseColor("#F44336")
            AppConfig.save(requireContext())
            updatePreview()
        }

        btnColorGreen.setOnClickListener {
            AppConfig.color = Color.parseColor("#4CAF50")
            AppConfig.save(requireContext())
            updatePreview()
        }

        btnColorBlue.setOnClickListener {
            AppConfig.color = Color.parseColor("#2196F3")
            AppConfig.save(requireContext())
            updatePreview()
        }

        btnColorWhite.setOnClickListener {
            AppConfig.color = Color.parseColor("#FFFFFF")
            AppConfig.save(requireContext())
            updatePreview()
        }

        switchInvertColors.isChecked = AppConfig.invertColors
        switchInvertColors.setOnCheckedChangeListener { _, isChecked ->
            AppConfig.invertColors = isChecked
            AppConfig.save(requireContext())
            updatePreview()
        }

        seekBarIntensity.progress = AppConfig.intensity
        tvIntensityValue.text = "${AppConfig.intensity}%"
        seekBarIntensity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                AppConfig.intensity = progress
                tvIntensityValue.text = "$progress%"
                AppConfig.save(requireContext())
                updatePreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        switchAutoBrightness.isChecked = AppConfig.autoBrightness
        switchAutoBrightness.setOnCheckedChangeListener { _, isChecked ->
            AppConfig.autoBrightness = isChecked
            seekBarBrightness.isEnabled = !isChecked
            AppConfig.save(requireContext())
            updatePreview()
        }

        seekBarBrightness.progress = AppConfig.brightness
        tvBrightnessValue.text = "${AppConfig.brightness}%"
        seekBarBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                AppConfig.brightness = progress
                tvBrightnessValue.text = "$progress%"
                AppConfig.save(requireContext())
                updatePreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val options = listOf("Automática", "Retrato", "Paisagem")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrientation.adapter = adapter
        spinnerOrientation.setSelection(when (AppConfig.orientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> 1
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> 2
            else -> 0
        })
        spinnerOrientation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                AppConfig.orientation = when (position) {
                    1 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    2 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
                AppConfig.save(requireContext())
                updatePreview()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSave.setOnClickListener {
            AppConfig.save(requireContext())
            val rootView = requireActivity().findViewById<ViewGroup>(android.R.id.content)
            rootView.post {
                com.mobile.mobiledu.EnvironmentApplier.applyToActivity(requireActivity())
                requireActivity().finish()
            }
        }

        btnCancel.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        AppConfig.load(requireContext())
        updatePreview()
    }

    private fun updatePreview() {
        var color = AppConfig.color
        if (AppConfig.invertColors) {
            val red = 255 - Color.red(color)
            val green = 255 - Color.green(color)
            val blue = 255 - Color.blue(color)
            color = Color.rgb(red, green, blue)
        }

        val alpha = (255 * (100 - AppConfig.intensity) / 100).coerceIn(0, 255)
        val colorWithAlpha = (alpha shl 24) or (color and 0x00FFFFFF)

        val brightnessFactor = AppConfig.brightness / 100f
        val r = (Color.red(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val g = (Color.green(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val b = (Color.blue(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)

        val finalColor = Color.argb(alpha, r, g, b)
        viewPreview.setBackgroundColor(finalColor)

        //  Atualiza o tamanho visual do preview conforme a orientação
        val params = viewPreview.layoutParams
        when (AppConfig.orientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
                params.width = (resources.displayMetrics.density * 120).toInt()
                params.height = (resources.displayMetrics.density * 180).toInt()
            }
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> {
                params.width = (resources.displayMetrics.density * 180).toInt()
                params.height = (resources.displayMetrics.density * 120).toInt()
            }
            else -> {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (resources.displayMetrics.density * 150).toInt()
            }
        }
        viewPreview.layoutParams = params
        viewPreview.requestLayout()
        viewPreview.invalidate()
    }

}
