package com.example.idleautobattler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.idleautobattler.Screen_Combat.TelaAutoBattlerImersiva
import com.example.idleautobattler.ui.theme.IdleAutoBattlerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IdleAutoBattlerTheme {
                TelaAutoBattlerImersiva()
            }
        }
    }
}
