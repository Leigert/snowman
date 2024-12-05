package com.example.snowfall.music

import android.content.Context
import android.media.MediaPlayer
import com.example.snowfall.R

class MusicPlayer(context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer.create(context, R.raw.song)
        mediaPlayer?.isLooping = true
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.prepare()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
} 