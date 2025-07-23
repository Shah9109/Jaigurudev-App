package com.example.jaigurudevapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.navigation.Routes
import com.example.jaigurudevapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// Responsive Design Utils
@Composable
fun getScreenSizeInfo(): ScreenSizeInfo {
    val configuration = LocalConfiguration.current
    return ScreenSizeInfo(
        heightPx = configuration.screenHeightDp,
        widthPx = configuration.screenWidthDp,
        heightDp = configuration.screenHeightDp.dp,
        widthDp = configuration.screenWidthDp.dp
    )
}

data class ScreenSizeInfo(
    val heightPx: Int,
    val widthPx: Int,
    val heightDp: Dp,
    val widthDp: Dp
) {
    val isTablet: Boolean get() = widthPx >= 600
    val isLandscape: Boolean get() = widthPx > heightPx
}

// Enhanced Data classes with more comprehensive data
data class Notice(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val priority: String,
    val icon: ImageVector = Icons.Default.Notifications
)

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val color: Color,
    val description: String = ""
)

data class SpiritualBook(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val category: String,
    val rating: Float,
    val chapters: Int,
    val readTime: String,
    val coverColor: Color,
    val isNew: Boolean = false,
    val isFavorite: Boolean = false
)

data class UpcomingEvent(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val category: String,
    val isOnline: Boolean,
    val color: Color,
    val attendees: Int,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val screenInfo = getScreenSizeInfo()
    
    // Comprehensive dummy data
    val notices = listOf(
        Notice("1", "Daily Morning Satsang", "Join us for morning meditation and prayers at 6:00 AM every day", "Today", "High", Icons.Default.Event),
        Notice("2", "New Spiritual Book Release", "\"Divine Wisdom Vol 3\" by Gurudev is now available in our library", "2 days ago", "Medium", Icons.Default.MenuBook),
        Notice("3", "Community Seva Program", "Monthly community service at local temple this weekend", "3 days ago", "Low", Icons.Default.Group),
        Notice("4", "Online Bhajan Session", "Live devotional singing session every Friday evening", "1 week ago", "Medium", Icons.Default.MusicNote),
        Notice("5", "Yoga Workshop", "Special yoga and pranayama workshop for beginners", "5 days ago", "High", Icons.Default.SelfImprovement)
    )
    
    val quickActions = listOf(
        QuickAction("Books", Icons.Default.MenuBook, Routes.BOOKS, Primary, "Sacred texts & spiritual literature"),
        QuickAction("Videos", Icons.Default.VideoLibrary, Routes.VIDEOS, Secondary, "Satsang & spiritual videos"),
        QuickAction("Music", Icons.Default.LibraryMusic, Routes.MUSIC, AccentGold, "Devotional songs & mantras")
    )
    
    val spiritualBooks = listOf(
        SpiritualBook("1", "Bhagavad Gita", "Vyasa Maharshi", "The eternal wisdom of Krishna to Arjuna", "Scripture", 4.9f, 18, "30 days", Primary, isNew = true),
        SpiritualBook("2", "Meditation Mastery", "Gurudev", "Complete guide to meditation techniques", "Practice", 4.8f, 12, "15 days", Secondary, isFavorite = true),
        SpiritualBook("3", "Sacred Mantras", "Various Sages", "Collection of powerful healing mantras", "Mantras", 4.7f, 8, "10 days", AccentGold),
        SpiritualBook("4", "Daily Prayers", "Traditional", "Morning and evening prayer collections", "Prayers", 4.6f, 6, "7 days", MeditationBlue),
        SpiritualBook("5", "Spiritual Stories", "Sant Kabir", "Inspiring tales of saints and sages", "Stories", 4.8f, 20, "25 days", AccentRed, isNew = true),
        SpiritualBook("6", "Yoga Sutras", "Patanjali", "Ancient wisdom on yoga philosophy", "Philosophy", 4.9f, 4, "20 days", Primary, isFavorite = true)
    )
    
    val upcomingEvents = listOf(
        UpcomingEvent("1", "Ram Mandir Satsang", "Tomorrow", "6:00 PM", "Community Center", "Satsang", false, Primary, 150, "Community gathering for prayers and bhajans"),
        UpcomingEvent("2", "Online Hanuman Chalisa", "Today", "7:00 PM", "Zoom Meeting", "Mantra", true, Secondary, 300, "108 times chanting session"),
        UpcomingEvent("3", "Yoga & Meditation Retreat", "Next Week", "6:00 AM", "Rishikesh Ashram", "Retreat", false, MeditationBlue, 50, "3-day intensive spiritual retreat"),
        UpcomingEvent("4", "Gita Study Circle", "This Sunday", "10:00 AM", "Local Temple", "Study", false, AccentGold, 75, "Weekly scripture study group"),
        UpcomingEvent("5", "Full Moon Meditation", "15th Feb", "8:00 PM", "Online & Chandigarh", "Meditation", true, AccentRed, 200, "Monthly group meditation")
    )
    
    // Responsive padding and spacing
    val horizontalPadding = if (screenInfo.isTablet) 32.dp else 16.dp
    val verticalSpacing = if (screenInfo.isTablet) 24.dp else 16.dp
    val cardSpacing = if (screenInfo.isTablet) 16.dp else 12.dp
    
    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition()
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.background
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f * animatedFloat, 1000f * animatedFloat)
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        
        // Welcome Header - Responsive
        item {
            ResponsiveWelcomeHeader(screenInfo)
        }
        
        // Quick Actions - Responsive Grid
        item {
            ResponsiveQuickActions(quickActions, screenInfo, navController)
        }
        
        // Upcoming Events
        item {
            ResponsiveEventsSection(upcomingEvents, screenInfo, navController)
        }
        
        // Important Notices
        item {
            ResponsiveNoticesSection(notices, screenInfo, cardSpacing)
        }
        
        // Spiritual Books - Responsive Grid
        item {
            ResponsiveBooksSection(spiritualBooks, screenInfo, navController)
        }
        
        // Daily Quote
        item {
            ResponsiveDailyQuote(screenInfo)
        }
    }
}

@Composable
fun ResponsiveWelcomeHeader(screenInfo: ScreenSizeInfo) {
    val cardPadding = if (screenInfo.isTablet) 32.dp else 20.dp
    val titleSize = if (screenInfo.isTablet) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.headlineSmall
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🙏 Jai Gurudev 🙏",
                style = titleSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Welcome to your spiritual journey",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ResponsiveQuickActions(
    actions: List<QuickAction>,
    screenInfo: ScreenSizeInfo,
    navController: NavHostController
) {
    Column {
        Text(
            text = "Quick Actions",
            style = if (screenInfo.isTablet) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        if (screenInfo.isTablet) {
            // Grid layout for tablets
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(200.dp),
                userScrollEnabled = false
            ) {
                items(actions) { action ->
                    ResponsiveActionCard(action, screenInfo, navController)
                }
            }
        } else {
            // Horizontal scroll for phones
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    ResponsiveActionCard(action, screenInfo, navController)
                }
            }
        }
    }
}

@Composable
fun ResponsiveActionCard(
    action: QuickAction,
    screenInfo: ScreenSizeInfo,
    navController: NavHostController
) {
    val cardSize = if (screenInfo.isTablet) 160.dp else 120.dp
    val iconSize = if (screenInfo.isTablet) 40.dp else 32.dp
    
    Card(
        modifier = Modifier
            .size(cardSize)
            .clickable { navController.navigate(action.route) }
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = action.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = action.color,
                modifier = Modifier.size(iconSize)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = action.title,
                style = if (screenInfo.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = action.color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ResponsiveEventsSection(
    events: List<UpcomingEvent>,
    screenInfo: ScreenSizeInfo,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Events",
                style = if (screenInfo.isTablet) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            TextButton(onClick = { navController.navigate(Routes.EVENTS) }) {
                Text("View All", color = MaterialTheme.colorScheme.primary)
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "View All",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(if (screenInfo.isTablet) 16.dp else 12.dp)
        ) {
            items(events.take(if (screenInfo.isTablet) 4 else 3)) { event ->
                ResponsiveEventCard(event, screenInfo, navController)
            }
        }
    }
}

@Composable
fun ResponsiveEventCard(
    event: UpcomingEvent,
    screenInfo: ScreenSizeInfo,
    navController: NavHostController
) {
    val cardWidth = if (screenInfo.isTablet) 320.dp else 260.dp
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .clickable { navController.navigate(Routes.EVENTS) }
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2
                    )
                    Text(
                        text = event.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = event.color,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(event.color.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when(event.category) {
                            "Satsang" -> "🕉️"
                            "Mantra" -> "📿"
                            "Retreat" -> "🏔️"
                            "Study" -> "📚"
                            "Meditation" -> "🧘"
                            else -> "🎯"
                        },
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = "Time",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${event.date} • ${event.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (event.isOnline) Icons.Default.Videocam else Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                if (event.isOnline) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ) {
                        Text(
                            text = "ONLINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { navController.navigate(Routes.EVENTS) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = event.color),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("View Details")
            }
        }
    }
}

@Composable
fun ResponsiveNoticesSection(
    notices: List<Notice>,
    screenInfo: ScreenSizeInfo,
    cardSpacing: Dp
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Important Notices",
                style = if (screenInfo.isTablet) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(cardSpacing)
        ) {
            items(notices) { notice ->
                ResponsiveNoticeCard(notice, screenInfo)
            }
        }
    }
}

@Composable
fun ResponsiveNoticeCard(notice: Notice, screenInfo: ScreenSizeInfo) {
    val cardWidth = if (screenInfo.isTablet) 320.dp else 280.dp
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                
                val priorityColor = when (notice.priority) {
                    "High" -> MaterialTheme.colorScheme.error
                    "Medium" -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.secondary
                }
                
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = priorityColor.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = notice.priority,
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = notice.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = notice.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ResponsiveBooksSection(
    books: List<SpiritualBook>,
    screenInfo: ScreenSizeInfo,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Spiritual Books",
                style = if (screenInfo.isTablet) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            TextButton(onClick = { navController.navigate(Routes.BOOKS) }) {
                Text("View All", color = MaterialTheme.colorScheme.primary)
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "View All",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (screenInfo.isTablet) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (screenInfo.isLandscape) 4 else 3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(400.dp),
                userScrollEnabled = false
            ) {
                items(books.take(6)) { book ->
                    ResponsiveBookCard(book, screenInfo, navController)
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books) { book ->
                    ResponsiveBookCard(book, screenInfo, navController)
                }
            }
        }
    }
}

@Composable
fun ResponsiveBookCard(
    book: SpiritualBook,
    screenInfo: ScreenSizeInfo,
    navController: NavHostController
) {
    val cardWidth = if (screenInfo.isTablet) 180.dp else 140.dp
    val cardHeight = if (screenInfo.isTablet) 240.dp else 200.dp
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable { navController.navigate("${Routes.PDF_VIEWER}/${book.id}") }
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(book.coverColor.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = "Book",
                    tint = book.coverColor,
                    modifier = Modifier.size(if (screenInfo.isTablet) 40.dp else 32.dp)
                )
                
                if (book.isNew) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = "NEW",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = book.title,
                    style = if (screenInfo.isTablet) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (screenInfo.isTablet) {
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = book.rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResponsiveDailyQuote(screenInfo: ScreenSizeInfo) {
    val cardPadding = if (screenInfo.isTablet) 32.dp else 20.dp
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.FormatQuote,
                contentDescription = "Quote",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(if (screenInfo.isTablet) 40.dp else 32.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "\"The mind is everything. What you think you become.\"",
                style = if (screenInfo.isTablet) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "- Buddha",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 