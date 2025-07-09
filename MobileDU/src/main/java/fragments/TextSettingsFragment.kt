
package fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobile.mobiledu.R
import utils.TextColorManager

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

    private var selectedFontSize: Int = 16
    private var selectedTextColor: Int = Color.WHITE
    private var selectedFontType: String = "sans-serif"

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
        btnColorBlack = view.findViewById(R.id.btnColorBlack)
        btnColorWhite = view.findViewById(R.id.btnColorWhite)
        btnColorRed = view.findViewById(R.id.btnColorRed)
        btnColorBlue = view.findViewById(R.id.btnColorBlue)
        btnColorPicker = view.findViewById(R.id.btnColorPicker)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnReset = view.findViewById(R.id.btnReset)

        TextColorManager.loadPreferences(requireContext()) // garante que os valores estejam carregados
        selectedFontSize = TextColorManager.currentTextSize.toInt()
        selectedTextColor = TextColorManager.currentTextColor
        selectedFontType = TextColorManager.fontFamily

        // Spinner com fontes visuais
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
        spinnerFontType.setSelection(fontOptions.indexOf(selectedFontType))
        spinnerFontType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFontType = fontOptions[position]
                updatePreview()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        seekBarFontSize.progress = selectedFontSize
        tvFontSizeValue.text = "${selectedFontSize}"
        seekBarFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedFontSize = progress.coerceAtLeast(12)
                tvFontSizeValue.text = "${selectedFontSize}"
                updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnColorBlack.setOnClickListener {
            selectedTextColor = Color.BLACK
            updatePreview()
        }
        btnColorWhite.setOnClickListener {
            selectedTextColor = Color.WHITE
            updatePreview()
        }
        btnColorRed.setOnClickListener {
            selectedTextColor = Color.RED
            updatePreview()
        }
        btnColorBlue.setOnClickListener {
            selectedTextColor = Color.BLUE
            updatePreview()
        }

        btnColorPicker.setOnClickListener {
            showColorPicker()
        }


        btnSave.setOnClickListener {
            TextColorManager.currentTextColor = selectedTextColor
            TextColorManager.currentTextSize = selectedFontSize.toFloat()
            TextColorManager.fontFamily = selectedFontType
            TextColorManager.isConfigured = true

            TextColorManager.savePreferences(requireContext()) //  Agora com as chaves corretas

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
            selectedFontSize = 16
            selectedTextColor = Color.WHITE
            selectedFontType = "sans-serif"
            seekBarFontSize.progress = 16
            spinnerFontType.setSelection(0)
            updatePreview()
        }

        updatePreview()
    }

    private fun updatePreview() {
        tvPreview.setTextColor(selectedTextColor)
        tvPreview.textSize = selectedFontSize.toFloat()
        tvPreview.typeface = Typeface.create(selectedFontType, Typeface.NORMAL)
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

    // Atribui a cor e atualiza o preview imediatamente ao clicar na cor
    colorButtons.forEach { (buttonId, colorValue) ->
        dialogView.findViewById<Button>(buttonId).setOnClickListener {
            selectedTextColor = colorValue
            updatePreview()
        }
    }

    AlertDialog.Builder(requireContext())
        .setTitle("Escolha uma Cor")
        .setView(dialogView)
        .setNegativeButton("Fechar", null) // Apenas um botão de fechar, já que a seleção é imediata
        .show()
}

}


