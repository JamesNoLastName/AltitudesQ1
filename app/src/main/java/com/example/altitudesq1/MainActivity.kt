package com.example.altitudesq1

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altitudesq1.ui.theme.AltitudesQ1Theme
import kotlin.math.pow

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorMgr: SensorManager
    private var pressureSensor: Sensor? = null
    private var pressure by mutableStateOf(1000.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorMgr = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_PRESSURE)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                SensorDisplay(pressure)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        sensorMgr.unregisterListener(this)
    }
    override fun onResume() {
        super.onResume()
        pressureSensor?.let {
            sensorMgr.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // Pressure formula
            pressure = 44330 * (1 - (it.values[0] / 1013.25).pow(1 / 5.255))
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
@Composable
fun SensorDisplay(altitude: Double) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getAltitudeColor(altitude))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Altitude: ${"%.2f".format(altitude)} m",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}
fun getAltitudeColor(altitude: Double): Color {
    if (altitude < 500) {
        return Color.Cyan
    } else if (altitude in 500.0..2000.0) {
        return Color.Magenta
    } else if (altitude in 2000.0..11000.0) {
        return Color.Blue
    } else {
        return Color.Black
    }
}