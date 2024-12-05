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
    val offsetY by animateFloatAsState(
        targetValue = if (isAnimating) -Random.nextInt(300, 600).toFloat() else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isAnimating = false }
    )

    // Random trigger for animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(5000, 15000)) // Random delay between 5-15 seconds
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
        
        // Animated reindeer image
        Image(
            painter = painterResource(id = R.drawable.pngegg),
            contentDescription = "Jumping reindeer",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(150.dp)
                .graphicsLayer {
                    translationY = offsetY
                    // Add a slight rotation for more playful effect
                    rotationZ = if (isAnimating) offsetY * 0.1f else 0f
                }
        )
    }
}