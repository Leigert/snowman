package com.example.snowfall

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snowfall.music.MusicPlayer
import com.example.snowfall.sound.SoundEffect
import com.example.snowfall.snowfall.Snowfall
import com.example.snowfall.ui.theme.SnowfallTheme
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity() {
    private var musicPlayer: MusicPlayer? = null
    private var soundEffect: SoundEffect? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Initialize audio
            volumeControlStream = android.media.AudioManager.STREAM_MUSIC
            
            soundEffect = SoundEffect(this)
            musicPlayer = MusicPlayer(this)
            musicPlayer?.start()
            
            Log.d("MainActivity", "Audio components initialized")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing audio", e)
        }
        
        enableEdgeToEdge()
        setContent {
            SnowfallTheme {
                SnowfallScreen(
                    onScore = {
                        Log.d("MainActivity", "Score hit, playing explosion sound")
                        soundEffect?.playExplosion()
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            musicPlayer?.start()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error resuming music", e)
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            musicPlayer?.stop()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error pausing music", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            musicPlayer?.release()
            musicPlayer = null
            soundEffect?.release()
            soundEffect = null
        } catch (e: Exception) {
            Log.e("MainActivity", "Error destroying audio players", e)
        }
    }
}

@Composable
fun SnowfallScreen(
    onScore: () -> Unit = {}
) {
    var score by remember { mutableStateOf(0) }
    var throwCount by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    var isExploding by remember { mutableStateOf(false) }
    var showQuintus by remember { mutableStateOf(false) }
    var showQuintusExplosion by remember { mutableStateOf(false) }
    var animationDuration by remember { mutableStateOf(1500) }
    var maxHeight by remember { mutableStateOf(0f) }
    var showIPhone by remember { mutableStateOf(false) }
    var iPhonePosition by remember { mutableStateOf(0f) }
    
    val quintusScale by animateFloatAsState(
        targetValue = if (showQuintus) 1f else 0f,
        animationSpec = tween(2000),
        finishedListener = {
            if (showQuintus && it == 1f) {
                kotlinx.coroutines.MainScope().launch {
                    delay(1000)
                    showQuintus = false
                    showQuintusExplosion = true
                }
            }
        }
    )

    val explosionAlpha by animateFloatAsState(
        targetValue = if (showQuintusExplosion) 0f else 1f,
        animationSpec = tween(500),
        finishedListener = {
            if (showQuintusExplosion && it == 0f) {
                showQuintusExplosion = false
            }
        }
    )

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

    // Animate iPhone scrolling
    val iPhoneOffset by animateFloatAsState(
        targetValue = iPhonePosition,
        animationSpec = tween(
            durationMillis = 5000,  // 5 seconds to scroll across screen
            easing = LinearEasing
        ),
        finishedListener = {
            if (showIPhone && it <= -1500f) {  // When scrolled off screen
                showIPhone = false
                iPhonePosition = 0f  // Reset position
            }
        }
    )

    // Timer for iPhone appearance
    LaunchedEffect(Unit) {
        while (true) {
            delay(15000) // Wait 15 seconds instead of 120000
            showIPhone = true
            iPhonePosition = -1500f  // Scroll distance
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000, 4000))
            maxHeight = Random.nextFloat() * 1500f + 200f
            animationDuration = Random.nextInt(1200, 2000)
            isAnimating = true
            throwCount++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background landscape
        Image(
            painter = painterResource(id = R.drawable.landscape),
            contentDescription = "Winter landscape",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

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
        
        if (showQuintus || showQuintusExplosion) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        id = if (showQuintusExplosion) R.drawable.explosion else R.drawable.quintus
                    ),
                    contentDescription = "Special effect",
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            scaleX = if (showQuintusExplosion) 1f else quintusScale
                            scaleY = if (showQuintusExplosion) 1f else quintusScale
                            alpha = if (showQuintusExplosion) explosionAlpha else 1f
                        }
                )
            }
        }

        Image(
            painter = painterResource(
                id = when {
                    isExploding -> R.drawable.explosion
                    throwCount % 3 == 0 -> R.drawable.televerket
                    else -> R.drawable.reindeer
                }
            ),
            contentDescription = when {
                isExploding -> "Explosion"
                throwCount % 3 == 0 -> "Televerket"
                else -> "Flying reindeer"
            },
            colorFilter = if (throwCount % 3 == 0) {
                ColorFilter.tint(Color(0xFFFF5722)) // Material Orange color
            } else null,
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
                        onScore()
                        
                        if (score % 2 == 0) {
                            showQuintus = true
                        }
                    }
                }
        )

        // Scrolling iPhone
        if (showIPhone) {
            Image(
                painter = painterResource(id = R.drawable.iphone),
                contentDescription = "Scrolling iPhone",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(200.dp)
                    .graphicsLayer {
                        translationX = iPhoneOffset
                    }
            )
        }
    }
}