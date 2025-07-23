package com.example.jaigurudevapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.jaigurudevapp.navigation.JaigurudevNavigation
import com.example.jaigurudevapp.ui.theme.JaigurudevAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        enableEdgeToEdge()
        setContent {
            JaigurudevAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JaigurudevApp()
                }
            }
        }
    }
}

@Composable
fun JaigurudevApp() {
    val navController = rememberNavController()
    
    JaigurudevNavigation(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JaigurudevAppTheme {
        JaigurudevApp()
    }
} 