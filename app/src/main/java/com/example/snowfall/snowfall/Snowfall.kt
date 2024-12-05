package com.example.snowfall.snowfall

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Snowfall(
    modifier: Modifier = Modifier,
    snowflakeCount: Int = 100
) {
    val density = LocalDensity.current
    val snowflakes = remember { mutableStateListOf<Snowflake>() }
    var size by remember { mutableStateOf(Offset.Zero) }

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
            drawCircle(
                color = Color.White,
                radius = with(density) { snowflake.size.dp.toPx() },
                center = snowflake.position,
                alpha = 0.8f
            )
        }
    }
} 