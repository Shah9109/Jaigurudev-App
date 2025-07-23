package com.example.jaigurudevapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val message: String,
    val timestamp: Long,
    val isFromCurrentUser: Boolean = false
)

data class ChatGroup(
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val lastMessage: String,
    val lastMessageTime: Long,
    val isActive: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    var showGroupDetails by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<ChatGroup?>(null) }
    
    val chatGroups = listOf(
        ChatGroup("1", "Morning Meditation", "Daily morning spiritual practice", 45, "See you all tomorrow at 6 AM!", System.currentTimeMillis() - 3600000),
        ChatGroup("2", "Evening Satsang", "Community evening discussions", 78, "Thank you for today's session 🙏", System.currentTimeMillis() - 7200000),
        ChatGroup("3", "Bhagavad Gita Study", "Weekly scripture study group", 32, "Chapter 2 discussion was amazing!", System.currentTimeMillis() - 14400000),
        ChatGroup("4", "Yoga Practitioners", "Share yoga experiences", 56, "New pose tutorial uploaded!", System.currentTimeMillis() - 21600000),
        ChatGroup("5", "Devotional Music", "Share and discuss spiritual music", 89, "Love the new kirtan recording!", System.currentTimeMillis() - 28800000)
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
            colors = CardDefaults.cardColors(containerColor = Primary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "💬 Spiritual Community",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Connect with fellow seekers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
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
                        text = { Text("Groups", color = Color.White) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Direct", color = Color.White) }
                    )
                }
            }
        }
        
        when (selectedTab) {
            0 -> {
                if (selectedGroup == null) {
                    GroupChatList(
                        groups = chatGroups,
                        onGroupClick = { group ->
                            selectedGroup = group
                        }
                    )
                } else {
                    GroupChatView(
                        group = selectedGroup!!,
                        onBackClick = { selectedGroup = null }
                    )
                }
            }
            1 -> {
                DirectChatList()
            }
        }
    }
}

@Composable
fun GroupChatList(
    groups: List<ChatGroup>,
    onGroupClick: (ChatGroup) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups) { group ->
            GroupChatCard(
                group = group,
                onClick = { onGroupClick(group) }
            )
        }
    }
}

@Composable
fun GroupChatCard(
    group: ChatGroup,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Primary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = formatTime(group.lastMessageTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurface.copy(alpha = 0.5f)
                    )
                }
                
                Text(
                    text = group.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = group.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurface.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = "${group.memberCount} members",
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )
            }
            
            if (group.isActive) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Secondary, CircleShape)
                )
            }
        }
    }
}

@Composable
fun GroupChatView(
    group: ChatGroup,
    onBackClick: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    
    // Sample messages for the group
    val messages = listOf(
        ChatMessage("1", "user1", "Rajesh", "Good morning everyone! Ready for today's meditation?", System.currentTimeMillis() - 3600000),
        ChatMessage("2", "user2", "Priya", "Yes! Looking forward to it 🙏", System.currentTimeMillis() - 3500000),
        ChatMessage("3", "current", "You", "Same here! The peace from yesterday's session was incredible", System.currentTimeMillis() - 3400000, true),
        ChatMessage("4", "user3", "Gurudev", "Remember, meditation is not about emptying the mind, but observing it with compassion", System.currentTimeMillis() - 3300000),
        ChatMessage("5", "user1", "Rajesh", "Thank you for that wisdom, Gurudev", System.currentTimeMillis() - 3200000),
        ChatMessage("6", "current", "You", "See you all at 6 AM tomorrow!", System.currentTimeMillis() - 3100000, true)
    )
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chat Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Primary)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = group.name.take(2).uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${group.memberCount} members",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                
                IconButton(onClick = { /* Show group info */ }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Group Info",
                        tint = Color.White
                    )
                }
            }
        }
        
        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        
        // Message Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type your message...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(
                    onClick = {
                        if (messageText.isNotEmpty()) {
                            // Send message (integrate with Firebase)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromCurrentUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Secondary.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.take(1),
                    style = MaterialTheme.typography.labelMedium,
                    color = Secondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (message.isFromCurrentUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (!message.isFromCurrentUser) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
            
            Card(
                shape = RoundedCornerShape(
                    topStart = if (message.isFromCurrentUser) 16.dp else 4.dp,
                    topEnd = if (message.isFromCurrentUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isFromCurrentUser) Primary else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isFromCurrentUser) Color.White else OnSurface,
                    modifier = Modifier.padding(12.dp)
                )
            }
            
            Text(
                text = formatTime(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = OnSurface.copy(alpha = 0.4f),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
        
        if (message.isFromCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Primary.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Y",
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary
                )
            }
        }
    }
}

@Composable
fun DirectChatList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💬",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Direct Messages",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Text(
                text = "Coming soon - Connect directly with community members",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurface.copy(alpha = 0.7f)
            )
        }
    }
}

fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    }
} 