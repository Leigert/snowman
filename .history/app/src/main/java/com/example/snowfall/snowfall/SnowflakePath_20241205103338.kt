package com.example.snowfall.snowfall

import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

object SnowflakePath {
    fun create(): Path {
        return Path().apply {
            // Create a 6-pointed snowflake
            val arms = 6
            val innerRadius = 0.2f
            val outerRadius = 1f
            
            for (i in 0 until arms) {
                val angle = (i * 360f / arms) * (Math.PI / 180f)
                val nextAngle = ((i + 1) * 360f / arms) * (Math.PI / 180f)
                
                // Main arm
                moveTo(0f, 0f)
                lineTo(
                    (outerRadius * cos(angle)).toFloat(),
                    (outerRadius * sin(angle)).toFloat()
                )
                
                // Small branches on each arm
                val branchAngle1 = angle - Math.PI / 6
                val branchAngle2 = angle + Math.PI / 6
                
                moveTo(
                    (innerRadius * cos(angle)).toFloat(),
                    (innerRadius * sin(angle)).toFloat()
                )
                lineTo(
                    (innerRadius * 2 * cos(branchAngle1)).toFloat(),
                    (innerRadius * 2 * sin(branchAngle1)).toFloat()
                )
                
                moveTo(
                    (innerRadius * cos(angle)).toFloat(),
                    (innerRadius * sin(angle)).toFloat()
                )
                lineTo(
                    (innerRadius * 2 * cos(branchAngle2)).toFloat(),
                    (innerRadius * 2 * sin(branchAngle2)).toFloat()
                )
            }
        }
    }
} 