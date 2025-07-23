package com.example.jaigurudevapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.data.SpiritualRepository
import com.example.jaigurudevapp.data.SpiritualMusic
import com.example.jaigurudevapp.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    songId: String,
    navController: NavHostController
) {
    val repository = remember { SpiritualRepository.getInstance() }
    val allMusic = remember { repository.getAllMusic() }
    
    // Find the current song by ID
    val currentSong = remember(songId) {
        allMusic.find { it.id == songId } ?: allMusic.firstOrNull() ?: SpiritualMusic(
            id = "default",
            title = "Om Namah Shivaya",
            artist = "Divine Chants",
            album = "Sacred Mantras",
            duration = "5:30",
            category = "Mantras",
            audioUrl = "",
            thumbnailUrl = "",
            language = "Sanskrit"
        )
    }
    
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Convert duration to seconds for slider
    val durationInSeconds = remember(currentSong.duration) {
        val parts = currentSong.duration.split(":")
        if (parts.size == 2) {
            (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
        } else 300 // Default 5 minutes
    }.toFloat()
    
    // Simulate playback progress
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (currentPosition < durationInSeconds && isPlaying) {
                delay(1000)
                currentPosition += 1f
            }
            if (currentPosition >= durationInSeconds) {
                isPlaying = false
                currentPosition = 0f
            }
        }
    }
    
    // Background animation
    val infiniteTransition = rememberInfiniteTransition()
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val backgroundGradient = Brush.radialGradient(
        colors = listOf(
            Primary.copy(alpha = 0.3f),
            Secondary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.background
        ),
        center = Offset(500f + animatedFloat * 200f, 800f + animatedFloat * 300f),
        radius = 1000f + animatedFloat * 500f
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Text(
                    text = "Now Playing",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                
                IconButton(
                    onClick = { /* Open queue */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.QueueMusic,
                        contentDescription = "Queue",
                        tint = Primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Album Art
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .shadow(16.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.8f),
                                Secondary.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Rotating animation for playing state
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = if (isPlaying) 360f else 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(10000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
                
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    when (currentSong.category) {
                        "Mantras" -> Text("🕉️", fontSize = 60.sp)
                        "Devotional" -> Text("🙏", fontSize = 60.sp)
                        "Meditation" -> Text("🧘", fontSize = 60.sp)
                        "Prayers" -> Text("📿", fontSize = 60.sp)
                        else -> Icon(
                            imageVector = Icons.Default.LibraryMusic,
                            contentDescription = "Music",
                            tint = Primary,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
                
                // Language badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = currentSong.language,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Song Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentSong.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = currentSong.artist,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = currentSong.album,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Rating and play count
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentSong.rating.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        
                        Text(
                            text = "${currentSong.playCount} plays",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Progress Bar
                    Column {
                        Slider(
                            value = currentPosition,
                            onValueChange = { currentPosition = it },
                            valueRange = 0f..durationInSeconds,
                            colors = SliderDefaults.colors(
                                thumbColor = Primary,
                                activeTrackColor = Primary,
                                inactiveTrackColor = Primary.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(currentPosition),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = currentSong.duration,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Control Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        IconButton(
                            onClick = { /* Previous song */ },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Previous",
                                tint = Primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        
                        // Play/Pause Button
                        IconButton(
                            onClick = { 
                                isPlaying = !isPlaying
                                isLoading = false
                                // In real app: toggle ExoPlayer playback
                            },
                            modifier = Modifier
                                .size(72.dp)
                                .background(Primary, CircleShape)
                                .shadow(8.dp, CircleShape)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        
                        // Next Button
                        IconButton(
                            onClick = { /* Next song */ },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Next",
                                tint = Primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Additional Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = { /* Shuffle */ }) {
                            Icon(
                                imageVector = Icons.Default.Shuffle,
                                contentDescription = "Shuffle",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        
                        IconButton(onClick = { /* Favorite */ }) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        
                        IconButton(onClick = { /* Repeat */ }) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Repeat",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        
                        IconButton(onClick = { /* Share */ }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    return String.format("%d:%02d", minutes, remainingSeconds)
} 