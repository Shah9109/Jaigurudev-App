package com.example.jaigurudevapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.data.SpiritualRepository
import com.example.jaigurudevapp.data.SpiritualVideo
import com.example.jaigurudevapp.navigation.Routes
import com.example.jaigurudevapp.ui.theme.*

data class Video(
    val id: String,
    val title: String,
    val creator: String,
    val duration: String,
    val views: String,
    val uploadDate: String,
    val category: String,
    val thumbnailColor: Color,
    val isLive: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen(navController: NavHostController) {
    val repository = remember { SpiritualRepository.getInstance() }
    val allVideos = remember { repository.getAllVideos() }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    
    // Categories with Satsang as prominent category
    val categories = listOf(
        "All", "Satsang", "Meditation", "Devotional", "Stories", "Live"
    )
    
    // Filter videos based on search and category
    val filteredVideos = remember(searchQuery, selectedCategory) {
        val searchFiltered = if (searchQuery.isBlank()) {
            allVideos
        } else {
            repository.searchVideos(searchQuery)
        }
        
        if (selectedCategory == "All") {
            searchFiltered
        } else {
            searchFiltered.filter { it.category == selectedCategory }
        }
    }
    
    // Satsang specific videos
    val satsangVideos = remember { repository.getVideosByCategory("Satsang") }
    val liveVideos = remember { allVideos.filter { it.isLive } }
    val trendingVideos = remember { allVideos.sortedByDescending { it.likes } }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        // Header with Search
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🎥 Spiritual Videos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search videos...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Live Videos Section (if any)
            if (liveVideos.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "🔴 Live Now",
                        subtitle = "Join live spiritual sessions"
                    )
                }
                
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(liveVideos) { video ->
                            LiveVideoCard(
                                video = video,
                                onClick = { navController.navigate("${Routes.VIDEO_PLAYER}/${video.id}") }
                            )
                        }
                    }
                }
            }
            
            // Satsang Section (Prominent)
            item {
                SectionHeader(
                    title = "🕉️ Satsang Collection",
                    subtitle = "Divine wisdom and spiritual teachings"
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(satsangVideos) { video ->
                        SatsangVideoCard(
                            video = video,
                            onClick = { navController.navigate("${Routes.VIDEO_PLAYER}/${video.id}") }
                        )
                    }
                }
            }
            
            // Category Filter
            item {
                CategoryFilterRow(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            
            // Trending Videos
            if (selectedCategory == "All") {
                item {
                    SectionHeader(
                        title = "🔥 Trending",
                        subtitle = "Most watched spiritual content"
                    )
                }
                
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(trendingVideos.take(5)) { video ->
                            TrendingVideoCard(
                                video = video,
                                onClick = { navController.navigate("${Routes.VIDEO_PLAYER}/${video.id}") }
                            )
                        }
                    }
                }
            }
            
            // All Videos or Filtered Videos
            item {
                val sectionTitle = if (selectedCategory == "All") "All Videos" else "$selectedCategory Videos"
                SectionHeader(
                    title = sectionTitle,
                    subtitle = "${filteredVideos.size} videos available"
                )
            }
            
            items(filteredVideos) { video ->
                VideoCard(
                    video = video,
                    onClick = { navController.navigate("${Routes.VIDEO_PLAYER}/${video.id}") }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                selected = selectedCategory == category,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun LiveVideoCard(
    video: SpiritualVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Live indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Red.copy(alpha = 0.8f), Color.Red)
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            Color.Red,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Circle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(8.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LIVE",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = "📺",
                    fontSize = 48.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = video.creator,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${video.views} watching",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun SatsangVideoCard(
    video: SpiritualVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Primary.copy(alpha = 0.8f), Primary)
                        )
                    )
            ) {
                Text(
                    text = "🕉️",
                    fontSize = 40.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Quality badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = video.quality,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = video.creator,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = video.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = video.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = Primary
                    )
                    Text(
                        text = video.views,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingVideoCard(
    video: SpiritualVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF6B35).copy(alpha = 0.8f),
                                Color(0xFFFF6B35)
                            )
                        )
                    )
            ) {
                Text(
                    text = "🔥",
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Trending badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    color = Color.Red,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "TRENDING",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${video.likes}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary
                        )
                    }
                    
                    Text(
                        text = video.views,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoCard(
    video: SpiritualVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // Video thumbnail placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Primary.copy(alpha = 0.3f), Primary.copy(alpha = 0.6f))
                        )
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
                Text(
                    text = emoji,
                    fontSize = 24.sp
                )
                
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
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = video.creator,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${video.views} • ${video.uploadDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = video.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Tags
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(video.tags.take(3)) { tag ->
                        Surface(
                            color = Primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
} 