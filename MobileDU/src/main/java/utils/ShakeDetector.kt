//package utils
//
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import kotlin.math.sqrt
//
//class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {
//    private var lastShakeTime = 0L
//    private val SHAKE_THRESHOLD_GRAVITY = 4.0f
//    private val SHAKE_SLOP_TIME_MS = 1000
//
//    override fun onSensorChanged(event: SensorEvent) {
//        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
//
//        val gX = event.values[0] / SensorManager.GRAVITY_EARTH
//        val gY = event.values[1] / SensorManager.GRAVITY_EARTH
//        val gZ = event.values[2] / SensorManager.GRAVITY_EARTH
//
//        val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)
//
//        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
//            val now = System.currentTimeMillis()
//            if (now - lastShakeTime > SHAKE_SLOP_TIME_MS) {
//                lastShakeTime = now
//                onShake()
//            }
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//}
package utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {

    private var lastShakeTimestamp = 0L
    private val SHAKE_THRESHOLD_GRAVITY = 4.0f
    private val SHAKE_SLOP_TIME_MS = 1000L // tempo mínimo entre shakes válidos
    private val SHAKE_WINDOW_MS = 1000L    // tempo máximo entre múltiplos picos
    private val SHAKE_COUNT_REQUIRED = 2  // número mínimo de picos

    private var shakeCount = 0

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val gX = event.values[0] / SensorManager.GRAVITY_EARTH
        val gY = event.values[1] / SensorManager.GRAVITY_EARTH
        val gZ = event.values[2] / SensorManager.GRAVITY_EARTH

        val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()

            // Reinicia o contador se passou da janela de tempo
            if (now - lastShakeTimestamp > SHAKE_WINDOW_MS) {
                shakeCount = 0
            }

            lastShakeTimestamp = now
            shakeCount++

            if (shakeCount >= SHAKE_COUNT_REQUIRED) {
                shakeCount = 0
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
