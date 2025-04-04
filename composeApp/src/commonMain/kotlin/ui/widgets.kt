package ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import geminikmp.composeapp.generated.resources.Res
import geminikmp.composeapp.generated.resources.assistant
import org.jetbrains.compose.resources.painterResource
import ui.screens.gemini_ai.AiMessage


@Composable
fun TextIcon(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    text: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    vAlignment: Alignment.Vertical = Alignment.Top,
    hArrangement: Arrangement.Horizontal
) {
    Row(modifier, horizontalArrangement = hArrangement, verticalAlignment = vAlignment) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(8.dp))
        }
        text()
        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            trailingIcon()
        }
    }
}


@Composable
fun RotatingIcon(
    painterIcon: Painter,
    modifier: Modifier = Modifier,
    sizeDpSize: Dp = 64.dp,
    durationMillis: Int = 2000 // Duration of one full rotation
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Rotation")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ), // Smooth linear rotation
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation Animation"
    )

    Icon(
        painter = painterIcon,
        contentDescription = "Rotating Icon",
        modifier = modifier
            .graphicsLayer(rotationZ = rotationAngle) // Apply rotation
            .size(sizeDpSize) // Icon size
    )
}


@Composable
fun ChatBubble(modifier: Modifier = Modifier, aiMessage: AiMessage) {
    val align = if (aiMessage.aiModel.lowercase() == "user") Arrangement.End else Arrangement.Start
    Row(
        modifier = modifier,
        horizontalArrangement = align
    ) {
        TextIcon(
            Modifier.fillMaxWidth(0.7f),
            leadingIcon = {
                if (aiMessage.aiModel.lowercase() == "model") {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(Res.drawable.assistant),
                        contentDescription = "assistant image"
                    )
                }
            },
            text = {
                Markdown(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(8.dp),
                    content = aiMessage.message
                )
            },
            hArrangement = align
        )
    }
}