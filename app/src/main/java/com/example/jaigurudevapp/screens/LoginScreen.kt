package com.example.jaigurudevapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.jaigurudevapp.R
import com.example.jaigurudevapp.ui.theme.AccentGold
import com.example.jaigurudevapp.ui.theme.Primary
import com.example.jaigurudevapp.ui.theme.Secondary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val auth = Firebase.auth
    
    // Animation values
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000)
    )
    
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Gradient background
    val gradient = Brush.linearGradient(
        colors = listOf(
            Primary.copy(alpha = 0.8f),
            Secondary.copy(alpha = 0.6f),
            AccentGold.copy(alpha = 0.4f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f + animatedOffset * 500f, 1000f + animatedOffset * 500f)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(animatedAlpha)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            // Lottie Animation (Placeholder - replace with actual Lottie file)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🕉️",
                    fontSize = 48.sp,
                    color = Primary
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Title
            Text(
                text = "Jai Gurudev",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Welcome to Spiritual Journey",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Login Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Password")
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    if (isPasswordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        )
                    )
                    
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Login Button
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                isLoading = true
                                errorMessage = ""
                                
                                // Firebase Authentication
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            onLoginSuccess()
                                        } else {
                                            errorMessage = task.exception?.message ?: "Login failed"
                                        }
                                    }
                            } else {
                                errorMessage = "Please fill in all fields"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Sign In",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sign Up Link
                    TextButton(
                        onClick = onNavigateToSignup,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Don't have an account? Sign Up",
                            color = Primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Skip Login Button
                    OutlinedButton(
                        onClick = onLoginSuccess,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Primary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Primary, Secondary)
                            )
                        )
                    ) {
                        Text(
                            text = "Skip Login",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Footer
            Text(
                text = "जय गुरुदेव 🙏",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
} 