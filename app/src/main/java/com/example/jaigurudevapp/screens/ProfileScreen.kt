package com.example.jaigurudevapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.ui.theme.*

data class ProfileSetting(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Sample user data (in real app, get from Firebase Auth/Firestore)
    val userProfile = UserProfile(
        name = "Devotee Seeker",
        email = "seeker@spiritual.com",
        joinDate = "Joined January 2024",
        level = "Spiritual Practitioner",
        completedSessions = 45,
        streakDays = 12,
        favoriteCategory = "Meditation"
    )
    
    val profileSettings = listOf(
        ProfileSetting("Edit Profile", "Update your information", Icons.Default.Edit) { },
        ProfileSetting("Notifications", "Manage app notifications", Icons.Default.Notifications) { },
        ProfileSetting("Downloads", "Manage offline content", Icons.Default.Download) { },
        ProfileSetting("Privacy", "Privacy and security settings", Icons.Default.Security) { },
        ProfileSetting("Help & Support", "Get help and contact us", Icons.Default.Help) { },
        ProfileSetting("About", "App information and credits", Icons.Default.Info) { },
        ProfileSetting("Logout", "Sign out of your account", Icons.Default.Logout) { showLogoutDialog = true }
    )
    
    val gradient = Brush.linearGradient(
        colors = listOf(
            Primary.copy(alpha = 0.1f),
            Secondary.copy(alpha = 0.05f),
            Background
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // Profile Header
        item {
            ProfileHeader(userProfile = userProfile)
        }
        
        // Stats Cards
        item {
            StatsSection(userProfile = userProfile)
        }
        
        // Settings
        item {
            SettingsSection(settings = profileSettings)
        }
    }
    
    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout from your account?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileHeader(userProfile: UserProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Primary, Secondary, AccentGold)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userProfile.name.split(" ").map { it.first() }.joinToString(""),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = userProfile.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            
            Text(
                text = userProfile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.1f))
            ) {
                Text(
                    text = userProfile.level,
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = userProfile.joinDate,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun StatsSection(userProfile: UserProfile) {
    Column {
        Text(
            text = "Your Spiritual Journey",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Sessions",
                value = userProfile.completedSessions.toString(),
                icon = "🧘",
                color = Primary,
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Streak",
                value = "${userProfile.streakDays} days",
                icon = "🔥",
                color = AccentGold,
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Favorite",
                value = userProfile.favoriteCategory,
                icon = "❤️",
                color = Secondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SettingsSection(settings: List<ProfileSetting>) {
    Column {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                settings.forEachIndexed { index, setting ->
                    SettingItem(
                        setting = setting,
                        showDivider = index < settings.size - 1
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    setting: ProfileSetting,
    showDivider: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { setting.onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = setting.icon,
                contentDescription = setting.title,
                tint = if (setting.title == "Logout") AccentRed else Primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = setting.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (setting.title == "Logout") AccentRed else OnSurface,
                    fontWeight = FontWeight.Medium
                )
                
                setting.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = OnSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
        
        if (showDivider) {
            Divider(
                modifier = Modifier.padding(horizontal = 56.dp),
                color = OnSurface.copy(alpha = 0.1f)
            )
        }
    }
}

data class UserProfile(
    val name: String,
    val email: String,
    val joinDate: String,
    val level: String,
    val completedSessions: Int,
    val streakDays: Int,
    val favoriteCategory: String
) 