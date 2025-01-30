package com.niket.helloworld

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import com.niket.helloworld.ui.theme.HelloWorldTheme
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloWorldTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        BMWLogoAnimation(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BMWLogoAnimation(
    modifier: Modifier,
    logoRadius: Float = 100f,
    animationDuration: Int = 5000,
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Step 1: Small Circle Expanding
    val circleRadius by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = logoRadius,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Step 2: Move Clockwise with Glowing Border
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f * 10f, // 10 Rounds
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val sweepAngleTransition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val bounceEffectTransition by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val textFontSize by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 42f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing, delayMillis = 3500),
            repeatMode = RepeatMode.Reverse
        )
    )

    var scaleFactor by remember { mutableFloatStateOf(1f) }

    val animatedScaleFactor by animateFloatAsState(
        targetValue = scaleFactor,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    LaunchedEffect(true) {
        // Update the scale factor based on some logic (e.g., progress over time)
        scaleFactor = 1 + 0.5f * sin(rotation)
    }

    BMWLogoCanvas(
        modifier,
        logoRadius,
        rotationAnimateValue = rotation,
        animatedLogoRadius = circleRadius,
        animatedScaleValue = animatedScaleFactor,
        animatedSweepAngle = sweepAngleTransition,
        animatedBounceValue = bounceEffectTransition,
        animatedTextFontSize = textFontSize
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BMWLogoCanvas(
    modifier: Modifier = Modifier,
    staticLogoRadius: Float,
    rotationAnimateValue: Float,
    bmwBlueColor: Color = Color(0xFF4599FE),
    animatedLogoRadius: Float,
    animatedScaleValue: Float,
    animatedSweepAngle: Float,
    animatedBounceValue: Float,
    animatedTextFontSize: Float,
) {
    val ccTypeFace = ResourcesCompat.getFont(
        LocalContext.current, R.font.bmw_helvetica_bold,
    )

    Canvas(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val newRadius = animatedLogoRadius * animatedScaleValue
        val newSize = Size(newRadius * 2, newRadius * 2)

        rotate(rotationAnimateValue, pivot = center) {
            drawArc(
                color = bmwBlueColor,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = true,
                topLeft = center - androidx.compose.ui.geometry.Offset(
                    animatedLogoRadius,
                    animatedLogoRadius
                ),
                size = newSize
            )

            drawArc(
                color = Color.White,
                startAngle = 90f,
                sweepAngle = 90f,
                useCenter = true,
                topLeft = center - androidx.compose.ui.geometry.Offset(
                    animatedLogoRadius,
                    animatedLogoRadius
                ),
                size = newSize
            )

            drawArc(
                color = bmwBlueColor,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = true,
                topLeft = center - androidx.compose.ui.geometry.Offset(
                    animatedLogoRadius,
                    animatedLogoRadius
                ),
                size = newSize
            )

            drawArc(
                color = Color.White,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = true,
                topLeft = center - androidx.compose.ui.geometry.Offset(
                    animatedLogoRadius,
                    animatedLogoRadius
                ),
                size = newSize
            )
        }

        // Draw outer black border with text
        drawCircle(
            color = Color.Black,
            radius = animatedLogoRadius + 4 + 20 + 4,
            center = center,
            style = Stroke(width = 48f)
        )

        drawCircle(
            color = Color.Black,
            radius = animatedLogoRadius + 4 + 48 + 4,
            center = center,
            style = Stroke(width = 1f)
        )

        val pathRadius = staticLogoRadius + 12

        val logoNameTextPath = android.graphics.Path().apply {
            addArc(
                center.x - pathRadius,
                center.y - pathRadius,
                center.x + pathRadius,
                center.y + pathRadius,
                -135f,
                animatedSweepAngle
            )
        }


        drawContext.canvas.nativeCanvas.drawTextOnPath(
            "B M W",
            logoNameTextPath,
            0f,
            animatedBounceValue,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = animatedTextFontSize
                textAlign = android.graphics.Paint.Align.CENTER
                letterSpacing = 0.5f
                typeface = ccTypeFace
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
private fun BMWLogoPreview() {
    BMWLogoAnimation(
        modifier = Modifier
    )
}