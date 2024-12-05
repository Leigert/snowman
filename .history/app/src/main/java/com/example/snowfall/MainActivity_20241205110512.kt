package com.example.snowfall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.snowfall.snowfall.Snowfall
import com.example.snowfall.ui.theme.SnowfallTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnowfallTheme {
                SnowfallScreen()
            }
        }
    }
}

@Composable
fun SnowfallScreen() {
    // Animation states
    var isAnimating by remember { mutableStateOf(false) }
    var animationDuration by remember { mutableStateOf(1500) }
    var maxHeight by remember { mutableStateOf(0f) }
    
    // Create infinite transition for smooth parabolic motion
    val infiniteTransition = rememberInfiniteTransition()
    val progress by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = LinearEasing
        ),
        finishedListener = { isAnimating = false }
    )

    // Calculate parabolic motion
    val offsetY = if (isAnimating) {
        val x = progress * 2 - 1 // Convert progress to -1..1 range
        -(1 - x * x) * maxHeight // Parabola equation: -(1-x²)
    } else 0f

    val offsetX = if (isAnimating) {
        -progress * Random.nextFloat() * 700f // Randomize horizontal distance too
    } else 0f

    // Random trigger for animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000, 4000)) // More frequent throws: 2-4 seconds
            // Randomize height and duration each throw
            maxHeight = Random.nextFloat() * 600f + 100f // More varied height: 100-700
            animationDuration = Random.nextInt(1000, 1800) // Slightly faster animation
            isAnimating = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Snowfall(
            modifier = Modifier.fillMaxSize()
        )
        
        // Stationary snowman
        Image(
            painter = painterResource(id = R.drawable.pngegg),
            contentDescription = "Snowman",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(150.dp)
        )
        
        // Animated reindeer with parabolic motion
        Image(
            painter = painterResource(id = R.drawable.reindeer),
            contentDescription = "Flying reindeer",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 120.dp)
                .size(50.dp)
                .graphicsLayer {
                    translationY = offsetY
                    translationX = offsetX
                    // Rotate based on position in arc
                    rotationZ = if (isAnimating) {
                        val rotationProgress = progress * 360f // One full rotation
                        rotationProgress
                    } else 0f
                }
        )
    }
}