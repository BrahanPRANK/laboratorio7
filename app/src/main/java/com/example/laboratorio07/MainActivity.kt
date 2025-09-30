package com.example.laboratorio07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.laboratorio07.ui.theme.Laboratorio07Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio07Theme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenUser() // 👈 aquí se muestra tu pantalla con Room y Compose
                }
            }
        }
    }
}
