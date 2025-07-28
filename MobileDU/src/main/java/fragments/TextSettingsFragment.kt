package fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobile.mobiledu.R
import config.AppConfig

class TextSettingsFragment : Fragment() {

    private lateinit var seekBarFontSize: SeekBar
    private lateinit var tvFontSizeValue: TextView
    private lateinit var spinnerFontType: Spinner
    private lateinit var tvPreview: TextView
    private lateinit var btnColorBlack: Button
    private lateinit var btnColorWhite: Button
    private lateinit var btnColorRed: Button
    private lateinit var btnColorBlue: Button
    private lateinit var btnColorPicker: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var btnReset: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        seekBarFontSize = view.findViewById(R.id.seekBarFontSize)
        tvFontSizeValue = view.findViewById(R.id.tvFontSizeValue)
        spinnerFontType = view.findViewById(R.id.spinnerFontType)
        tvPreview = view.findViewById(R.id.tvPreview)
        tvPreview.setBackgroundColor(AppConfig.color)

        btnColorBlack = view.findViewById(R.id.btnColorBlack)
        btnColorWhite = view.findViewById(R.id.btnColorWhite)
        btnColorRed = view.findViewById(R.id.btnColorRed)
        btnColorBlue = view.findViewById(R.id.btnColorBlue)
        btnColorPicker = view.findViewById(R.id.btnColorPicker)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnReset = view.findViewById(R.id.btnReset)

        AppConfig.load(requireContext())

        val fontOptions = listOf("sans-serif", "serif", "monospace")
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_font_spinner,
            fontOptions
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).typeface = Typeface.create(fontOptions[position], Typeface.NORMAL)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).typeface = Typeface.create(fontOptions[position], Typeface.NORMAL)
                return view
            }
        }
        spinnerFontType.adapter = adapter
        spinnerFontType.setSelection(fontOptions.indexOf(AppConfig.textFontName))
        spinnerFontType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                AppConfig.textFontName = fontOptions[position]
                AppConfig.save(requireContext())
                updatePreview()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        seekBarFontSize.progress = AppConfig.textSize.toInt()
        tvFontSizeValue.text = "${AppConfig.textSize.toInt()}"
        seekBarFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                AppConfig.textSize = progress.toFloat().coerceAtLeast(12f)
                tvFontSizeValue.text = "${AppConfig.textSize.toInt()}"
                AppConfig.save(requireContext())
                updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnColorBlack.setOnClickListener {
            AppConfig.textColor = Color.BLACK
            AppConfig.save(requireContext())
            updatePreview()
        }
        btnColorWhite.setOnClickListener {
            AppConfig.textColor = Color.WHITE
            AppConfig.save(requireContext())
            updatePreview()
        }
        btnColorRed.setOnClickListener {
            AppConfig.textColor = Color.RED
            AppConfig.save(requireContext())
            updatePreview()
        }
        btnColorBlue.setOnClickListener {
            AppConfig.textColor = Color.BLUE
            AppConfig.save(requireContext())
            updatePreview()
        }

        btnColorPicker.setOnClickListener {
            showColorPicker()
        }

        btnSave.setOnClickListener {
            val rootView = requireActivity().findViewById<ViewGroup>(android.R.id.content)
            rootView.post {
                com.mobile.mobiledu.DUSettingsApplier.applyToActivity(requireActivity())
                requireActivity().finish()
            }
        }

        btnCancel.setOnClickListener {
            requireActivity().finish()
        }

        btnReset.setOnClickListener {
            AppConfig.restoreDefaults(requireContext())
            AppConfig.save(requireContext())
            updatePreview()
        }

        AppConfig.save(requireContext())
        updatePreview()
    }

    override fun onResume() {
        super.onResume()
        AppConfig.load(requireContext())
        updatePreview()
    }


    private fun updatePreview() {
        tvPreview.setTextColor(AppConfig.textColor)
        tvPreview.textSize = AppConfig.textSize.toFloat()
        tvPreview.typeface = Typeface.create(AppConfig.textFontName, Typeface.NORMAL)
        tvPreview.setBackgroundColor(AppConfig.color)
    }

    private fun showColorPicker() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_color_palette, null)

        val colorButtons = mapOf(
            R.id.btnColorYellow to Color.parseColor("#FFFF00"),
            R.id.btnColorOrange to Color.parseColor("#FFA500"),
            R.id.btnColorDarkGreen to Color.parseColor("#006400"),
            R.id.btnColorGray to Color.parseColor("#555555"),
            R.id.btnColorPurple to Color.parseColor("#800080"),
            R.id.btnColorCyan to Color.parseColor("#00FFFF")
        )

        colorButtons.forEach { (buttonId, colorValue) ->
            dialogView.findViewById<Button>(buttonId).setOnClickListener {
                AppConfig.textColor = colorValue
                AppConfig.save(requireContext())
                updatePreview()
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Escolha uma Cor")
            .setView(dialogView)
            .setNegativeButton("Fechar", null)
            .show()
    }
}
