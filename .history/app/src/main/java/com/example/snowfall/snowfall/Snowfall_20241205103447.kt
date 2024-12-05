package com.example.snowfall.snowfall

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Snowfall(
    modifier: Modifier = Modifier,
    snowflakeCount: Int = 200
) {
    val density = LocalDensity.current
    val snowflakes = remember { mutableStateListOf<Snowflake>() }
    var size by remember { mutableStateOf(Offset.Zero) }
    val snowflakePath = remember { SnowflakePath.create() }

    LaunchedEffect(Unit) {
        launch {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    snowflakes.forEachIndexed { index, snowflake ->
                        snowflake.position = Offset(
                            x = snowflake.position.x + snowflake.wind,
                            y = snowflake.position.y + snowflake.speed
                        )

                        if (snowflake.position.y > size.y + 20f) {
                            snowflakes[index] = Snowflake.create(size.x, size.y)
                        }
                        if (snowflake.position.x < -20f || snowflake.position.x > size.x + 20f) {
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
        
        if (snowflakes.isEmpty()) {
            repeat(snowflakeCount) {
                snowflakes.add(Snowflake.create(this.size.width, this.size.height))
            }
        }

        snowflakes.forEach { snowflake ->
            translate(snowflake.position.x, snowflake.position.y) {
                rotate(45f) {
                    scale(snowflake.size / 20f) {
                        drawPath(
                            path = snowflakePath,
                            color = Color.White,
                            alpha = 0.9f
                        )
                    }
                }
            }
        }
    }
} 