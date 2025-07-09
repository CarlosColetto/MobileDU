package utils
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.TypedValue

object TextColorManager {
    // Define a cor padrão; pode ser modificada pelo usuário.
    var currentTextColor: Int = Color.BLACK
    // Tamanho atual dos textos (em sp); padrão 14sp.
    var currentTextSize: Float = 14f

    // Flag para indicar se as configurações já foram alteradas
    var isConfigured: Boolean = false

    /**
     * Percorre recursivamente a hierarquia de views e, para cada TextView encontrada, atualiza a cor do texto.
     */
    fun updateTextColor(view: View) {
        when (view) {
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    updateTextColor(view.getChildAt(i))
                }
            }
            is TextView -> {
                view.setTextColor(currentTextColor)
            }
        }
    }

fun updateTextSize(view: View) {
    when (view) {
        is ViewGroup -> for (i in 0 until view.childCount) updateTextSize(view.getChildAt(i))
        is TextView -> view.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSize) // CORRIGIDO
    }
}

fun updateFontFamily(view: View) {
    when (view) {
        is ViewGroup -> for (i in 0 until view.childCount) updateFontFamily(view.getChildAt(i))
        is TextView -> view.typeface = Typeface.create(fontFamily, Typeface.NORMAL) // ✅ já correto se usar o mesmo nome
    }
}
    /**
     * Atualiza ambos os atributos (cor e tamanho) de todos os TextViews na hierarquia da view.
     */
    fun updateTextAppearance(view: View) {
        updateTextColor(view)
        updateTextSize(view)
        updateFontFamily(view)
    }

    fun savePreferences(context: Context) {
        val prefs = context.getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("textColor", currentTextColor)
            .putFloat("textSize", currentTextSize)
            .putString("fontFamily", fontFamily)
            .apply()
    }

    fun loadPreferences(context: Context) {
        val prefs = context.getSharedPreferences("du_prefs", Context.MODE_PRIVATE)
        currentTextColor = prefs.getInt("textColor", Color.BLACK)
        currentTextSize = prefs.getFloat("textSize", 14f)
        fontFamily = prefs.getString("fontFamily", "sans-serif") ?: "sans-serif"
    }

     //Adicionado para compatibilidade com TextSettingsFragment
    var fontFamily: String = "sans-serif"
}
