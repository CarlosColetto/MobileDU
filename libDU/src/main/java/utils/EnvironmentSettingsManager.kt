package utils

object EnvironmentSettingsManager {
    var customColor: Int = 0xFFFFFFFF.toInt() // branco padrão
    var invertColors: Boolean = false
    var intensityReduction: Int = 0         // 0% redução
    var autoBrightness: Boolean = true
    var brightness: Int = 50                // 50%
    var screenOrientation: Int = 0          // 0 para auto; usar ActivityInfo constants para outras
}