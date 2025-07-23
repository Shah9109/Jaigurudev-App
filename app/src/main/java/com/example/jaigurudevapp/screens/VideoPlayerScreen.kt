package com.example.jaigurudevapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.data.SpiritualRepository
import com.example.jaigurudevapp.data.SpiritualVideo
import com.example.jaigurudevapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    videoId: String,
    navController: NavHostController
) {
    val repository = remember { SpiritualRepository.getInstance() }
    val allVideos = remember { repository.getAllVideos() }
    
    // Find the current video by ID
    val videoData = remember(videoId) {
        allVideos.find { it.id == videoId } ?: allVideos.firstOrNull() ?: SpiritualVideo(
            id = "default",
            title = "Spiritual Video",
            description = "A beautiful spiritual video",
            creator = "Spiritual Teacher",
            duration = "30:00",
            views = "1K",
            uploadDate = "Today",
            category = "Meditation",
            thumbnailUrl = "",
            videoUrl = "",
            language = "Hindi"
        )
    }
    
    // Get related videos from the same category
    val relatedVideos = remember(videoData.category) {
        allVideos.filter { it.id != videoData.id && it.category == videoData.category }.take(5)
    }
    
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(1800f) } // 30 minutes
    var isFullscreen by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        
        // Video Player Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            
            // Video placeholder (In real app, integrate ExoPlayer here)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Primary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (videoData.category) {
                        "Satsang" -> Text("🕉️", style = MaterialTheme.typography.displayLarge, color = Color.White)
                        "Meditation" -> Text("🧘", style = MaterialTheme.typography.displayLarge, color = Color.White)
                        "Devotional" -> Text("🙏", style = MaterialTheme.typography.displayLarge, color = Color.White)
                        "Stories" -> Text("📚", style = MaterialTheme.typography.displayLarge, color = Color.White)
                        else -> Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Video",
                            tint = Color.White,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "ExoPlayer Video Here",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    
                    if (videoData.isLive) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color.Red,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "🔴 LIVE",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            
            // Play/Pause Button
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Black.copy(alpha = 0.6f), androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            // Top Controls
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Row {
                    // Quality indicator
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = videoData.quality,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(onClick = { /* Cast */ }) {
                        Icon(
                            imageVector = Icons.Default.Cast,
                            contentDescription = "Cast",
                            tint = Color.White
                        )
                    }
                    
                    IconButton(onClick = { isFullscreen = !isFullscreen }) {
                        Icon(
                            imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Fullscreen",
                            tint = Color.White
                        )
                    }
                }
            }
            
            // Bottom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
                    .padding(16.dp)
            ) {
                
                // Progress Bar
                Slider(
                    value = currentPosition,
                    onValueChange = { currentPosition = it },
                    valueRange = 0f..duration,
                    colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatVideoTime(currentPosition),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                    
                    Row {
                        IconButton(onClick = { /* Previous 10s */ }) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Replay 10s",
                                tint = Color.White
                            )
                        }
                        
                        IconButton(onClick = { /* Next 10s */ }) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Forward 10s",
                                tint = Color.White
                            )
                        }
                    }
                    
                    Text(
                        text = videoData.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }
        
        // Video Details and Related Videos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Video Info
            item {
                VideoInfoCard(video = videoData)
            }
            
            // Action Buttons
            item {
                VideoActionButtons(
                    onLikeClick = { /* Like video */ },
                    onDislikeClick = { /* Dislike video */ },
                    onShareClick = { /* Share video */ },
                    onDownloadClick = { /* Download video */ },
                    onCommentsClick = { showComments = !showComments }
                )
            }
            
            // Comments Section (if shown)
            if (showComments) {
                item {
                    CommentsSection()
                }
            }
            
            // Related Videos
            if (relatedVideos.isNotEmpty()) {
                item {
                    Text(
                        text = "Related Videos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
                
                items(relatedVideos) { video ->
                    RelatedVideoCard(
                        video = video,
                        onClick = { navController.navigate("video_player/${video.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun VideoInfoCard(video: SpiritualVideo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = video.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = video.creator,
                        style = MaterialTheme.typography.titleMedium,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "${video.views} views • ${video.uploadDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = video.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Category
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Primary.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = video.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoActionButtons(
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onCommentsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(
                icon = Icons.Default.ThumbUp,
                text = "Like",
                onClick = onLikeClick
            )
            
            ActionButton(
                icon = Icons.Default.ThumbDown,
                text = "Dislike",
                onClick = onDislikeClick
            )
            
            ActionButton(
                icon = Icons.Default.Share,
                text = "Share",
                onClick = onShareClick
            )
            
            ActionButton(
                icon = Icons.Default.Download,
                text = "Download",
                onClick = onDownloadClick
            )
            
            ActionButton(
                icon = Icons.Default.Comment,
                text = "Comments",
                onClick = onCommentsClick
            )
        }
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Primary
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun CommentsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Comments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Sample comments
            repeat(3) { index ->
                CommentItem(
                    username = "Devotee ${index + 1}",
                    comment = when (index) {
                        0 -> "Beautiful session! Thank you for sharing this divine wisdom 🙏"
                        1 -> "This meditation helped me find inner peace. Grateful for these teachings."
                        else -> "Amazing content! Looking forward to more spiritual videos."
                    },
                    timestamp = "${index + 1} hours ago"
                )
                
                if (index < 2) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    username: String,
    comment: String,
    timestamp: String
) {
    Row {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Primary.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.first().toString(),
                style = MaterialTheme.typography.labelMedium,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun RelatedVideoCard(
    video: SpiritualVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp, 60.dp)
                    .background(
                        Primary.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when (video.category) {
                    "Satsang" -> "🕉️"
                    "Meditation" -> "🧘"
                    "Devotional" -> "🙏"
                    "Stories" -> "📚"
                    else -> "🎥"
                }
                Text(text = emoji, style = MaterialTheme.typography.titleLarge)
                
                // Duration overlay
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp),
                    color = Color.Black.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = video.creator,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${video.views} views • ${video.uploadDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Primary
                )
            }
        }
    }
}

fun formatVideoTime(seconds: Float): String {
    val hours = (seconds / 3600).toInt()
    val minutes = ((seconds % 3600) / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format("%d:%02d", minutes, remainingSeconds)
    }
} 