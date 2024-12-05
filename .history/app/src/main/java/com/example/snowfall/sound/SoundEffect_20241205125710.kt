package com.example.snowfall.sound

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.snowfall.R

class SoundEffect(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playExplosion() {
        try {
            // Create and play a new MediaPlayer instance each time
            mediaPlayer = MediaPlayer.create(context, R.raw.explosion)
            mediaPlayer?.apply {
                setOnCompletionListener {
                    release()
                }
                start()
            }
        } catch (e: Exception) {
            Log.e("SoundEffect", "Error playing explosion sound", e)
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
} 