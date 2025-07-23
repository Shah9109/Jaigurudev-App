# Jai Gurudev - Spiritual Community App 🙏

A comprehensive Android application built with Jetpack Compose for spiritual learning, community engagement, and personal growth. This app provides a complete platform for meditation, learning scriptures, listening to devotional music, participating in satsangs, and connecting with like-minded spiritual seekers.

## ✨ Features

### 🔐 Authentication
- **Firebase Auth Integration**: Email/Password login and registration
- **Beautiful UI**: Gradient backgrounds with smooth animations
- **User Profiles**: Store and manage user information in Firestore

### 🏠 Home Screen
- **Spiritual Dashboard**: Welcome section with personalized greetings
- **Quick Actions**: Fast access to all major sections
- **Important Notices**: Community announcements and updates
- **Upcoming Content**: Featured books, videos, and events
- **Daily Quotes**: Inspirational spiritual wisdom
- **Lottie Animations**: Beautiful meditation animations (placeholder ready)

### 📚 Books Section
- **Digital Library**: Collection of spiritual texts and guides
- **Categories**: Scriptures, Meditation, Devotional, Prayers, Yoga
- **PDF Viewer**: Built-in reader with bookmarking and navigation
- **Sample Content**: Includes Bhagavad Gita verses with translations
- **Download Management**: Offline reading capabilities

### 🎵 Music Section
- **Spiritual Music Library**: Mantras, devotional songs, meditation music
- **ExoPlayer Integration**: Professional audio playback (placeholder ready)
- **Background Playback**: Continue listening while using other features
- **Music Player**: Full-featured player with seek, skip, and repeat
- **Categories**: Mantras, Devotional, Meditation, Prayers, Instrumental
- **Playlist Management**: Create and manage personal playlists

### 🎬 Videos Section
- **YouTube-style Interface**: Grid layout with thumbnails
- **Spiritual Content**: Meditation sessions, scripture studies, discourses
- **Video Player**: Custom player with ExoPlayer integration
- **Live Streaming**: Support for live satsang broadcasts
- **Categories**: Meditation, Scriptures, Devotional, Satsang, Yoga

### 💬 Chat & Community
- **Group Chats**: Join spiritual discussion groups
- **Real-time Messaging**: Firebase Firestore powered chat
- **Community Groups**: Morning Meditation, Evening Satsang, Study Circles
- **Message Bubbles**: Beautiful UI with timestamps
- **Direct Messages**: One-on-one conversations (placeholder ready)

### 🎯 Meetings & Satsangs
- **Jitsi Integration**: Video conferencing for spiritual gatherings
- **Meeting Scheduler**: View upcoming and past sessions
- **Live Sessions**: Join ongoing satsangs and meditations
- **Meeting Codes**: Quick join with room codes
- **Calendar Integration**: Add events to device calendar

### 👤 Profile Management
- **User Dashboard**: Personal spiritual journey tracking
- **Statistics**: Session count, streak days, favorite categories
- **Settings**: Notifications, downloads, privacy controls
- **Spiritual Level**: Track progress and achievements
- **Account Management**: Edit profile, logout functionality

### 📍 Events & Gatherings
- **Event Discovery**: Find local and online spiritual events
- **Event Details**: Complete information with registration
- **Category Filters**: Satsang, Mantra, Retreat, Study sessions
- **Registration**: Join events with attendee management
- **Location Support**: Both online and physical venue events

## 🛠️ Technical Architecture

### Frontend
- **Jetpack Compose**: Modern declarative UI framework
- **Material Design 3**: Latest design system with beautiful theming
- **Navigation Compose**: Type-safe navigation between screens
- **Smooth Animations**: Custom animations and transitions
- **Responsive Design**: Optimized for different screen sizes

### Backend & Services
- **Firebase Authentication**: Secure user management
- **Cloud Firestore**: Real-time database for chat and user data
- **Firebase Storage**: File storage for books, music, and videos
- **ExoPlayer**: Professional media playback (integration ready)
- **Jitsi Meet SDK**: Video conferencing capabilities
- **Background Services**: Music playback with notification controls

### Key Dependencies
```gradle
// Jetpack Compose
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.material3:material3"
implementation "androidx.navigation:navigation-compose"

// Firebase
implementation "com.google.firebase:firebase-auth-ktx"
implementation "com.google.firebase:firebase-firestore-ktx"
implementation "com.google.firebase:firebase-storage-ktx"

// Media & Communication
implementation "com.google.android.exoplayer:exoplayer-core"
implementation "org.jitsi.react:jitsi-meet-sdk"

// Animations & UI
implementation "com.airbnb.android:lottie-compose"
implementation "io.coil-kt:coil-compose"
```

## 🚀 Setup Instructions

### 1. Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK API 24 or higher
- Kotlin 1.9.10 or later

### 2. Firebase Configuration
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app with package name: `com.example.jaigurudevapp`
3. Download `google-services.json` and replace the sample file
4. Enable Authentication with Email/Password
5. Create Firestore database
6. Set up Firebase Storage

### 3. Jitsi Meet Setup
1. The app uses Jitsi Meet SDK for video conferencing
2. No additional setup required for basic functionality
3. For custom domain, update the configuration in meeting screens

### 4. ExoPlayer Integration
The app is prepared for ExoPlayer integration:
- Dependencies are included
- Service classes are ready
- UI components support media controls
- Replace placeholders in `MusicPlayerScreen` and `MusicService`

### 5. Lottie Animations
- Download spiritual/meditation animations from [LottieFiles](https://lottiefiles.com)
- Place them in `app/src/main/assets/`
- Update the animation components in relevant screens

## 📱 App Structure

```
app/
├── src/main/java/com/example/jaigurudevapp/
│   ├── MainActivity.kt                 # Main entry point
│   ├── navigation/
│   │   └── JaigurudevNavigation.kt    # Navigation setup
│   ├── screens/                       # All screen composables
│   │   ├── LoginScreen.kt
│   │   ├── SignupScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── BooksScreen.kt
│   │   ├── VideosScreen.kt
│   │   ├── MusicScreen.kt
│   │   ├── MusicPlayerScreen.kt
│   │   ├── ChatScreen.kt
│   │   ├── MeetingsScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── EventsScreen.kt
│   │   ├── PDFViewerScreen.kt
│   │   └── VideoPlayerScreen.kt
│   ├── service/
│   │   └── MusicService.kt           # Background music service
│   └── ui/theme/                     # App theming
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── google-services.json              # Firebase configuration
```

## 🎨 Design Features

### Color Scheme
- **Primary**: Saffron/Orange (#FF9800) - Traditional spiritual color
- **Secondary**: Green (#4CAF50) - Growth and harmony
- **Accent**: Gold (#FFD700) - Divine illumination
- **Background**: Cream (#FFFBE6) - Peaceful and warm

### Typography
- **Headers**: Bold fonts for spiritual emphasis
- **Body**: Readable fonts optimized for long content
- **Sanskrit/Hindi**: Unicode support for scriptures

### Animations
- **Smooth Transitions**: Between screens and states
- **Gradient Backgrounds**: Dynamic and peaceful
- **Floating Elements**: Cards with shadows and elevation
- **Loading States**: Beautiful progress indicators

## 🔧 Customization

### Adding New Spiritual Content
1. **Books**: Add new PDF files and update the books list
2. **Music**: Include audio files in assets and update playlists
3. **Videos**: Add video URLs or local files
4. **Events**: Create new event types and categories

### Theming
- Modify colors in `ui/theme/Color.kt`
- Update typography in `ui/theme/Type.kt`
- Customize Material 3 theme in `ui/theme/Theme.kt`

### Firebase Integration
- Update Firestore collections for your data structure
- Modify authentication flow as needed
- Add new user fields and preferences

## 📖 Usage Guide

### For Users
1. **Sign Up/Login**: Create account with email and password
2. **Explore Content**: Browse books, videos, and music
3. **Join Community**: Participate in group chats and discussions
4. **Attend Satsangs**: Join live video meetings
5. **Track Progress**: Monitor your spiritual journey

### For Developers
1. **Extend Features**: Add new screens and functionality
2. **Integrate APIs**: Connect with additional spiritual content providers
3. **Customize UI**: Modify themes and animations
4. **Add Languages**: Support multiple languages for global reach

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit a Pull Request

## 📄 License

This project is created for educational and spiritual purposes. Please respect the spiritual content and use it mindfully.

## 🙏 Acknowledgments

- **Spiritual Teachers**: For the wisdom shared in the content
- **Open Source Community**: For the amazing libraries and tools
- **Firebase**: For providing excellent backend services
- **Jetpack Compose**: For making Android UI development delightful

---

**Jai Gurudev** 🕉️

*May this app help bring peace, wisdom, and spiritual growth to all who use it.* # Jaigurudev-App
