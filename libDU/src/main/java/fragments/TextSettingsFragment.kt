package fragments
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mobile.libdu.R
import utils.TextColorManager

class TextSettingsFragment : Fragment() {

    private var selectedColor: Int = TextColorManager.currentTextColor
    private var selectedTextSize: Float = TextColorManager.currentTextSize

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val previewText = view.findViewById<TextView>(R.id.previewText)
        previewText.text = "Texto de Exemplo"
        previewText.setTextColor(selectedColor)
        previewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize)

        // Configuração dos botões de cor
        val btnColorBlue = view.findViewById<Button>(R.id.btnColorBlue)
        val btnColorRed = view.findViewById<Button>(R.id.btnColorRed)
        val btnColorGreen = view.findViewById<Button>(R.id.btnColorGreen)
        val btnColorBlack = view.findViewById<Button>(R.id.btnColorBlack)

        // Remover tint se necessário
        btnColorBlue.backgroundTintList = null
        btnColorRed.backgroundTintList = null
        btnColorGreen.backgroundTintList = null
        btnColorBlack.backgroundTintList = null

        btnColorBlue.setOnClickListener {
            selectedColor = Color.parseColor("#00008B")
            previewText.setTextColor(selectedColor)
        }
        btnColorRed.setOnClickListener {
            selectedColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            previewText.setTextColor(selectedColor)
        }
        btnColorGreen.setOnClickListener {
            selectedColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            previewText.setTextColor(selectedColor)
        }
        btnColorBlack.setOnClickListener {
            selectedColor = Color.BLACK
            previewText.setTextColor(selectedColor)
        }

        // Configuração dos botões de ajuste do tamanho da fonte
        val btnDecreaseSize = view.findViewById<Button>(R.id.btnDecreaseSize)
        val btnIncreaseSize = view.findViewById<Button>(R.id.btnIncreaseSize)
        val sizeStep = 2f

        btnDecreaseSize.setOnClickListener {
            selectedTextSize = (selectedTextSize - sizeStep).coerceAtLeast(8f)
            previewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize)
        }
        btnIncreaseSize.setOnClickListener {
            selectedTextSize += sizeStep
            previewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize)
        }

        // Botões de ação: Salvar e Cancelar
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            // Atualiza o gerenciador apenas se houver alguma alteração
            val configChanged = (selectedColor != TextColorManager.currentTextColor) ||
                    (selectedTextSize != TextColorManager.currentTextSize)
            if (configChanged) {
                TextColorManager.currentTextColor = selectedColor
                TextColorManager.currentTextSize = selectedTextSize
                TextColorManager.isConfigured = true
                // Atualiza a aparência na Activity que chamou
                val rootView = requireActivity().findViewById<View>(android.R.id.content)
                TextColorManager.updateTextAppearance(rootView)
            }
            // Fecha a Activity de configurações para voltar à tela que chamou
            requireActivity().finish()
        }

        btnCancel.setOnClickListener {
            // Reverte as alterações no preview para os valores atuais globais
            selectedColor = TextColorManager.currentTextColor
            selectedTextSize = TextColorManager.currentTextSize
            previewText.setTextColor(selectedColor)
            previewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize)
            // Fecha a Activity de configurações para voltar à tela que chamou
            requireActivity().finish()
        }
    }
}
