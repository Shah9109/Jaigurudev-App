package com.example.jaigurudevapp.data

import com.example.jaigurudevapp.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// Enhanced Data Models for Backend
data class SpiritualVideo(
    val id: String,
    val title: String,
    val description: String,
    val creator: String,
    val duration: String,
    val views: String,
    val uploadDate: String,
    val category: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val isLive: Boolean = false,
    val language: String = "Hindi",
    val quality: String = "HD",
    val rating: Float = 4.5f,
    val likes: Int = 0,
    val comments: Int = 0,
    val tags: List<String> = emptyList()
)

data class SpiritualMusic(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val category: String,
    val audioUrl: String,
    val thumbnailUrl: String,
    val isDownloaded: Boolean = false,
    val language: String = "Sanskrit",
    val lyrics: String = "",
    val rating: Float = 4.5f,
    val playCount: Int = 0,
    val releaseYear: Int = 2024,
    val tags: List<String> = emptyList()
)

data class SpiritualBook(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val category: String,
    val pages: Int,
    val rating: Float,
    val coverImageUrl: String,
    val pdfUrl: String,
    val language: String = "Hindi",
    val publishYear: Int,
    val publisher: String,
    val isbn: String = "",
    val readTime: String,
    val chapters: List<String> = emptyList(),
    val isDownloaded: Boolean = false,
    val isFavorite: Boolean = false,
    val tags: List<String> = emptyList()
)

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
    val imageUrl: String,
    val contactInfo: String = "",
    val requirements: List<String> = emptyList()
)

// Backend Repository Class
class SpiritualRepository {
    
    companion object {
        @Volatile
        private var INSTANCE: SpiritualRepository? = null
        
        fun getInstance(): SpiritualRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SpiritualRepository().also { INSTANCE = it }
            }
        }
    }
    
    // Comprehensive Video Database
    fun getAllVideos(): List<SpiritualVideo> = listOf(
        SpiritualVideo(
            id = "v001",
            title = "Bhagavad Gita Chapter 1 - Complete Explanation",
            description = "Detailed explanation of the first chapter of Bhagavad Gita with Sanskrit verses and Hindi translation",
            creator = "Gurudev Spiritual Academy",
            duration = "1:45:30",
            views = "125K",
            uploadDate = "2 days ago",
            category = "Satsang",
            thumbnailUrl = "https://example.com/gita_ch1_thumb.jpg",
            videoUrl = "https://example.com/gita_ch1_video.mp4",
            language = "Hindi",
            quality = "4K",
            rating = 4.9f,
            likes = 5420,
            comments = 234,
            tags = listOf("Bhagavad Gita", "Krishna", "Arjuna", "Dharma", "Sanskrit")
        ),
        SpiritualVideo(
            id = "v002",
            title = "Morning Meditation - 30 Minutes Guided Session",
            description = "Start your day with peace and tranquility through this guided meditation session",
            creator = "Meditation Masters",
            duration = "30:00",
            views = "89K",
            uploadDate = "1 week ago",
            category = "Meditation",
            thumbnailUrl = "https://example.com/meditation_thumb.jpg",
            videoUrl = "https://example.com/meditation_video.mp4",
            language = "English",
            quality = "HD",
            rating = 4.8f,
            likes = 3210,
            comments = 156,
            tags = listOf("Meditation", "Morning", "Guided", "Peace", "Mindfulness")
        ),
        SpiritualVideo(
            id = "v003",
            title = "Hanuman Chalisa - Complete Recitation with Meaning",
            description = "Beautiful recitation of Hanuman Chalisa with word-by-word meaning and significance",
            creator = "Divine Chants",
            duration = "15:45",
            views = "234K",
            uploadDate = "3 days ago",
            category = "Devotional",
            thumbnailUrl = "https://example.com/hanuman_thumb.jpg",
            videoUrl = "https://example.com/hanuman_video.mp4",
            language = "Hindi",
            quality = "HD",
            rating = 4.9f,
            likes = 8765,
            comments = 432,
            tags = listOf("Hanuman", "Chalisa", "Devotional", "Prayer", "Protection")
        ),
        SpiritualVideo(
            id = "v004",
            title = "Live Satsang - Questions and Answers",
            description = "Live interactive session answering spiritual questions from devotees worldwide",
            creator = "Spiritual Wisdom Channel",
            duration = "LIVE",
            views = "1.2K",
            uploadDate = "Live now",
            category = "Satsang",
            thumbnailUrl = "https://example.com/live_satsang_thumb.jpg",
            videoUrl = "https://example.com/live_satsang_stream.m3u8",
            isLive = true,
            language = "Hindi",
            quality = "HD",
            rating = 4.7f,
            likes = 456,
            comments = 89,
            tags = listOf("Live", "Satsang", "Q&A", "Interactive", "Wisdom")
        ),
        SpiritualVideo(
            id = "v005",
            title = "Ramayana Stories for Children",
            description = "Beautiful animated stories from Ramayana explained in simple language for children",
            creator = "Kids Spiritual Stories",
            duration = "25:30",
            views = "156K",
            uploadDate = "5 days ago",
            category = "Stories",
            thumbnailUrl = "https://example.com/ramayana_kids_thumb.jpg",
            videoUrl = "https://example.com/ramayana_kids_video.mp4",
            language = "Hindi",
            quality = "HD",
            rating = 4.8f,
            likes = 4567,
            comments = 234,
            tags = listOf("Ramayana", "Children", "Stories", "Animation", "Values")
        )
    )
    
    // Comprehensive Music Database
    fun getAllMusic(): List<SpiritualMusic> = listOf(
        SpiritualMusic(
            id = "m001",
            title = "Om Namah Shivaya - 108 Times",
            artist = "Divine Chants Orchestra",
            album = "Sacred Mantras Vol 1",
            duration = "27:30",
            category = "Mantras",
            audioUrl = "https://example.com/om_namah_shivaya.mp3",
            thumbnailUrl = "https://example.com/om_namah_thumb.jpg",
            language = "Sanskrit",
            lyrics = "Om Namah Shivaya Om Namah Shivaya Om Namah Shivaya...",
            rating = 4.9f,
            playCount = 45230,
            releaseYear = 2024,
            tags = listOf("Shiva", "Mantra", "108", "Sacred", "Meditation")
        ),
        SpiritualMusic(
            id = "m002",
            title = "Gayatri Mantra - Traditional",
            artist = "Sanskrit Scholars",
            album = "Vedic Chants",
            duration = "8:15",
            category = "Mantras",
            audioUrl = "https://example.com/gayatri_mantra.mp3",
            thumbnailUrl = "https://example.com/gayatri_thumb.jpg",
            language = "Sanskrit",
            lyrics = "Om bhur bhuva swaha, Tat savitur varenyam...",
            rating = 4.8f,
            playCount = 32145,
            releaseYear = 2023,
            tags = listOf("Gayatri", "Vedic", "Sacred", "Morning", "Wisdom")
        ),
        SpiritualMusic(
            id = "m003",
            title = "Krishna Bhajan Medley",
            artist = "Devotional Singers",
            album = "Krishna Love Songs",
            duration = "12:45",
            category = "Devotional",
            audioUrl = "https://example.com/krishna_bhajan.mp3",
            thumbnailUrl = "https://example.com/krishna_thumb.jpg",
            language = "Hindi",
            lyrics = "Radhe Krishna, Radhe Krishna, Krishna Krishna Radhe Radhe...",
            rating = 4.9f,
            playCount = 67890,
            releaseYear = 2024,
            tags = listOf("Krishna", "Radha", "Bhajan", "Love", "Devotion")
        ),
        SpiritualMusic(
            id = "m004",
            title = "Shanti Mantra - Peace Chant",
            artist = "Peaceful Hearts",
            album = "Inner Peace Collection",
            duration = "15:00",
            category = "Meditation",
            audioUrl = "https://example.com/shanti_mantra.mp3",
            thumbnailUrl = "https://example.com/shanti_thumb.jpg",
            language = "Sanskrit",
            lyrics = "Om shanti shanti shanti...",
            rating = 4.7f,
            playCount = 23456,
            releaseYear = 2024,
            tags = listOf("Peace", "Shanti", "Calm", "Meditation", "Healing")
        ),
        SpiritualMusic(
            id = "m005",
            title = "Aarti Sangrah - Evening Prayers",
            artist = "Temple Singers",
            album = "Daily Aarti Collection",
            duration = "18:30",
            category = "Prayers",
            audioUrl = "https://example.com/aarti_sangrah.mp3",
            thumbnailUrl = "https://example.com/aarti_thumb.jpg",
            language = "Hindi",
            lyrics = "Om Jai Jagdish Hare, Swami Jai Jagdish Hare...",
            rating = 4.8f,
            playCount = 54321,
            releaseYear = 2023,
            tags = listOf("Aarti", "Evening", "Prayer", "Temple", "Devotion")
        )
    )
    
    // Comprehensive Books Database
    fun getAllBooks(): List<SpiritualBook> = listOf(
        SpiritualBook(
            id = "b001",
            title = "Bhagavad Gita - Complete with Commentary",
            author = "Vyasa Maharshi",
            description = "The eternal dialogue between Lord Krishna and Arjuna with detailed commentary and explanations",
            category = "Scriptures",
            pages = 1200,
            rating = 4.9f,
            coverImageUrl = "https://example.com/bhagavad_gita_cover.jpg",
            pdfUrl = "https://example.com/bhagavad_gita.pdf",
            language = "Hindi",
            publishYear = 2024,
            publisher = "Spiritual Publications",
            isbn = "978-93-123456-01-1",
            readTime = "45 days",
            chapters = listOf(
                "Arjuna Vishada Yoga", "Sankhya Yoga", "Karma Yoga", "Jnana Karma Sanyasa Yoga",
                "Karma Sanyasa Yoga", "Atmasamyama Yoga", "Paramahamsa Vijnana Yoga", "Akshara Parabrahma Yoga",
                "Rajavidya Rajaguhya Yoga", "Vibhuti Yoga", "Vishvarupa Darshana Yoga", "Bhakti Yoga",
                "Kshetra Kshetrajna Vibhaga Yoga", "Gunatraya Vibhaga Yoga", "Purushottama Yoga", "Daivasura Sampad Vibhaga Yoga",
                "Shraddhatraya Vibhaga Yoga", "Moksha Sanyasa Yoga"
            ),
            tags = listOf("Krishna", "Arjuna", "Dharma", "Yoga", "Philosophy", "Vedic")
        ),
        SpiritualBook(
            id = "b002",
            title = "Ramayana - The Epic Journey",
            author = "Maharshi Valmiki",
            description = "The complete story of Lord Rama's life, His exile, and the victory of good over evil",
            category = "Scriptures",
            pages = 950,
            rating = 4.8f,
            coverImageUrl = "https://example.com/ramayana_cover.jpg",
            pdfUrl = "https://example.com/ramayana.pdf",
            language = "Hindi",
            publishYear = 2023,
            publisher = "Sacred Texts Publishers",
            isbn = "978-93-123456-02-2",
            readTime = "35 days",
            chapters = listOf(
                "Bala Kanda", "Ayodhya Kanda", "Aranya Kanda", "Kishkindha Kanda",
                "Sundara Kanda", "Yuddha Kanda", "Uttara Kanda"
            ),
            tags = listOf("Rama", "Sita", "Hanuman", "Dharma", "Epic", "Values")
        ),
        SpiritualBook(
            id = "b003",
            title = "Meditation for Beginners",
            author = "Spiritual Guide Anand",
            description = "Complete guide to meditation techniques, breathing exercises, and achieving inner peace",
            category = "Practice",
            pages = 280,
            rating = 4.7f,
            coverImageUrl = "https://example.com/meditation_guide_cover.jpg",
            pdfUrl = "https://example.com/meditation_guide.pdf",
            language = "English",
            publishYear = 2024,
            publisher = "Mindful Living Publications",
            isbn = "978-93-123456-03-3",
            readTime = "12 days",
            chapters = listOf(
                "Understanding Meditation", "Breathing Techniques", "Postures and Environment",
                "Mindfulness Practice", "Concentration Methods", "Advanced Techniques",
                "Daily Practice", "Overcoming Obstacles", "Benefits of Meditation"
            ),
            tags = listOf("Meditation", "Mindfulness", "Peace", "Practice", "Beginner")
        ),
        SpiritualBook(
            id = "b004",
            title = "108 Sacred Mantras",
            author = "Pandit Vedic Sharma",
            description = "Collection of the most powerful mantras with their meanings, pronunciation, and benefits",
            category = "Mantras",
            pages = 350,
            rating = 4.8f,
            coverImageUrl = "https://example.com/mantras_cover.jpg",
            pdfUrl = "https://example.com/sacred_mantras.pdf",
            language = "Sanskrit",
            publishYear = 2024,
            publisher = "Vedic Wisdom Press",
            isbn = "978-93-123456-04-4",
            readTime = "20 days",
            chapters = listOf(
                "Introduction to Mantras", "Ganesh Mantras", "Shiva Mantras", "Vishnu Mantras",
                "Devi Mantras", "Healing Mantras", "Protection Mantras", "Prosperity Mantras",
                "Peace Mantras", "Daily Mantras"
            ),
            tags = listOf("Mantras", "Sanskrit", "Sacred", "Healing", "Protection")
        ),
        SpiritualBook(
            id = "b005",
            title = "Spiritual Stories for Children",
            author = "Grandma Gita",
            description = "Beautiful collection of moral and spiritual stories to inspire young minds",
            category = "Stories",
            pages = 180,
            rating = 4.9f,
            coverImageUrl = "https://example.com/kids_stories_cover.jpg",
            pdfUrl = "https://example.com/spiritual_stories_kids.pdf",
            language = "Hindi",
            publishYear = 2024,
            publisher = "Children's Spiritual Books",
            isbn = "978-93-123456-05-5",
            readTime = "8 days",
            chapters = listOf(
                "The Honest Woodcutter", "The Devoted Bhakt", "The Wise Elephant",
                "The Magical Tree", "The Kind Princess", "The Brave Little Monk",
                "The Generous Farmer", "The Learning Bird", "The Peaceful Village"
            ),
            tags = listOf("Children", "Stories", "Moral", "Values", "Learning")
        ),
        SpiritualBook(
            id = "b006",
            title = "Yoga Sutras of Patanjali",
            author = "Maharshi Patanjali",
            description = "The foundational text of yoga philosophy with detailed commentary and practical applications",
            category = "Philosophy",
            pages = 420,
            rating = 4.8f,
            coverImageUrl = "https://example.com/yoga_sutras_cover.jpg",
            pdfUrl = "https://example.com/yoga_sutras.pdf",
            language = "Sanskrit",
            publishYear = 2023,
            publisher = "Yoga Philosophy Publications",
            isbn = "978-93-123456-06-6",
            readTime = "25 days",
            chapters = listOf(
                "Samadhi Pada", "Sadhana Pada", "Vibhuti Pada", "Kaivalya Pada"
            ),
            tags = listOf("Yoga", "Philosophy", "Patanjali", "Sutras", "Practice")
        )
    )
    
    // Comprehensive Events Database
    fun getAllEvents(): List<SpiritualEvent> = listOf(
        SpiritualEvent(
            id = "e001",
            title = "Ram Navami Celebration",
            description = "Grand celebration of Lord Rama's birth with bhajans, aarti, and prasad distribution",
            location = "Mohali Community Center",
            startTime = System.currentTimeMillis() + 86400000, // Tomorrow
            endTime = System.currentTimeMillis() + 86400000 + 28800000, // 8 hours
            organizer = "Ram Bhakt Samaj",
            category = "Festival",
            isOnline = false,
            registrationRequired = true,
            maxAttendees = 500,
            currentAttendees = 234,
            imageUrl = "https://example.com/ram_navami_event.jpg",
            contactInfo = "contact@rambhaktsamaj.org",
            requirements = listOf("Registration Required", "Free Entry", "Prasad Available")
        ),
        SpiritualEvent(
            id = "e002",
            title = "Online Meditation Workshop",
            description = "Learn advanced meditation techniques from experienced teachers",
            location = "Zoom Meeting",
            startTime = System.currentTimeMillis() + 259200000, // 3 days
            endTime = System.currentTimeMillis() + 259200000 + 7200000, // 2 hours
            organizer = "Meditation Academy",
            category = "Workshop",
            isOnline = true,
            registrationRequired = true,
            maxAttendees = 100,
            currentAttendees = 67,
            price = "₹500",
            imageUrl = "https://example.com/meditation_workshop.jpg",
            contactInfo = "info@meditationacademy.com",
            requirements = listOf("Zoom App", "Quiet Environment", "Meditation Mat")
        )
    )
    
    // Search Functions
    fun searchVideos(query: String): List<SpiritualVideo> {
        return getAllVideos().filter { 
            it.title.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true) ||
            it.creator.contains(query, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
    
    fun searchMusic(query: String): List<SpiritualMusic> {
        return getAllMusic().filter { 
            it.title.contains(query, ignoreCase = true) ||
            it.artist.contains(query, ignoreCase = true) ||
            it.album.contains(query, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
    
    fun searchBooks(query: String): List<SpiritualBook> {
        return getAllBooks().filter { 
            it.title.contains(query, ignoreCase = true) ||
            it.author.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
    
    // Filter Functions
    fun getVideosByCategory(category: String): List<SpiritualVideo> {
        return getAllVideos().filter { it.category == category }
    }
    
    fun getMusicByCategory(category: String): List<SpiritualMusic> {
        return getAllMusic().filter { it.category == category }
    }
    
    fun getBooksByCategory(category: String): List<SpiritualBook> {
        return getAllBooks().filter { it.category == category }
    }
    
    // Favorites Management
    private val favoriteVideos = mutableSetOf<String>()
    private val favoriteMusic = mutableSetOf<String>()
    private val favoriteBooks = mutableSetOf<String>()
    
    fun addToFavorites(id: String, type: String) {
        when (type) {
            "video" -> favoriteVideos.add(id)
            "music" -> favoriteMusic.add(id)
            "book" -> favoriteBooks.add(id)
        }
    }
    
    fun removeFromFavorites(id: String, type: String) {
        when (type) {
            "video" -> favoriteVideos.remove(id)
            "music" -> favoriteMusic.remove(id)
            "book" -> favoriteBooks.remove(id)
        }
    }
    
    fun getFavoriteVideos(): List<SpiritualVideo> {
        return getAllVideos().filter { it.id in favoriteVideos }
    }
    
    fun getFavoriteMusic(): List<SpiritualMusic> {
        return getAllMusic().filter { it.id in favoriteMusic }
    }
    
    fun getFavoriteBooks(): List<SpiritualBook> {
        return getAllBooks().filter { it.id in favoriteBooks }
    }
    
    // Advanced Backend Features
    
    // Get single items by ID with error handling
    fun getVideoById(id: String): SpiritualVideo? {
        return try {
            getAllVideos().find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
    
    fun getMusicById(id: String): SpiritualMusic? {
        return try {
            getAllMusic().find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
    
    fun getBookById(id: String): SpiritualBook? {
        return try {
            getAllBooks().find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
    
    fun getEventById(id: String): SpiritualEvent? {
        return try {
            getAllEvents().find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
    
    // Category management
    fun getAllVideoCategories(): List<String> {
        return try {
            getAllVideos().map { it.category }.distinct().sorted()
        } catch (e: Exception) {
            listOf("Satsang", "Meditation", "Devotional", "Stories")
        }
    }
    
    fun getAllMusicCategories(): List<String> {
        return try {
            getAllMusic().map { it.category }.distinct().sorted()
        } catch (e: Exception) {
            listOf("Mantras", "Devotional", "Meditation", "Prayers")
        }
    }
    
    fun getAllBookCategories(): List<String> {
        return try {
            getAllBooks().map { it.category }.distinct().sorted()
        } catch (e: Exception) {
            listOf("Scriptures", "Practice", "Mantras", "Stories", "Philosophy")
        }
    }
    
    // Popular content based on ratings and views
    fun getPopularVideos(limit: Int = 10): List<SpiritualVideo> {
        return try {
            getAllVideos()
                .sortedByDescending { it.rating * it.likes }
                .take(limit)
        } catch (e: Exception) {
            getAllVideos().take(limit)
        }
    }
    
    fun getPopularMusic(limit: Int = 10): List<SpiritualMusic> {
        return try {
            getAllMusic()
                .sortedByDescending { it.rating * it.playCount }
                .take(limit)
        } catch (e: Exception) {
            getAllMusic().take(limit)
        }
    }
    
    fun getPopularBooks(limit: Int = 10): List<SpiritualBook> {
        return try {
            getAllBooks()
                .sortedByDescending { it.rating }
                .take(limit)
        } catch (e: Exception) {
            getAllBooks().take(limit)
        }
    }
    
    // Recent content
    fun getRecentVideos(limit: Int = 5): List<SpiritualVideo> {
        return try {
            getAllVideos().take(limit)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getRecentMusic(limit: Int = 5): List<SpiritualMusic> {
        return try {
            getAllMusic().take(limit)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getRecentBooks(limit: Int = 5): List<SpiritualBook> {
        return try {
            getAllBooks().take(limit)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Live content
    fun getLiveVideos(): List<SpiritualVideo> {
        return try {
            getAllVideos().filter { it.isLive }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Language filtering
    fun getVideosByLanguage(language: String): List<SpiritualVideo> {
        return try {
            getAllVideos().filter { it.language.equals(language, ignoreCase = true) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getMusicByLanguage(language: String): List<SpiritualMusic> {
        return try {
            getAllMusic().filter { it.language.equals(language, ignoreCase = true) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getBooksByLanguage(language: String): List<SpiritualBook> {
        return try {
            getAllBooks().filter { it.language.equals(language, ignoreCase = true) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Trending content (simulated based on views and ratings)
    fun getTrendingVideos(limit: Int = 10): List<SpiritualVideo> {
        return try {
            getAllVideos()
                .filter { !it.isLive }
                .sortedByDescending { 
                    val viewsNumber = it.views.replace("K", "000").replace("M", "000000").replace(",", "").toIntOrNull() ?: 0
                    viewsNumber * it.rating
                }
                .take(limit)
        } catch (e: Exception) {
            getAllVideos().take(limit)
        }
    }
    
    // Advanced search with multiple criteria
    fun searchContent(
        query: String,
        contentType: String = "all", // "videos", "music", "books", "all"
        category: String = "",
        language: String = ""
    ): SearchResult {
        return try {
            val videos = if (contentType == "all" || contentType == "videos") {
                searchVideos(query)
                    .let { if (category.isNotEmpty()) it.filter { video -> video.category.equals(category, ignoreCase = true) } else it }
                    .let { if (language.isNotEmpty()) it.filter { video -> video.language.equals(language, ignoreCase = true) } else it }
            } else emptyList()
            
            val music = if (contentType == "all" || contentType == "music") {
                searchMusic(query)
                    .let { if (category.isNotEmpty()) it.filter { track -> track.category.equals(category, ignoreCase = true) } else it }
                    .let { if (language.isNotEmpty()) it.filter { track -> track.language.equals(language, ignoreCase = true) } else it }
            } else emptyList()
            
            val books = if (contentType == "all" || contentType == "books") {
                searchBooks(query)
                    .let { if (category.isNotEmpty()) it.filter { book -> book.category.equals(category, ignoreCase = true) } else it }
                    .let { if (language.isNotEmpty()) it.filter { book -> book.language.equals(language, ignoreCase = true) } else it }
            } else emptyList()
            
            SearchResult(videos, music, books)
        } catch (e: Exception) {
            SearchResult(emptyList(), emptyList(), emptyList())
        }
    }
    
    // Recommendation system (basic implementation)
    fun getRecommendedVideos(basedOnVideoId: String, limit: Int = 5): List<SpiritualVideo> {
        return try {
            val baseVideo = getVideoById(basedOnVideoId)
            if (baseVideo != null) {
                getAllVideos()
                    .filter { it.id != basedOnVideoId }
                    .filter { it.category == baseVideo.category || it.tags.any { tag -> baseVideo.tags.contains(tag) } }
                    .sortedByDescending { it.rating }
                    .take(limit)
            } else {
                getPopularVideos(limit)
            }
        } catch (e: Exception) {
            getPopularVideos(limit)
        }
    }
    
    fun getRecommendedMusic(basedOnMusicId: String, limit: Int = 5): List<SpiritualMusic> {
        return try {
            val baseMusic = getMusicById(basedOnMusicId)
            if (baseMusic != null) {
                getAllMusic()
                    .filter { it.id != basedOnMusicId }
                    .filter { it.category == baseMusic.category || it.tags.any { tag -> baseMusic.tags.contains(tag) } }
                    .sortedByDescending { it.rating }
                    .take(limit)
            } else {
                getPopularMusic(limit)
            }
        } catch (e: Exception) {
            getPopularMusic(limit)
        }
    }
    
    fun getRecommendedBooks(basedOnBookId: String, limit: Int = 5): List<SpiritualBook> {
        return try {
            val baseBook = getBookById(basedOnBookId)
            if (baseBook != null) {
                getAllBooks()
                    .filter { it.id != basedOnBookId }
                    .filter { it.category == baseBook.category || it.tags.any { tag -> baseBook.tags.contains(tag) } }
                    .sortedByDescending { it.rating }
                    .take(limit)
            } else {
                getPopularBooks(limit)
            }  
        } catch (e: Exception) {
            getPopularBooks(limit)
        }
    }
    
    // Validation functions
    fun isValidVideoId(id: String): Boolean {
        return try {
            getAllVideos().any { it.id == id }
        } catch (e: Exception) {
            false
        }
    }
    
    fun isValidMusicId(id: String): Boolean {
        return try {
            getAllMusic().any { it.id == id }
        } catch (e: Exception) {
            false
        }
    }
    
    fun isValidBookId(id: String): Boolean {
        return try {
            getAllBooks().any { it.id == id }
        } catch (e: Exception) {
            false
        }
    }
    
    // Statistics
    fun getContentStats(): ContentStats {
        return try {
            ContentStats(
                totalVideos = getAllVideos().size,
                totalMusic = getAllMusic().size,
                totalBooks = getAllBooks().size,
                totalEvents = getAllEvents().size,
                liveVideos = getLiveVideos().size
            )
        } catch (e: Exception) {
            ContentStats(0, 0, 0, 0, 0)
        }
    }
}

// Data classes for advanced features
data class SearchResult(
    val videos: List<SpiritualVideo>,
    val music: List<SpiritualMusic>,
    val books: List<SpiritualBook>
)

data class ContentStats(
    val totalVideos: Int,
    val totalMusic: Int,
    val totalBooks: Int,
    val totalEvents: Int,
    val liveVideos: Int
) 