package com.example.jaigurudevapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.jaigurudevapp.screens.*
import com.example.jaigurudevapp.data.SpiritualRepository

// Navigation routes
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val BOOKS = "books"
    const val VIDEOS = "videos"
    const val MUSIC = "music"
    const val CHAT = "chat"
    const val MEETINGS = "meetings"
    const val PROFILE = "profile"
    const val EVENTS = "events"
    const val MUSIC_PLAYER = "music_player/{songId}"
    const val PDF_VIEWER = "pdf_viewer/{bookId}"
    const val VIDEO_PLAYER = "video_player/{videoId}"
    
    // Helper functions for navigation with parameters
    fun musicPlayer(songId: String) = "music_player/$songId"
    fun pdfViewer(bookId: String) = "pdf_viewer/$bookId"
    fun videoPlayer(videoId: String) = "video_player/$videoId"
}

// Bottom navigation items - only Home, Books, Music, Videos
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(Routes.HOME, Icons.Default.Home, "Home")
    object Books : BottomNavItem(Routes.BOOKS, Icons.AutoMirrored.Filled.MenuBook, "Books")
    object Videos : BottomNavItem(Routes.VIDEOS, Icons.Default.VideoLibrary, "Videos")
    object Music : BottomNavItem(Routes.MUSIC, Icons.Default.LibraryMusic, "Music")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JaigurudevNavigation(navController: NavHostController) {
    var isLoggedIn by remember { mutableStateOf(false) }
    
    // Initialize backend repository
    val repository = remember { SpiritualRepository.getInstance() }
    
    // Safe navigation function
    fun safeNavigate(route: String) {
        try {
            navController.navigate(route)
        } catch (e: Exception) {
            // Handle navigation error gracefully - could log error or show toast
        }
    }
    
    if (!isLoggedIn) {
        // Show login/signup flow
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = { isLoggedIn = true },
                    onNavigateToSignup = { safeNavigate(Routes.SIGNUP) }
                )
            }
            composable(Routes.SIGNUP) {
                SignupScreen(
                    onSignupSuccess = { isLoggedIn = true },
                    onNavigateToLogin = { 
                        try {
                            navController.popBackStack()
                        } catch (e: Exception) {
                            // Handle gracefully
                        }
                    }
                )
            }
        }
    } else {
        // Show main app with bottom navigation
        val bottomNavItems = listOf(
            BottomNavItem.Home,
            BottomNavItem.Books,
            BottomNavItem.Videos, 
            BottomNavItem.Music
        )
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Jai Gurudev") },
                    navigationIcon = {
                        // Chat button in top-left
                        IconButton(
                            onClick = { safeNavigate(Routes.CHAT) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "Chat",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        // Profile button in top-right
                        IconButton(
                            onClick = { safeNavigate(Routes.PROFILE) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                try {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } catch (e: Exception) {
                                    // Handle navigation error gracefully
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Main screens
                composable(Routes.HOME) {
                    HomeScreen(navController = navController)
                }
                
                composable(Routes.BOOKS) {
                    BooksScreen(navController = navController)  
                }
                
                composable(Routes.VIDEOS) {
                    VideosScreen(navController = navController)
                }
                
                composable(Routes.MUSIC) {
                    MusicScreen(navController = navController)
                }
                
                composable(Routes.CHAT) {
                    ChatScreen(navController = navController)
                }
                
                composable(Routes.MEETINGS) {
                    MeetingsScreen(navController = navController)
                }
                
                composable(Routes.EVENTS) {
                    EventsScreen(navController = navController)
                }
                
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        navController = navController,
                        onLogout = { 
                            try {
                                isLoggedIn = false
                                navController.navigate(Routes.LOGIN) {
                                    popUpTo(0) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                // Handle logout error gracefully
                                isLoggedIn = false
                            }
                        }
                    )
                }
                
                // Player screens with comprehensive parameter validation
                composable(
                    route = Routes.MUSIC_PLAYER,
                    arguments = listOf(
                        navArgument("songId") { 
                            type = NavType.StringType
                            defaultValue = ""
                            nullable = false
                        }
                    )
                ) { backStackEntry ->
                    val songId = backStackEntry.arguments?.getString("songId") ?: ""
                    
                    // Validate songId exists in repository
                    val musicExists = remember(songId) {
                        try {
                            repository.isValidMusicId(songId)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    
                    if (musicExists && songId.isNotEmpty()) {
                        MusicPlayerScreen(
                            songId = songId,
                            navController = navController
                        )
                    } else {
                        ErrorScreen(
                            message = "Music not found",
                            onRetry = { 
                                try {
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    // Handle gracefully
                                }
                            }
                        )
                    }
                }
                
                composable(
                    route = Routes.PDF_VIEWER,
                    arguments = listOf(
                        navArgument("bookId") { 
                            type = NavType.StringType
                            defaultValue = ""
                            nullable = false
                        }
                    )
                ) { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                    
                    // Validate bookId exists in repository
                    val bookExists = remember(bookId) {
                        try {
                            repository.isValidBookId(bookId)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    
                    if (bookExists && bookId.isNotEmpty()) {
                        PDFViewerScreen(
                            bookId = bookId,
                            navController = navController
                        )
                    } else {
                        ErrorScreen(
                            message = "Book not found",
                            onRetry = { 
                                try {
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    // Handle gracefully
                                }
                            }
                        )
                    }
                }
                
                composable(
                    route = Routes.VIDEO_PLAYER,
                    arguments = listOf(
                        navArgument("videoId") { 
                            type = NavType.StringType
                            defaultValue = ""
                            nullable = false
                        }
                    )
                ) { backStackEntry ->
                    val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
                    
                    // Validate videoId exists in repository
                    val videoExists = remember(videoId) {
                        try {
                            repository.isValidVideoId(videoId)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    
                    if (videoExists && videoId.isNotEmpty()) {
                        VideoPlayerScreen(
                            videoId = videoId,
                            navController = navController
                        )
                    } else {
                        ErrorScreen(
                            message = "Video not found",
                            onRetry = { 
                                try {
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    // Handle gracefully
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Error screen component for graceful error handling
@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Retry")
        }
    }
} 