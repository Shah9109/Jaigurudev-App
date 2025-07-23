package com.example.jaigurudevapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.jaigurudevapp.ui.theme.AccentGold
import com.example.jaigurudevapp.ui.theme.Primary
import com.example.jaigurudevapp.ui.theme.Secondary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val auth = Firebase.auth
    val firestore = Firebase.firestore
    
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
            Secondary.copy(alpha = 0.8f),
            Primary.copy(alpha = 0.6f),
            AccentGold.copy(alpha = 0.4f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f + animatedOffset * 500f, 1000f + animatedOffset * 500f)
    )
    
    fun validateForm(): Boolean {
        return when {
            fullName.isEmpty() -> {
                errorMessage = "Please enter your full name"
                false
            }
            email.isEmpty() -> {
                errorMessage = "Please enter your email"
                false
            }
            !email.contains("@") -> {
                errorMessage = "Please enter a valid email"
                false
            }
            password.isEmpty() -> {
                errorMessage = "Please enter a password"
                false
            }
            password.length < 6 -> {
                errorMessage = "Password must be at least 6 characters"
                false
            }
            password != confirmPassword -> {
                errorMessage = "Passwords do not match"
                false
            }
            else -> {
                errorMessage = ""
                true
            }
        }
    }
    
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
            
            // Spiritual Symbol
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(75.dp))
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🙏",
                    fontSize = 36.sp,
                    color = Primary
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Title
            Text(
                text = "Join Our Community",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Begin your spiritual journey with us",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Signup Form
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
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // Full Name Field
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "Full Name")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
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
                    
                    // Phone Number Field
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number (Optional)") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = "Phone")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
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
                    
                    // Confirm Password Field
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                        },
                        trailingIcon = {
                            IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                Icon(
                                    if (isConfirmPasswordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None
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
                    
                    // Signup Button
                    Button(
                        onClick = {
                            if (validateForm()) {
                                isLoading = true
                                
                                // Firebase Authentication
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Save user profile to Firestore
                                            val user = auth.currentUser
                                            val userProfile = hashMapOf(
                                                "fullName" to fullName,
                                                "email" to email,
                                                "phoneNumber" to phoneNumber,
                                                "createdAt" to System.currentTimeMillis(),
                                                "isActive" to true
                                            )
                                            
                                            user?.let { firebaseUser ->
                                                firestore.collection("users")
                                                    .document(firebaseUser.uid)
                                                    .set(userProfile)
                                                    .addOnSuccessListener {
                                                        isLoading = false
                                                        onSignupSuccess()
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        isLoading = false
                                                        errorMessage = "Profile creation failed: ${exception.message}"
                                                    }
                                            }
                                        } else {
                                            isLoading = false
                                            errorMessage = task.exception?.message ?: "Signup failed"
                                        }
                                    }
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
                                text = "Create Account",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Login Link
                    TextButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Already have an account? Sign In",
                            color = Primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
} 