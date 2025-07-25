/*
package fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import config.AppConfig
import com.mobile.mobiledu.R

class ContrastSettingsFragment : Fragment() {

    private lateinit var preview: View
    private lateinit var spinner: Spinner
    private val contrastOptions = listOf(
        "Claro (Fundo Branco, Texto Preto)" to Pair("#FFFFFF", "#000000"),
        "Escuro (Fundo Preto, Texto Branco)" to Pair("#000000", "#FFFFFF"),
        "Azul Escuro e Branco" to Pair("#003366", "#FFFFFF"),
        "Cinza Escuro e Amarelo" to Pair("#2E2E2E", "#FFFF99"),
        "Verde Escuro e Branco" to Pair("#004400", "#FFFFFF")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contrast_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preview = view.findViewById(R.id.viewContrastPreview)
        spinner = view.findViewById(R.id.spinnerContrastOptions)
        val btnSave = view.findViewById<Button>(R.id.btnSaveContrast)
        val btnCancel = view.findViewById<Button>(R.id.btnCancelContrast)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            contrastOptions.map { it.first }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val (bg, fg) = contrastOptions[position].second
                preview.setBackgroundColor(Color.parseColor(bg))
                if (preview is TextView) (preview as TextView).setTextColor(Color.parseColor(fg))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnSave.setOnClickListener {
            val (bg, fg) = contrastOptions[spinner.selectedItemPosition].second
            AppConfig.color = Color.parseColor(bg)
            AppConfig.textColor = Color.parseColor(fg)
            AppConfig.save(requireContext())
            Toast.makeText(context, "Contraste salvo!", Toast.LENGTH_SHORT).show()
        }

        btnCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
*/
package fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.mobile.mobiledu.R
import config.AppConfig

class ContrastSettingsFragment : Fragment() {

    private lateinit var preview: TextView
    private lateinit var contrastRoot: ViewGroup
    private var selectedButton: Button? = null
    private var selectedBgColor: Int = Color.WHITE
    private var selectedFgColor: Int = Color.BLACK

    private val contrastOptions = listOf(
        Triple("Preto no Branco", "#FFFFFF", "#000000"),
        Triple("Branco no Preto", "#000000", "#FFFFFF"),
        Triple("Branco no Azul Escuro", "#003366", "#FFFFFF"),
        Triple("Amarelo no Cinza Escuro", "#2E2E2E", "#FFFF99"),
        Triple("Branco no Verde Escuro", "#004400", "#FFFFFF")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contrast_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preview = view.findViewById(R.id.viewContrastPreview)
        contrastRoot = view.findViewById(R.id.contrastRoot)
        val btnSave = view.findViewById<Button>(R.id.btnSaveContrast)
        val btnCancel = view.findViewById<Button>(R.id.btnCancelContrast)

        contrastOptions.forEachIndexed { index, (label, bgColorHex, textColorHex) ->
            val btn = Button(requireContext()).apply {
                text = label
                setBackgroundColor(Color.parseColor(bgColorHex))
                setTextColor(Color.parseColor(textColorHex))
                textSize = 18f
                setPadding(16, 16, 16, 16)
                setOnClickListener { onContrastSelected(this, bgColorHex, textColorHex) }
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    180
                    //ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }
            }

            contrastRoot.addView(btn, contrastRoot.childCount - 2) // insere antes do preview e dos botões de ação
        }

        btnSave.setOnClickListener {
            AppConfig.color = selectedBgColor
            AppConfig.textColor = selectedFgColor
            AppConfig.save(requireContext())
            Toast.makeText(requireContext(), "Contraste salvo!", Toast.LENGTH_SHORT).show()
        }

        btnCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onContrastSelected(button: Button, bgColorHex: String, textColorHex: String) {
        selectedButton?.background = GradientDrawable().apply {
            setColor((selectedButton?.background as? GradientDrawable)?.color?.defaultColor ?: Color.TRANSPARENT)
            cornerRadius = 8f
        }

        selectedButton = button
        selectedBgColor = Color.parseColor(bgColorHex)
        selectedFgColor = Color.parseColor(textColorHex)

        // Atualiza preview
        preview.setBackgroundColor(selectedBgColor)
        preview.setTextColor(selectedFgColor)

        // Adiciona borda preta ao botão selecionado
        val border = GradientDrawable().apply {
            setColor(selectedBgColor)
            setStroke(8, Color.RED)
            cornerRadius = 8f
        }
        button.background = border
    }
}
