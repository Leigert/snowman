package com.example.snowfall.snowfall

import androidx.compose.ui.geometry.Offset
import kotlin.random.Random

data class Snowflake(
    var position: Offset,
    val size: Float,
    var speed: Float,
    var wind: Float = 0f
) {
    companion object {
        fun create(width: Float, height: Float): Snowflake {
            return Snowflake(
                position = Offset(
                    x = Random.nextFloat() * width,
                    y = -20f
                ),
                size = Random.nextFloat() * 8f + 2f,
                speed = Random.nextFloat() * 4f + 2f,
                wind = Random.nextFloat() * 2f - 1f
            )
        }
    }
} 