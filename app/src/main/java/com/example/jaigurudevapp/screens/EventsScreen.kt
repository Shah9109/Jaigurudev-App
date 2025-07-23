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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class SpiritualEvent(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val startTime: Long,
    val endTime: Long,
    val organizer: String,
    val category: String,
    val isOnline: Boolean,
    val registrationRequired: Boolean,
    val maxAttendees: Int?,
    val currentAttendees: Int,
    val price: String = "Free",
    val imageColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedTimeFilter by remember { mutableStateOf("Upcoming") }
    
    val events = listOf(
        SpiritualEvent(
            id = "1",
            title = "Ram Mandir Satsang",
            description = "Community gathering for prayer and devotional singing",
            location = "Mohali Community Center",
            startTime = System.currentTimeMillis() + 86400000, // Tomorrow
            endTime = System.currentTimeMillis() + 86400000 + 7200000, // 2 hours
            organizer = "Local Satsang Group",
            category = "Satsang",
            isOnline = false,
            registrationRequired = true,
            maxAttendees = 100,
            currentAttendees = 67,
            imageColor = Primary
        ),
        SpiritualEvent(
            id = "2",
            title = "Online Mahamrityunjaya Mantra Jaap",
            description = "108 times chanting for healing and protection",
            location = "Zoom Meeting",
            startTime = System.currentTimeMillis() + 3600000, // 1 hour from now
            endTime = System.currentTimeMillis() + 3600000 + 5400000, // 1.5 hours
            organizer = "Divine Chants Group",
            category = "Mantra",
            isOnline = true,
            registrationRequired = false,
            maxAttendees = null,
            currentAttendees = 234,
            imageColor = Secondary
        ),
        SpiritualEvent(
            id = "3",
            title = "Yoga and Meditation Retreat",
            description = "3-day intensive spiritual practice retreat",
            location = "Rishikesh Ashram",
            startTime = System.currentTimeMillis() + 604800000, // Next week
            endTime = System.currentTimeMillis() + 604800000 + 259200000, // 3 days
            organizer = "Himalayan Yoga Center",
            category = "Retreat",
            isOnline = false,
            registrationRequired = true,
            maxAttendees = 50,
            currentAttendees = 23,
            price = "₹2500",
            imageColor = MeditationBlue
        ),
        SpiritualEvent(
            id = "4",
            title = "Bhagavad Gita Discourse",
            description = "Weekly study of Chapter 4 - Jnana Karma Sanyasa Yoga",
            location = "Local Temple Hall",
            startTime = System.currentTimeMillis() + 259200000, // 3 days
            endTime = System.currentTimeMillis() + 259200000 + 5400000, // 1.5 hours
            organizer = "Spiritual Study Circle",
            category = "Study",
            isOnline = false,
            registrationRequired = false,
            maxAttendees = 80,
            currentAttendees = 45,
            imageColor = AccentGold
        ),
        SpiritualEvent(
            id = "5",
            title = "Full Moon Meditation",
            description = "Monthly full moon group meditation and energy healing",
            location = "Online & Chandigarh Center",
            startTime = System.currentTimeMillis() + 1209600000, // 2 weeks
            endTime = System.currentTimeMillis() + 1209600000 + 3600000, // 1 hour
            organizer = "Lunar Energy Circle",
            category = "Meditation",
            isOnline = true,
            registrationRequired = true,
            maxAttendees = 200,
            currentAttendees = 89,
            imageColor = AccentRed
        )
    )
    
    val categories = listOf("All", "Satsang", "Mantra", "Retreat", "Study", "Meditation")
    val timeFilters = listOf("Upcoming", "This Week", "This Month")
    
    val filteredEvents = events.filter { event ->
        val categoryMatch = selectedCategory == "All" || event.category == selectedCategory
        val timeMatch = when (selectedTimeFilter) {
            "This Week" -> event.startTime <= System.currentTimeMillis() + 604800000
            "This Month" -> event.startTime <= System.currentTimeMillis() + 2592000000
            else -> true // Upcoming shows all
        }
        categoryMatch && timeMatch
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        
        // Header
        Text(
            text = "🎯 Spiritual Events",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
        Text(
            text = "Join community gatherings and sacred ceremonies",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Filters
        Column {
            // Category Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            
            // Time Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(timeFilters) { timeFilter ->
                    FilterChip(
                        onClick = { selectedTimeFilter = timeFilter },
                        label = { Text(timeFilter) },
                        selected = selectedTimeFilter == timeFilter,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Secondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
        
        // Events List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredEvents) { event ->
                EventCard(
                    event = event,
                    onClick = { /* Navigate to event details */ }
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: SpiritualEvent,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Event Header Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(event.imageColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // Category Icon
                when (event.category) {
                    "Satsang" -> Text("🕉️", style = MaterialTheme.typography.displaySmall)
                    "Mantra" -> Text("📿", style = MaterialTheme.typography.displaySmall)
                    "Retreat" -> Text("🏔️", style = MaterialTheme.typography.displaySmall)
                    "Study" -> Text("📚", style = MaterialTheme.typography.displaySmall)
                    "Meditation" -> Text("🧘", style = MaterialTheme.typography.displaySmall)
                    else -> Text("🎯", style = MaterialTheme.typography.displaySmall)
                }
                
                // Online indicator
                if (event.isOnline) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MeditationBlue)
                    ) {
                        Text(
                            text = "ONLINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                // Price indicator
                if (event.price != "Free") {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = AccentGold)
                    ) {
                        Text(
                            text = event.price,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            // Event Details
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Title and Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurface.copy(alpha = 0.7f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // Date Card
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = event.imageColor.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatEventDate(event.startTime).first,
                                style = MaterialTheme.typography.labelSmall,
                                color = event.imageColor,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = formatEventDate(event.startTime).second,
                                style = MaterialTheme.typography.titleMedium,
                                color = event.imageColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Event Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Location & Time
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (event.isOnline) Icons.Default.Videocam else Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = OnSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = event.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurface.copy(alpha = 0.8f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Time",
                                tint = OnSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatEventTime(event.startTime),
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                    
                    // Attendees & Organizer
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = "Attendees",
                                tint = OnSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (event.maxAttendees != null) {
                                    "${event.currentAttendees}/${event.maxAttendees}"
                                } else {
                                    "${event.currentAttendees} attending"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurface.copy(alpha = 0.8f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Organizer",
                                tint = OnSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = event.organizer,
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* Register/Join */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = event.imageColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (event.registrationRequired) Icons.Default.AppRegistration else Icons.Default.Login,
                            contentDescription = if (event.registrationRequired) "Register" else "Join",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (event.registrationRequired) "Register" else "Join")
                    }
                    
                    OutlinedButton(
                        onClick = { /* Share */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share")
                    }
                }
            }
        }
    }
}

fun formatEventDate(timestamp: Long): Pair<String, String> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    
    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    
    return Pair(
        monthFormat.format(calendar.time).uppercase(),
        dayFormat.format(calendar.time)
    )
}

fun formatEventTime(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val now = Calendar.getInstance()
    
    val isToday = calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) &&
                  calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)
    
    val isTomorrow = calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) + 1 &&
                     calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)
    
    return when {
        isToday -> "Today ${timeFormat.format(calendar.time)}"
        isTomorrow -> "Tomorrow ${timeFormat.format(calendar.time)}"
        else -> {
            val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
            "${dateFormat.format(calendar.time)} ${timeFormat.format(calendar.time)}"
        }
    }
} 