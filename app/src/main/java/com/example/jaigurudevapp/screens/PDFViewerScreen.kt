package com.example.jaigurudevapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jaigurudevapp.data.SpiritualRepository
import com.example.jaigurudevapp.data.SpiritualBook
import com.example.jaigurudevapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PDFViewerScreen(
    bookId: String,
    navController: NavHostController
) {
    val repository = remember { SpiritualRepository.getInstance() }
    val allBooks = remember { repository.getAllBooks() }
    
    // Find the current book by ID
    val bookData = remember(bookId) {
        allBooks.find { it.id == bookId } ?: allBooks.firstOrNull() ?: SpiritualBook(
            id = "default",
            title = "Spiritual Book",
            author = "Spiritual Author",
            description = "A wonderful spiritual book for seekers",
            category = "Practice",
            pages = 100,
            rating = 4.5f,
            coverImageUrl = "",
            pdfUrl = "",
            language = "Hindi",
            publishYear = 2024,
            publisher = "Spiritual Publications",
            readTime = "5 days"
        )
    }
    
    var currentPage by remember { mutableStateOf(1) }
    val totalPages = bookData.pages
    var bookmarkPages by remember { mutableStateOf(setOf<Int>()) }
    var showBookmarkDialog by remember { mutableStateOf(false) }
    var showChapterList by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        
        // Top Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Primary)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bookData.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "by ${bookData.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${bookData.category} • ${bookData.language}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = bookData.rating.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                IconButton(onClick = { showBookmarkDialog = true }) {
                    Icon(
                        imageVector = if (currentPage in bookmarkPages) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (currentPage in bookmarkPages) Color(0xFFFFD700) else Color.White
                    )
                }
                
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
            }
        }
        
        // Book Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Book cover placeholder
                Box(
                    modifier = Modifier
                        .size(60.dp, 80.dp)
                        .background(
                            Primary.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val emoji = when (bookData.category) {
                        "Scriptures" -> "📜"
                        "Philosophy" -> "🤔"
                        "Practice" -> "🧘"
                        "Mantras" -> "🕉️"
                        "Stories" -> "📖"
                        else -> "📚"
                    }
                    Text(
                        text = emoji,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${bookData.pages} pages • ${bookData.readTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Text(
                        text = "Published ${bookData.publishYear} by ${bookData.publisher}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    if (bookData.chapters.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showChapterList = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Chapters",
                                color = Primary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
        
        // PDF Content Area (Placeholder)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    
                    // Page header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = bookData.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        
                        Surface(
                            color = Primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Page $currentPage",
                                style = MaterialTheme.typography.labelMedium,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Sample spiritual content based on book category and page
                    Text(
                        text = getSampleBookContent(bookData, currentPage),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.6
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Page indicator
                    Text(
                        text = "--- Page $currentPage of $totalPages ---",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Bottom Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                
                // Page Slider
                Text(
                    text = "Page $currentPage of $totalPages",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Slider(
                    value = currentPage.toFloat(),
                    onValueChange = { currentPage = it.toInt() },
                    valueRange = 1f..totalPages.toFloat(),
                    steps = if (totalPages > 2) totalPages - 2 else 0,
                    colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = { if (currentPage > 1) currentPage-- },
                        enabled = currentPage > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.NavigateBefore,
                            contentDescription = "Previous Page",
                            tint = if (currentPage > 1) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                    
                    OutlinedButton(
                        onClick = { /* Go to specific page */ },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Go to page",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Go to Page")
                    }
                    
                    IconButton(
                        onClick = { if (currentPage < totalPages) currentPage++ },
                        enabled = currentPage < totalPages
                    ) {
                        Icon(
                            imageVector = Icons.Default.NavigateNext,
                            contentDescription = "Next Page",
                            tint = if (currentPage < totalPages) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
    
    // Bookmark Dialog
    if (showBookmarkDialog) {
        AlertDialog(
            onDismissRequest = { showBookmarkDialog = false },
            title = { Text("Bookmark Page") },
            text = { 
                Text("Do you want to bookmark page $currentPage?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        bookmarkPages = if (currentPage in bookmarkPages) {
                            bookmarkPages - currentPage
                        } else {
                            bookmarkPages + currentPage
                        }
                        showBookmarkDialog = false
                    }
                ) {
                    Text(if (currentPage in bookmarkPages) "Remove Bookmark" else "Add Bookmark")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookmarkDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Chapter List Dialog
    if (showChapterList && bookData.chapters.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showChapterList = false },
            title = { Text("Chapters") },
            text = {
                Column(
                    modifier = Modifier.height(300.dp)
                ) {
                    bookData.chapters.forEachIndexed { index, chapterTitle ->
                        val chapterNumber = index + 1
                        TextButton(
                            onClick = {
                                // Navigate to chapter (simplified: each chapter starts proportionally)
                                currentPage = (chapterNumber * (totalPages / bookData.chapters.size.coerceAtLeast(1))).coerceAtMost(totalPages)
                                showChapterList = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Chapter $chapterNumber",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = chapterTitle,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                        if (index < bookData.chapters.size - 1) {
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChapterList = false }) {
                    Text("Close")
                }
            }
        )
    }
}

fun getSampleBookContent(book: SpiritualBook, page: Int): String {
    return when (book.category) {
        "Scriptures" -> when {
            book.title.contains("Bhagavad Gita") -> getBhagavadGitaContent(page)
            book.title.contains("Ramayana") -> getRamayanaContent(page)
            else -> getGenericScriptureContent(book.title, page)
        }
        "Practice" -> getMeditationContent(page)
        "Philosophy" -> getPhilosophyContent(page)
        "Mantras" -> getMantraContent(page)
        "Stories" -> getStoryContent(page)
        else -> getGenericSpiritualContent(book.title, page)
    }
}

fun getBhagavadGitaContent(page: Int): String {
    return when (page) {
        1 -> """
            श्रीभगवद्गीता - अध्याय १
            अर्जुनविषादयोग
            
            धृतराष्ट्र उवाच |
            धर्मक्षेत्रे कुरुक्षेत्रे समवेता युयुत्सवः |
            मामकाः पाण्डवाश्चैव किमकुर्वत सञ्जय ||१||
            
            Dhritarashtra said: O Sanjaya, after my sons and the sons of Pandu assembled in the place of pilgrimage at Kurukshetra, desiring to fight, what did they do?
            
            This opening verse of the Bhagavad Gita sets the stage for one of the most profound spiritual dialogues in human history. The battlefield of Kurukshetra becomes the metaphor for the eternal struggle between dharma and adharma, between the forces of light and darkness that exist within every human heart.
        """.trimIndent()
        
        2 -> """
            संजय उवाच |
            दृष्ट्वा तु पाण्डवानीकं व्यूढं दुर्योधनस्तदा |
            आचार्यमुपसंगम्य राजा वचनमब्रवीत् ||२||
            
            Sanjaya said: O King, after looking over the army arranged in military formation by the sons of Pandu, King Duryodhana went to his teacher and spoke the following words.
            
            The great teacher Dronacharya represents the guru principle - that divine grace which comes through the lineage of wisdom. Even in the midst of conflict, the importance of seeking guidance from those who have walked the spiritual path before us is emphasized.
        """.trimIndent()
        
        else -> """
            As we continue through the sacred verses of the Bhagavad Gita, each shloka reveals deeper layers of spiritual wisdom. Lord Krishna's teachings encompass the four great paths of yoga:
            
            1. Karma Yoga - The path of selfless action
            2. Bhakti Yoga - The path of devotion and love
            3. Raja Yoga - The path of meditation and self-control
            4. Jnana Yoga - The path of knowledge and wisdom
            
            Through Arjuna's questions and Krishna's divine responses, we learn how to navigate the complexities of life while remaining centered in our true spiritual nature.
        """.trimIndent()
    }
}

fun getRamayanaContent(page: Int): String {
    return """
        श्रीरामायण - ${if (page <= 20) "बाल काण्ड" else if (page <= 40) "अयोध्या काण्ड" else "अरण्य काण्ड"}
        
        The life of Sri Rama represents the ideal of dharmic living. His journey from prince to exile to king teaches us about duty, righteousness, and the triumph of good over evil.
        
        In this section, we witness how Rama faces every challenge with equanimity, never deviating from the path of righteousness even when it requires great personal sacrifice. His devotion to truth, his respect for his parents, and his love for all beings make him the perfect role model for spiritual seekers.
        
        "रघुकुल रीति सदा चली आई, प्राण जाएं पर वचन न जाई"
        
        This verse reminds us that in the lineage of Raghu (Rama's family), the tradition has always been that life may go, but one's word should never be broken. This teaches us the supreme importance of truth and integrity in spiritual life.
    """.trimIndent()
}

fun getMeditationContent(page: Int): String {
    return """
        Chapter ${(page + 9) / 10}: The Practice of Mindful Awareness
        
        Meditation is the gateway to inner peace and self-realization. As you sit in quiet contemplation, remember that the goal is not to stop thoughts, but to observe them with detached awareness.
        
        Basic Meditation Steps:
        
        1. Find a comfortable, stable sitting position
        2. Keep your spine straight but not rigid
        3. Close your eyes gently or maintain a soft gaze
        4. Begin by watching the natural flow of breath
        5. When the mind wanders, gently return to the breath
        6. Practice with patience and compassion for yourself
        
        Remember, every moment of awareness is a victory. The mind that notices it has wandered is already awake. This is the beginning of wisdom.
        
        "ध्यानमूलं गुरोर्मूर्तिः" - The root of meditation is the form of the Guru - the inner teacher that guides us to truth.
    """.trimIndent()
}

fun getPhilosophyContent(page: Int): String {
    return """
        Philosophical Insights - Chapter ${(page + 4) / 5}
        
        The ancient sages have given us profound insights into the nature of reality and our place in the cosmos. Through their wisdom, we understand that we are not merely physical beings having a spiritual experience, but spiritual beings having a human experience.
        
        Key Philosophical Principles:
        
        • Advaita (Non-duality): The ultimate reality is one, beyond all distinctions
        • Dharma: The cosmic law that maintains order and righteousness
        • Karma: The law of action and consequence that governs our evolution
        • Moksha: Liberation from the cycle of birth and death
        
        "सर्वं खल्विदं ब्रह्म" - All this is indeed Brahman
        
        This mahavakya (great statement) from the Upanishads reminds us that the divine consciousness pervades everything. When we truly understand this, we see the sacred in the ordinary and find peace in all circumstances.
    """.trimIndent()
}

fun getMantraContent(page: Int): String {
    return """
        Sacred Mantras - Collection ${(page + 9) / 10}
        
        Mantras are sacred sounds that have the power to transform consciousness. When chanted with devotion and proper understanding, they purify the mind and connect us with divine energy.
        
        Featured Mantra:
        
        ॐ नमः शिवाय
        Om Namah Shivaya
        
        Meaning: "I bow to Shiva" - the consciousness that lies within each of us
        
        Benefits:
        • Purifies negative emotions
        • Brings inner peace and clarity
        • Connects us with our higher self
        • Provides protection and strength
        
        How to Practice:
        1. Sit comfortably with spine straight
        2. Begin with Om (108 times if possible)
        3. Chant with feeling and devotion
        4. Feel the vibration in your heart center
        5. End with a few moments of silence
        
        "मनत्रायते इति मंत्रः" - That which protects the mind is called mantra.
    """.trimIndent()
}

fun getStoryContent(page: Int): String {
    return """
        Spiritual Story ${page}: The Wise Elephant
        
        Once upon a time, in a peaceful village, there lived a young boy named Arjun who was always asking questions about life and its meaning. One day, he met an old wise elephant near the temple.
        
        "Dear elephant," said the boy, "why do you always look so peaceful and happy?"
        
        The elephant smiled and replied, "Little one, I have learned the secret of contentment. I eat when I am hungry, rest when I am tired, and help others whenever I can. I don't worry about tomorrow or regret yesterday."
        
        "But how do you remember to be so wise?" asked Arjun.
        
        "Every morning, I visit the temple and bow to the divine. This reminds me that I am not just a body, but a soul on a journey. When we remember our true nature, peace naturally follows."
        
        From that day, Arjun began each morning with gratitude and remembrance of the divine, and he too found the peace that comes from living in harmony with spiritual principles.
        
        Moral: True happiness comes from remembering our spiritual nature and living in alignment with dharma.
    """.trimIndent()
}

fun getGenericScriptureContent(title: String, page: Int): String {
    return """
        $title - Page $page
        
        The sacred texts are repositories of eternal wisdom, passed down through generations of realized souls. Each verse contains layers of meaning that reveal themselves as our understanding deepens.
        
        "गुरुर्ब्रह्मा गुरुर्विष्णुर्गुरुर्देवो महेश्वरः।
        गुरुरेव परं ब्रह्म तस्मै श्री गुरवे नमः॥"
        
        The Guru is Brahma, the Guru is Vishnu, the Guru is Shiva.
        The Guru is indeed the Supreme Brahman. Salutations to that Guru.
        
        This verse reminds us that spiritual knowledge comes through the grace of the teacher, whether external or internal. The true guru awakens us to our own divine nature.
    """.trimIndent()
}

fun getGenericSpiritualContent(title: String, page: Int): String {
    return """
        $title - Chapter ${(page + 9) / 10}
        
        On the spiritual path, every experience becomes a teacher, every moment an opportunity for growth. The ancient wisdom traditions remind us that the ultimate goal of life is not material success, but spiritual realization.
        
        Key Principles for Spiritual Living:
        
        1. Ahimsa (Non-violence) - in thought, word, and deed
        2. Satya (Truthfulness) - being honest with ourselves and others
        3. Asteya (Non-stealing) - not taking what doesn't belong to us
        4. Brahmacharya (Celibacy/Moderation) - conserving vital energy
        5. Aparigraha (Non-possessiveness) - freedom from excessive desires
        
        "योगः कर्मसु कौशलम्" - Yoga is skill in action
        
        This teaching from the Bhagavad Gita reminds us that spiritual practice is not separate from daily life, but the art of living consciously in every moment.
    """.trimIndent()
} 