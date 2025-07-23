package com.example.jaigurudevapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class Meeting(
    val id: String,
    val title: String,
    val description: String,
    val host: String,
    val scheduledTime: Long,
    val duration: Int, // in minutes
    val participants: Int,
    val maxParticipants: Int,
    val meetingCode: String,
    val isLive: Boolean = false,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingsScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    var showJoinDialog by remember { mutableStateOf(false) }
    var meetingCode by remember { mutableStateOf("") }
    
    val upcomingMeetings = listOf(
        Meeting("1", "Morning Meditation Satsang", "Daily guided meditation session", "Gurudev", 
                System.currentTimeMillis() + 1800000, 60, 45, 100, "MORNING123", false, "Meditation"),
        Meeting("2", "Bhagavad Gita Study Circle", "Chapter 2 discussion", "Spiritual Teacher", 
                System.currentTimeMillis() + 3600000, 90, 32, 50, "GITA456", false, "Study"),
        Meeting("3", "Evening Satsang", "Community prayers and chanting", "Community Hall", 
                System.currentTimeMillis() + 7200000, 45, 78, 150, "EVENING789", false, "Satsang"),
        Meeting("4", "Live Kirtan Session", "Devotional singing with community", "Kirtan Group", 
                System.currentTimeMillis() - 1800000, 120, 156, 200, "KIRTAN101", true, "Devotional")
    )
    
    val pastMeetings = listOf(
        Meeting("5", "Yoga and Pranayama", "Morning yoga practice", "Yoga Master", 
                System.currentTimeMillis() - 86400000, 60, 67, 80, "YOGA234", false, "Yoga"),
        Meeting("6", "Spiritual Discourse", "Weekly wisdom sharing", "Wise Sage", 
                System.currentTimeMillis() - 172800000, 75, 89, 100, "WISDOM567", false, "Discourse")
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MeditationBlue)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🎯 Spiritual Satsangs",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Join live sessions and community gatherings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Quick Join Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = "Video Call",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Quick Join",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Button(
                            onClick = { showJoinDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MeditationBlue
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Join Meeting")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            color = Color.White
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Live & Upcoming", color = Color.White) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Past Sessions", color = Color.White) }
                    )
                }
            }
        }
        
        when (selectedTab) {
            0 -> MeetingsList(meetings = upcomingMeetings, isUpcoming = true)
            1 -> MeetingsList(meetings = pastMeetings, isUpcoming = false)
        }
    }
    
    // Join Meeting Dialog
    if (showJoinDialog) {
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            title = { Text("Join Satsang") },
            text = {
                Column {
                    Text("Enter meeting code to join:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = meetingCode,
                        onValueChange = { meetingCode = it },
                        label = { Text("Meeting Code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (meetingCode.isNotEmpty()) {
                            // Launch Jitsi Meet with the code
                            showJoinDialog = false
                            meetingCode = ""
                        }
                    }
                ) {
                    Text("Join")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun MeetingsList(meetings: List<Meeting>, isUpcoming: Boolean) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(meetings) { meeting ->
            MeetingCard(
                meeting = meeting,
                isUpcoming = isUpcoming,
                onJoinClick = {
                    // Launch Jitsi Meet for this meeting
                    // In real implementation, use Jitsi SDK here
                }
            )
        }
    }
}

@Composable
fun MeetingCard(
    meeting: Meeting,
    isUpcoming: Boolean,
    onJoinClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = meeting.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        
                        if (meeting.isLive) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = AccentRed)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.White, androidx.compose.foundation.shape.CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "LIVE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    
                    Text(
                        text = meeting.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface.copy(alpha = 0.7f)
                    )
                }
                
                // Category Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            when (meeting.category) {
                                "Meditation" -> Primary.copy(alpha = 0.2f)
                                "Study" -> Secondary.copy(alpha = 0.2f)
                                "Satsang" -> MeditationBlue.copy(alpha = 0.2f)
                                "Devotional" -> AccentGold.copy(alpha = 0.2f)
                                "Yoga" -> AccentRed.copy(alpha = 0.2f)
                                else -> OnSurface.copy(alpha = 0.1f)
                            },
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (meeting.category) {
                        "Meditation" -> Text("🧘", style = MaterialTheme.typography.titleMedium)
                        "Study" -> Text("📚", style = MaterialTheme.typography.titleMedium)
                        "Satsang" -> Text("🕉️", style = MaterialTheme.typography.titleMedium)
                        "Devotional" -> Text("🎵", style = MaterialTheme.typography.titleMedium)
                        "Yoga" -> Text("🤸", style = MaterialTheme.typography.titleMedium)
                        "Discourse" -> Text("🎤", style = MaterialTheme.typography.titleMedium)
                        else -> Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = "Meeting",
                            tint = OnSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Meeting Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Host",
                            tint = OnSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = meeting.host,
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
                            text = if (meeting.isLive) "Live now" else formatMeetingTime(meeting.scheduledTime),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (meeting.isLive) AccentRed else OnSurface.copy(alpha = 0.8f),
                            fontWeight = if (meeting.isLive) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Participants",
                            tint = OnSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${meeting.participants}/${meeting.maxParticipants}",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurface.copy(alpha = 0.8f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Duration",
                            tint = OnSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${meeting.duration} min",
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
                if (isUpcoming) {
                    Button(
                        onClick = onJoinClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (meeting.isLive) AccentRed else Primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = "Join",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (meeting.isLive) "Join Live" else "Join Meeting")
                    }
                    
                    OutlinedButton(
                        onClick = { /* Add to calendar */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventNote,
                            contentDescription = "Calendar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Calendar")
                    }
                } else {
                    OutlinedButton(
                        onClick = { /* View recording */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = "Recording",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Recording")
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

fun formatMeetingTime(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    
    val now = Calendar.getInstance()
    val isToday = calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) &&
                  calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)
    
    val isTomorrow = calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) + 1 &&
                     calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)
    
    return when {
        isToday -> "Today ${timeFormat.format(calendar.time)}"
        isTomorrow -> "Tomorrow ${timeFormat.format(calendar.time)}"
        else -> "${dateFormat.format(calendar.time)} ${timeFormat.format(calendar.time)}"
    }
} 