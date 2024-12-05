package com.example.snowfall.sound

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class SoundEffect(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playExplosion() {
        try {
            Log.d("SoundEffect", "Attempting to play explosion sound")
            // Release previous instance if it exists
            mediaPlayer?.release()
            
            // Create new instance
            mediaPlayer = MediaPlayer.create(context, com.example.snowfall.R.raw.explosion)
            if (mediaPlayer == null) {
                Log.e("SoundEffect", "Failed to create MediaPlayer for explosion sound")
                return
            }

            mediaPlayer?.apply {
                setOnCompletionListener { mp ->
                    mp.release()
                    Log.d("SoundEffect", "Explosion sound completed and released")
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("SoundEffect", "MediaPlayer error: what=$what extra=$extra")
                    mp.release()
                    true
                }
                start()
                Log.d("SoundEffect", "Explosion sound started playing")
            }
        } catch (e: Exception) {
            Log.e("SoundEffect", "Error playing explosion sound", e)
        }
    }

    fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            Log.d("SoundEffect", "SoundEffect resources released")
        } catch (e: Exception) {
            Log.e("SoundEffect", "Error releasing sound resources", e)
        }
    }
} 