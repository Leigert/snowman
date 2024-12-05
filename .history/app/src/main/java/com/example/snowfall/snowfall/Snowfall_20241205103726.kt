package com.example.snowfall.snowfall

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.snowfall.R
import kotlinx.coroutines.launch

@Composable
fun Snowfall(
    modifier: Modifier = Modifier,
    snowflakeCount: Int = 150
) {
    val density = LocalDensity.current
    val snowflakes = remember { mutableStateListOf<Snowflake>() }
    var size by remember { mutableStateOf(Offset.Zero) }
    val snowflakeImage = painterResource(id = R.drawable.snowflake)

    LaunchedEffect(Unit) {
        launch {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    snowflakes.forEachIndexed { index, snowflake ->
                        snowflake.position = Offset(
                            x = snowflake.position.x + snowflake.wind,
                            y = snowflake.position.y + snowflake.speed
                        )

                        // Reset snowflake if it goes off screen
                        if (snowflake.position.y > size.y + 20f) {
                            // Reset to random x position at top
                            snowflakes[index] = Snowflake.create(size.x, size.y)
                        }
                        // Reset if snowflake goes too far left/right
                        if (snowflake.position.x < -20f || snowflake.position.x > size.x + 20f) {
                            // Reset to random position within screen bounds
                            snowflakes[index] = Snowflake.create(size.x, size.y)
                        }
                    }
                }
            }
        }
    }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        size = Offset(this.size.width, this.size.height)
        
        // Initialize snowflakes if empty
        if (snowflakes.isEmpty()) {
            repeat(snowflakeCount) {
                // Distribute initial positions across the entire screen
                snowflakes.add(
                    Snowflake(
                        position = Offset(
                            x = (Math.random() * this.size.width).toFloat(),
                            y = (Math.random() * this.size.height).toFloat()
                        ),
                        size = (Math.random() * 8f + 2f).toFloat(),
                        speed = (Math.random() * 4f + 2f).toFloat(),
                        wind = (Math.random() * 2f - 1f).toFloat()
                    )
                )
            }
        }

        snowflakes.forEach { snowflake ->
            translate(
                left = snowflake.position.x - (12 * (snowflake.size / 4f)),  // Center the snowflake
                top = snowflake.position.y - (12 * (snowflake.size / 4f))
            ) {
                rotate(45f) {
                    scale(snowflake.size / 4f) {  // Adjusted scale factor
                        with(snowflakeImage) {
                            draw(
                                size = this.intrinsicSize,
                                alpha = 0.8f
                            )
                        }
                    }
                }
            }
        }
    }
} 