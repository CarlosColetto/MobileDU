package fragments

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobile.libdu.R
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


    // Cores pré-definidas para demonstração
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
        // Obter referências dos componentes
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

        // Remover tint se necessário
        btnColorBlue.backgroundTintList = null
        btnColorWhite.backgroundTintList = null
        btnColorGreen.backgroundTintList = null
        btnColorRed.backgroundTintList = null


        // Inicializa os valores com base nas configurações atuais
        selectedCustomColor = EnvironmentSettingsManager.customColor
        selectedInvert = EnvironmentSettingsManager.invertColors
        selectedIntensity = EnvironmentSettingsManager.intensityReduction
        selectedAutoBrightness = EnvironmentSettingsManager.autoBrightness
        selectedBrightness = EnvironmentSettingsManager.brightness
        selectedOrientation = EnvironmentSettingsManager.screenOrientation

        // Atualiza a view de preview
        updatePreview()

        // Configurar os botões de cor
        btnColorRed.setOnClickListener {
            selectedCustomColor = Color.parseColor("#F44336") // vermelho
            updatePreview()
        }
        btnColorGreen.setOnClickListener {
            selectedCustomColor = Color.parseColor("#4CAF50") // verde
            updatePreview()
        }
        btnColorBlue.setOnClickListener {
            selectedCustomColor = Color.parseColor("#2196F3") // azul
            updatePreview()
        }
        btnColorWhite.setOnClickListener {
            selectedCustomColor = Color.parseColor("#FFFFFF") // branco
            updatePreview()
        }

        // Inverter cores
        switchInvertColors.isChecked = selectedInvert
        switchInvertColors.setOnCheckedChangeListener { _, isChecked ->
            selectedInvert = isChecked
            updatePreview()
        }

        // SeekBar para intensidade (simula redução das cores brilhantes, por exemplo, alterando opacidade)
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

        // Brilho automático e manual
        switchAutoBrightness.isChecked = selectedAutoBrightness
        switchAutoBrightness.setOnCheckedChangeListener { _, isChecked ->
            selectedAutoBrightness = isChecked
            // Se for automático, desabilita o SeekBar; senão, habilita
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

        // Configurar o Spinner para orientação da tela
        // Cria uma lista de opções
        val options = listOf("Automática", "Retrato", "Paisagem")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrientation.adapter = adapter
        // Define a seleção com base no valor atual (0 = Auto, 1 = Portrait, 2 = Landscape)
        spinnerOrientation.setSelection(when(selectedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> 1
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> 2
            else -> 0
        })
        spinnerOrientation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                selectedOrientation = when(position) {
                    1 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    2 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


// Botão Salvar: aplica as alterações e propaga para a tela chamadora
        btnSave.setOnClickListener {
            // Verifica se houve alteração nas configurações
            val configChanged = (selectedCustomColor != EnvironmentSettingsManager.customColor) ||
                    (selectedInvert != EnvironmentSettingsManager.invertColors) ||
                    (selectedIntensity != EnvironmentSettingsManager.intensityReduction) ||
                    (selectedAutoBrightness != EnvironmentSettingsManager.autoBrightness) ||
                    (selectedBrightness != EnvironmentSettingsManager.brightness) ||
                    (selectedOrientation != EnvironmentSettingsManager.screenOrientation)
            if (configChanged) {
                // Atualiza o gerenciador
                EnvironmentSettingsManager.customColor = selectedCustomColor
                EnvironmentSettingsManager.invertColors = selectedInvert
                EnvironmentSettingsManager.intensityReduction = selectedIntensity
                EnvironmentSettingsManager.autoBrightness = selectedAutoBrightness
                EnvironmentSettingsManager.brightness = selectedBrightness
                EnvironmentSettingsManager.screenOrientation = selectedOrientation

                // Propaga as alterações para a tela chamadora
                val rootView = requireActivity().findViewById<View>(android.R.id.content)
                updateAmbienteAppearance(rootView)
            }
            // Fecha a Activity de configurações para voltar à tela que chamou
            requireActivity().finish()
        }

        // Botão Cancelar: reverte as alterações no preview para os valores atuais globais
        btnCancel.setOnClickListener {
            // Reverte os valores locais para os valores atuais do gerenciador
            selectedCustomColor = EnvironmentSettingsManager.customColor
            selectedInvert = EnvironmentSettingsManager.invertColors
            selectedIntensity = EnvironmentSettingsManager.intensityReduction
            selectedAutoBrightness = EnvironmentSettingsManager.autoBrightness
            selectedBrightness = EnvironmentSettingsManager.brightness
            selectedOrientation = EnvironmentSettingsManager.screenOrientation
            updatePreview()
            requireActivity().finish()
        }

    }

    // Função para propagar as alterações para a tela que chamou
    private fun updateAmbienteAppearance(rootView: View) {
        // Neste exemplo, reutilizamos a lógica de updatePreview() para aplicar o fundo
        var color = selectedCustomColor
        if (selectedInvert) {
            color = invertColor(color)
        }
        val alpha = (255 * (100 - selectedIntensity) / 100).coerceIn(0, 255)
        val colorWithAlpha = (alpha shl 24) or (color and 0x00FFFFFF)
        val brightnessFactor = selectedBrightness / 100f
        val r = (Color.red(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val g = (Color.green(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val b = (Color.blue(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val finalColor = Color.argb(alpha, r, g, b)
        rootView.setBackgroundColor(finalColor)
        // Ajusta também a orientação da tela, se necessário
        requireActivity().requestedOrientation = selectedOrientation
    }
    // Função auxiliar para inverter uma cor
    private fun invertColor(color: Int): Int {
        val red = 255 - Color.red(color)
        val green = 255 - Color.green(color)
        val blue = 255 - Color.blue(color)
        return Color.rgb(red, green, blue)
    }

    private fun updatePreview() {
        // Exemplo de atualização:
        // Define o fundo da view de preview com a cor escolhida
        // Se a opção inverter estiver ativa, inverte a cor (simplesmente subtraindo os componentes RGB de 255)
        var color = selectedCustomColor
        if (selectedInvert) {
            val red = 255 - Color.red(color)
            val green = 255 - Color.green(color)
            val blue = 255 - Color.blue(color)
            color = Color.rgb(red, green, blue)
        }
        // Aplicar redução de intensidade: simulamos diminuindo a opacidade conforme a porcentagem
        val alpha = (255 * (100 - selectedIntensity) / 100).coerceIn(0, 255)
        val colorWithAlpha = (alpha shl 24) or (color and 0x00FFFFFF)
        // Aplicar ajuste de brilho (simplesmente alterando o valor de brilho – aqui, de forma demonstrativa, ajusta a opacidade adicional)
        // Em uma implementação real, pode-se aplicar um ColorMatrix
        val brightnessFactor = selectedBrightness / 100f
        // Para demonstrar, multiplicamos os componentes RGB pelo brightnessFactor (limitado a 255)
        val r = (Color.red(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val g = (Color.green(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val b = (Color.blue(colorWithAlpha) * brightnessFactor).toInt().coerceAtMost(255)
        val finalColor = Color.argb(alpha, r, g, b)

        viewPreview.setBackgroundColor(finalColor)
    }
}



