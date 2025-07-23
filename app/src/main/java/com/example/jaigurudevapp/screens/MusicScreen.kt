package com.example.jaigurudevapp.screens

import androidx.compose.animation.core.*
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
import com.example.jaigurudevapp.data.SpiritualMusic
import com.example.jaigurudevapp.navigation.Routes
import com.example.jaigurudevapp.ui.theme.*

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val category: String,
    val isDownloaded: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(navController: NavHostController) {
    val repository = remember { SpiritualRepository.getInstance() }
    val allMusic = remember { repository.getAllMusic() }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    
    // Music categories
    val categories = listOf(
        "All", "Mantras", "Devotional", "Meditation", "Prayers"
    )
    
    // Filter music based on search and category
    val filteredMusic = remember(searchQuery, selectedCategory) {
        val searchFiltered = if (searchQuery.isBlank()) {
            allMusic
        } else {
            repository.searchMusic(searchQuery)
        }
        
        if (selectedCategory == "All") {
            searchFiltered
        } else {
            searchFiltered.filter { it.category == selectedCategory }
        }
    }
    
    // Category specific music
    val mantrasMusic = remember { repository.getMusicByCategory("Mantras") }
    val devotionalMusic = remember { repository.getMusicByCategory("Devotional") }
    val popularMusic = remember { allMusic.sortedByDescending { it.playCount } }
    val recentMusic = remember { allMusic.sortedByDescending { it.releaseYear } }
    
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎵 Spiritual Music",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Current playing indicator
                    Surface(
                        color = Primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Now Playing",
                                style = MaterialTheme.typography.labelSmall,
                                color = Primary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search music, mantras, bhajans...") },
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
            // Sacred Mantras Section (Prominent)
            item {
                SectionHeader(
                    title = "🕉️ Sacred Mantras",
                    subtitle = "Powerful chants for meditation and peace"
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(mantrasMusic) { music ->
                        MantraMusicCard(
                            music = music,
                            onClick = { navController.navigate("${Routes.MUSIC_PLAYER}/${music.id}") }
                        )
                    }
                }
            }
            
            // Devotional Music Section
            item {
                SectionHeader(
                    title = "🙏 Devotional Collection",
                    subtitle = "Bhajans and spiritual songs"
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(devotionalMusic) { music ->
                        DevotionalMusicCard(
                            music = music,
                            onClick = { navController.navigate("${Routes.MUSIC_PLAYER}/${music.id}") }
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
            
            // Popular Music (if All category selected)
            if (selectedCategory == "All") {
                item {
                    SectionHeader(
                        title = "🔥 Most Popular",
                        subtitle = "Trending spiritual music"
                    )
                }
                
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(popularMusic.take(5)) { music ->
                            PopularMusicCard(
                                music = music,
                                onClick = { navController.navigate("${Routes.MUSIC_PLAYER}/${music.id}") }
                            )
                        }
                    }
                }
            }
            
            // All Music or Filtered Music
            item {
                val sectionTitle = if (selectedCategory == "All") "All Music" else "$selectedCategory Music"
                SectionHeader(
                    title = sectionTitle,
                    subtitle = "${filteredMusic.size} tracks available"
                )
            }
            
            items(filteredMusic) { music ->
                MusicTrackCard(
                    music = music,
                    onClick = { navController.navigate("${Routes.MUSIC_PLAYER}/${music.id}") }
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
private fun MantraMusicCard(
    music: SpiritualMusic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
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
                            colors = listOf(
                                Color(0xFFFFB74D).copy(alpha = 0.8f),
                                Color(0xFFFFB74D)
                            )
                        )
                    )
            ) {
                Text(
                    text = "🕉️",
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Language badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = music.language,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = music.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${music.playCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary
                        )
                    }
                    
                    Text(
                        text = music.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DevotionalMusicCard(
    music: SpiritualMusic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
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
                    .height(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE91E63).copy(alpha = 0.8f),
                                Color(0xFFE91E63)
                            )
                        )
                    )
            ) {
                Text(
                    text = "🙏",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Rating
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = music.rating.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = music.album,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = music.duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PopularMusicCard(
    music: SpiritualMusic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
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
                    .height(90.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF5722).copy(alpha = 0.8f),
                                Color(0xFFFF5722)
                            )
                        )
                    )
            ) {
                Text(
                    text = "🔥",
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Popular badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp),
                    color = Color.Red,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "HOT",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = "${music.playCount} plays",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun MusicTrackCard(
    music: SpiritualMusic,
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
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Music album art placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)  
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Primary.copy(alpha = 0.3f), Primary.copy(alpha = 0.6f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when (music.category) {
                    "Mantras" -> "🕉️"
                    "Devotional" -> "🙏"
                    "Meditation" -> "🧘"
                    "Prayers" -> "📿"
                    else -> "🎵"
                }
                Text(
                    text = emoji,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = music.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = music.album,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = music.duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = music.rating.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                // Tags
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(music.tags.take(2)) { tag ->
                        Surface(
                            color = Primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Play button
            IconButton(
                onClick = onClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Primary.copy(alpha = 0.1f)
                )
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Primary
                )
            }
        }
    }
} 