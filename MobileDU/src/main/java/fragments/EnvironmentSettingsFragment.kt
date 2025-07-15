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
import utils.EnvironmentSettingsManager

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

    private var selectedCustomColor: Int = EnvironmentSettingsManager.customColor
    private var selectedInvert: Boolean = EnvironmentSettingsManager.invertColors
    private var selectedIntensity: Int = EnvironmentSettingsManager.intensityReduction
    private var selectedAutoBrightness: Boolean = EnvironmentSettingsManager.autoBrightness
    private var selectedBrightness: Int = EnvironmentSettingsManager.brightness
    private var selectedOrientation: Int = EnvironmentSettingsManager.screenOrientation



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

        btnColorBlue.backgroundTintList = null
        btnColorWhite.backgroundTintList = null
        btnColorGreen.backgroundTintList = null
        btnColorRed.backgroundTintList = null

        //selectedCustomColor = EnvironmentSettingsManager.customColor
        //selectedInvert = EnvironmentSettingsManager.invertColors
        //selectedIntensity = EnvironmentSettingsManager.intensityReduction
        //selectedAutoBrightness = EnvironmentSettingsManager.autoBrightness
        //selectedBrightness = EnvironmentSettingsManager.brightness
        //selectedOrientation = EnvironmentSettingsManager.screenOrientation

        // 🔧 NOVA LEITURA DOS DADOS DIRETAMENTE DO SharedPreferences
        val prefs = requireContext().getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
        selectedCustomColor = prefs.getInt("envColor", Color.WHITE)
        selectedInvert = prefs.getBoolean("envInvert", false)
        selectedIntensity = prefs.getInt("envIntensity", 0)
        selectedAutoBrightness = prefs.getBoolean("envAutoBrightness", true)
        selectedBrightness = prefs.getInt("envBrightness", 100)
        selectedOrientation = prefs.getInt("envOrientation", -1)


        updatePreview()

        btnColorRed.setOnClickListener {
            selectedCustomColor = Color.parseColor("#F44336")
            updatePreview()
        }
        btnColorGreen.setOnClickListener {
            selectedCustomColor = Color.parseColor("#4CAF50")
            updatePreview()
        }
        btnColorBlue.setOnClickListener {
            selectedCustomColor = Color.parseColor("#2196F3")
            updatePreview()
        }
        btnColorWhite.setOnClickListener {
            selectedCustomColor = Color.parseColor("#FFFFFF")
            updatePreview()
        }

        switchInvertColors.isChecked = selectedInvert
        switchInvertColors.setOnCheckedChangeListener { _, isChecked ->
            selectedInvert = isChecked
            updatePreview()
        }

        seekBarIntensity.progress = selectedIntensity
        tvIntensityValue.text = "$selectedIntensity%"
        seekBarIntensity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedIntensity = progress
                tvIntensityValue.text = "$progress%"
                updatePreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        switchAutoBrightness.isChecked = selectedAutoBrightness
        switchAutoBrightness.setOnCheckedChangeListener { _, isChecked ->
            selectedAutoBrightness = isChecked
            seekBarBrightness.isEnabled = !isChecked
        }

        seekBarBrightness.progress = selectedBrightness
        tvBrightnessValue.text = "$selectedBrightness%"
        seekBarBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedBrightness = progress
                tvBrightnessValue.text = "$progress%"
                updatePreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val options = listOf("Automática", "Retrato", "Paisagem")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrientation.adapter = adapter
        spinnerOrientation.setSelection(when (selectedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> 1
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> 2
            else -> 0
        })
        spinnerOrientation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                selectedOrientation = when (position) {
                    1 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    2 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSave.setOnClickListener {
            val configChanged = (selectedCustomColor != EnvironmentSettingsManager.customColor) ||
                    (selectedInvert != EnvironmentSettingsManager.invertColors) ||
                    (selectedIntensity != EnvironmentSettingsManager.intensityReduction) ||
                    (selectedAutoBrightness != EnvironmentSettingsManager.autoBrightness) ||
                    (selectedBrightness != EnvironmentSettingsManager.brightness) ||
                    (selectedOrientation != EnvironmentSettingsManager.screenOrientation)

            if (configChanged) {
                EnvironmentSettingsManager.customColor = selectedCustomColor
                EnvironmentSettingsManager.invertColors = selectedInvert
                EnvironmentSettingsManager.intensityReduction = selectedIntensity
                EnvironmentSettingsManager.autoBrightness = selectedAutoBrightness
                EnvironmentSettingsManager.brightness = selectedBrightness
                EnvironmentSettingsManager.screenOrientation = selectedOrientation

                val prefs = requireContext().getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putInt("envColor", selectedCustomColor)
                    .putBoolean("envInvert", selectedInvert)
                    .putInt("envIntensity", selectedIntensity)
                    .putBoolean("envAutoBrightness", selectedAutoBrightness)
                    .putInt("envBrightness", selectedBrightness)
                    .putInt("envOrientation", selectedOrientation)
                    .apply()

                val rootView = requireActivity().findViewById<ViewGroup>(android.R.id.content)
                rootView.post {
                    com.mobile.mobiledu.EnvironmentApplier.applyToActivity(requireActivity())
                    requireActivity().finish()
                }
            } else {
                requireActivity().finish()
            }
        }

        btnCancel.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun updatePreview() {
        var color = selectedCustomColor
        if (selectedInvert) {
            val red = 255 - Color.red(color)
            val green = 255 - Color.green(color)
            val blue = 255 - Color.blue(color)
            color = Color.rgb(red, green, blue)
        }

        val alpha = (255 * (100 - selectedIntensity) / 100).coerceIn(0, 255)
        val colorWithAlpha = (alpha shl 24) or (color and 0x00FFFFFF)

        val brightnessFactor = selectedBrightness / 100f
        val r = (Color.red(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val g = (Color.green(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val b = (Color.blue(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)

        val finalColor = Color.argb(alpha, r, g, b)
        viewPreview.setBackgroundColor(finalColor)
    }

    private fun invertColor(color: Int): Int {
        val red = 255 - Color.red(color)
        val green = 255 - Color.green(color)
        val blue = 255 - Color.blue(color)
        return Color.rgb(red, green, blue)
    }
}
