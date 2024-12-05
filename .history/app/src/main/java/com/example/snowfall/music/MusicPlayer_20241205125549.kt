package com.example.snowfall.music

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.snowfall.R

class MusicPlayer(context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    init {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.song)
            mediaPlayer?.apply {
                isLooping = true
                setVolume(1f, 1f)
            }
            Log.d("MusicPlayer", "MediaPlayer initialized successfully")
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error initializing MediaPlayer", e)
        }
    }

    fun start() {
        try {
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                    Log.d("MusicPlayer", "Music started")
                }
            }
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error starting MediaPlayer", e)
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                    player.prepare()
                    Log.d("MusicPlayer", "Music stopped")
                }
            }
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error stopping MediaPlayer", e)
        }
    }

    fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            Log.d("MusicPlayer", "MediaPlayer released")
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error releasing MediaPlayer", e)
        }
    }
} 