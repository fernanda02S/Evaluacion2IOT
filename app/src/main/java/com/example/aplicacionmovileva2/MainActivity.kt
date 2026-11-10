package com.example.aplicacionmovileva2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.aplicacionmovileva2.ui.MainApp
import com.example.aplicacionmovileva2.ui.screens.LoginScreen
import com.example.aplicacionmovileva2.ui.theme.AplicacionMovilEva2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionMovilEva2Theme {
                MainApp()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    AplicacionMovilEva2Theme {
        MainApp()
    }
}
