package com.example.snowfall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var score by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    var isExploding by remember { mutableStateOf(false) }
    var animationDuration by remember { mutableStateOf(1500) }
    var maxHeight by remember { mutableStateOf(0f) }
    
    val progress by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = LinearEasing
        ),
        finishedListener = { 
            isAnimating = false
            isExploding = false
        }
    )

    val offsetY = if (isAnimating) {
        val x = progress * 2 - 1
        -(1 - x * x) * maxHeight
    } else 0f

    val offsetX = if (isAnimating) {
        -progress * 500f
    } else 0f

    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000, 4000))
            maxHeight = Random.nextFloat() * 1500f + 200f
            animationDuration = Random.nextInt(1200, 2000)
            isAnimating = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text = "Score: $score",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        )

        Snowfall(
            modifier = Modifier.fillMaxSize()
        )
        
        Image(
            painter = painterResource(id = R.drawable.pngegg),
            contentDescription = "Snowman",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(150.dp)
        )
        
        Image(
            painter = painterResource(
                id = if (isExploding) R.drawable.explosion else R.drawable.reindeer
            ),
            contentDescription = if (isExploding) "Explosion" else "Flying reindeer",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 120.dp)
                .size(50.dp)
                .graphicsLayer {
                    translationY = offsetY
                    translationX = offsetX
                    rotationZ = if (isAnimating && !isExploding) {
                        progress * 720f
                    } else 0f
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (isAnimating && !isExploding) {
                        score++
                        isExploding = true
                    }
                }
        )
    }
}