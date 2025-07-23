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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.data.SpiritualRepository
import com.example.jaigurudevapp.data.SpiritualBook
import com.example.jaigurudevapp.navigation.Routes
import com.example.jaigurudevapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(navController: NavHostController) {
    val repository = remember { SpiritualRepository.getInstance() }
    val allBooks = remember { repository.getAllBooks() }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    
    // Book categories
    val categories = listOf(
        "All", "Scriptures", "Philosophy", "Practice", "Mantras", "Stories"
    )
    
    // Filter books based on search and category
    val filteredBooks = remember(searchQuery, selectedCategory) {
        val searchFiltered = if (searchQuery.isBlank()) {
            allBooks
        } else {
            repository.searchBooks(searchQuery)
        }
        
        if (selectedCategory == "All") {
            searchFiltered
        } else {
            searchFiltered.filter { it.category == selectedCategory }
        }
    }
    
    // Category specific books
    val scriptureBooks = remember { repository.getBooksByCategory("Scriptures") }
    val philosophyBooks = remember { repository.getBooksByCategory("Philosophy") }
    val practiceBooks = remember { repository.getBooksByCategory("Practice") }
    val popularBooks = remember { allBooks.sortedByDescending { it.rating } }
    val recentBooks = remember { allBooks.sortedByDescending { it.publishYear } }
    
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
                        text = "📚 Spiritual Library",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Reading status indicator
                    Surface(
                        color = Primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${allBooks.size} Books",
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
                    placeholder = { Text("Search books, authors, topics...") },
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
            // Sacred Scriptures Section (Prominent)
            item {
                SectionHeader(
                    title = "📜 Sacred Scriptures",
                    subtitle = "Ancient wisdom and divine teachings"
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(scriptureBooks) { book ->
                        ScriptureBookCard(
                            book = book,
                            onClick = { navController.navigate("${Routes.PDF_VIEWER}/${book.id}") }
                        )
                    }
                }
            }
            
            // Philosophy & Wisdom Section
            item {
                SectionHeader(
                    title = "🤔 Philosophy & Wisdom",
                    subtitle = "Deep insights and philosophical teachings"
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(philosophyBooks) { book ->
                        PhilosophyBookCard(
                            book = book,
                            onClick = { navController.navigate("${Routes.PDF_VIEWER}/${book.id}") }
                        )
                    }
                }
            }
            
            // Spiritual Practice Section
            item {
                SectionHeader(
                    title = "🧘 Spiritual Practice",
                    subtitle = "Practical guides for spiritual growth"
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(practiceBooks) { book ->
                        PracticeBookCard(
                            book = book,
                            onClick = { navController.navigate("${Routes.PDF_VIEWER}/${book.id}") }
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
            
            // Top Rated Books (if All category selected)
            if (selectedCategory == "All") {
                item {
                    SectionHeader(
                        title = "⭐ Highly Rated",
                        subtitle = "Most loved spiritual books"
                    )
                }
                
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(popularBooks.take(5)) { book ->
                            PopularBookCard(
                                book = book,
                                onClick = { navController.navigate("${Routes.PDF_VIEWER}/${book.id}") }
                            )
                        }
                    }
                }
            }
            
            // All Books or Filtered Books
            item {
                val sectionTitle = if (selectedCategory == "All") "All Books" else "$selectedCategory Books"
                SectionHeader(
                    title = sectionTitle,
                    subtitle = "${filteredBooks.size} books available"
                )
            }
            
            items(filteredBooks) { book ->
                BookDetailCard(
                    book = book,
                    onClick = { navController.navigate("${Routes.PDF_VIEWER}/${book.id}") }
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
private fun ScriptureBookCard(
    book: SpiritualBook,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF9800).copy(alpha = 0.8f),
                                Color(0xFFFF9800)
                            )
                        )
                    )
            ) {
                Text(
                    text = "📜",
                    fontSize = 40.sp,
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
                        text = book.language,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                // Pages indicator
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${book.pages} pages",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = book.author,
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
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = book.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD700)
                        )
                    }
                    
                    Text(
                        text = book.readTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun PhilosophyBookCard(
    book: SpiritualBook,
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
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF9C27B0).copy(alpha = 0.8f),
                                Color(0xFF9C27B0)
                            )
                        )
                    )
            ) {
                Text(
                    text = "🤔",
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Publication year
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = book.publishYear.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = book.publisher,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${book.chapters.size} chapters",
                        style = MaterialTheme.typography.bodySmall,
                        color = Primary
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = book.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeBookCard(
    book: SpiritualBook,
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
                    .height(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.8f),
                                Color(0xFF4CAF50)
                            )
                        )
                    )
            ) {
                Text(
                    text = "🧘",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Difficulty level
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (book.title.contains("Beginners")) "Beginner" else "Intermediate",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = book.readTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PopularBookCard(
    book: SpiritualBook,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
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
                                Color(0xFFFFD700).copy(alpha = 0.8f),
                                Color(0xFFFFD700)
                            )
                        )
                    )
            ) {
                Text(
                    text = "⭐",
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Rating badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp),
                    color = Color.Black.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "★ ${book.rating}",
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
                    text = book.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFD700)
                )
            }
        }
    }
}

@Composable
private fun BookDetailCard(
    book: SpiritualBook,
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
            verticalAlignment = Alignment.Top
        ) {
            // Book cover placeholder
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Primary.copy(alpha = 0.3f), Primary.copy(alpha = 0.6f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when (book.category) {
                    "Scriptures" -> "📜"
                    "Philosophy" -> "🤔"
                    "Practice" -> "🧘"
                    "Mantras" -> "🕉️"
                    "Stories" -> "📖"
                    else -> "📚"
                }
                Text(
                    text = emoji,
                    fontSize = 24.sp
                )
                
                // Download indicator
                if (book.isDownloaded) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        color = Color.Green,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(
                            Icons.Default.CloudDone,
                            contentDescription = "Downloaded",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(2.dp)
                                .size(12.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${book.rating} • ${book.pages} pages",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        Text(
                            text = "Read time: ${book.readTime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Primary
                        )
                    }
                    
                    // Action buttons
                    Row {
                        // Favorite button
                        IconButton(
                            onClick = { /* Toggle favorite */ },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = if (book.isFavorite) Color.Red.copy(alpha = 0.1f) else Color.Transparent
                            )
                        ) {
                            Icon(
                                if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (book.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        // Read button
                        IconButton(
                            onClick = onClick,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Primary.copy(alpha = 0.1f)
                            )
                        ) {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = "Read",
                                tint = Primary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Tags
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(book.tags.take(4)) { tag ->
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