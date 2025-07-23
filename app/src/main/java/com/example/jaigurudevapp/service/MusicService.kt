package com.example.jaigurudevapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.jaigurudevapp.MainActivity
import com.example.jaigurudevapp.R

class MusicService : Service() {
    
    private val binder = MusicBinder()
    private var isPlaying = false
    private var currentSong: String? = null
    
    companion object {
        const val CHANNEL_ID = "MUSIC_CHANNEL"
        const val NOTIFICATION_ID = 1
        
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_STOP = "ACTION_STOP"
    }
    
    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> playMusic()
            ACTION_PAUSE -> pauseMusic()
            ACTION_NEXT -> playNext()
            ACTION_PREVIOUS -> playPrevious()
            ACTION_STOP -> stopMusic()
        }
        return START_STICKY
    }
    
    fun playMusic(songId: String? = null) {
        songId?.let { currentSong = it }
        isPlaying = true
        
        // In real implementation, use ExoPlayer here
        // exoPlayer.prepare()
        // exoPlayer.play()
        
        showNotification()
    }
    
    fun pauseMusic() {
        isPlaying = false
        
        // In real implementation, pause ExoPlayer
        // exoPlayer.pause()
        
        showNotification()
    }
    
    fun stopMusic() {
        isPlaying = false
        currentSong = null
        
        // In real implementation, stop ExoPlayer
        // exoPlayer.stop()
        // exoPlayer.release()
        
        stopForeground(true)
        stopSelf()
    }
    
    private fun playNext() {
        // Implement next song logic
        showNotification()
    }
    
    private fun playPrevious() {
        // Implement previous song logic
        showNotification()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Music playback controls"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val playPauseIntent = Intent(this, MusicService::class.java).apply {
            action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        }
        val playPausePendingIntent = PendingIntent.getService(
            this, 0, playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val previousIntent = Intent(this, MusicService::class.java).apply {
            action = ACTION_PREVIOUS
        }
        val previousPendingIntent = PendingIntent.getService(
            this, 1, previousIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val nextIntent = Intent(this, MusicService::class.java).apply {
            action = ACTION_NEXT
        }
        val nextPendingIntent = PendingIntent.getService(
            this, 2, nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentSong ?: "Spiritual Music")
            .setContentText("Jai Gurudev App")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(
                android.R.drawable.ic_media_previous,
                "Previous",
                previousPendingIntent
            )
            .addAction(
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (isPlaying) "Pause" else "Play",
                playPausePendingIntent
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "Next",
                nextPendingIntent
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isPlaying)
            .build()
        
        if (isPlaying) {
            startForeground(NOTIFICATION_ID, notification)
        } else {
            stopForeground(false)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
    
    fun isPlaying(): Boolean = isPlaying
    fun getCurrentSong(): String? = currentSong
} 