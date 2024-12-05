package com.example.snowfall.snowfall

import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

object SnowflakePath {
    fun create(): Path {
        return Path().apply {
            val arms = 6
            val innerRadius = 0.3f
            val outerRadius = 1.5f
            
            for (i in 0 until arms) {
                val angle = (i * 360f / arms) * (Math.PI / 180f)
                
                // Main arm
                moveTo(0f, 0f)
                lineTo(
                    (outerRadius * cos(angle)).toFloat(),
                    (outerRadius * sin(angle)).toFloat()
                )
                
                // Branches
                val branchAngle1 = angle - Math.PI / 4
                val branchAngle2 = angle + Math.PI / 4
                
                moveTo(
                    (innerRadius * cos(angle)).toFloat(),
                    (innerRadius * sin(angle)).toFloat()
                )
                lineTo(
                    (innerRadius * 1.5f * cos(branchAngle1)).toFloat(),
                    (innerRadius * 1.5f * sin(branchAngle1)).toFloat()
                )
                
                moveTo(
                    (innerRadius * cos(angle)).toFloat(),
                    (innerRadius * sin(angle)).toFloat()
                )
                lineTo(
                    (innerRadius * 1.5f * cos(branchAngle2)).toFloat(),
                    (innerRadius * 1.5f * sin(branchAngle2)).toFloat()
                )
            }
        }
    }
} 