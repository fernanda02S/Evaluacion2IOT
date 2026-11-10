package com.example.aplicacionmovileva2.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconResumenGrid(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 20.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val stroke = 1.8.dp.toPx()
        val spacing = 3.dp.toPx()
        val boxWidth = (this.size.width - spacing) / 2f
        val boxHeight = (this.size.height - spacing) / 2f
        val r = 2.dp.toPx()

        // Top Left
        drawRoundRect(
            color = tint,
            topLeft = Offset(0f, 0f),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(r, r),
            style = Stroke(stroke)
        )
        // Top Right
        drawRoundRect(
            color = tint,
            topLeft = Offset(boxWidth + spacing, 0f),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(r, r),
            style = Stroke(stroke)
        )
        // Bottom Left
        drawRoundRect(
            color = tint,
            topLeft = Offset(0f, boxHeight + spacing),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(r, r),
            style = Stroke(stroke)
        )
        // Bottom Right
        drawRoundRect(
            color = tint,
            topLeft = Offset(boxWidth + spacing, boxHeight + spacing),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(r, r),
            style = Stroke(stroke)
        )
    }
}

@Composable
fun IconPlanoMap(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 20.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val stroke = 1.8.dp.toPx()
        val w = this.size.width
        val h = this.size.height

        val path = Path().apply {
            moveTo(0.05f * w, 0.2f * h)
            lineTo(0.35f * w, 0.05f * h)
            lineTo(0.65f * w, 0.2f * h)
            lineTo(0.95f * w, 0.05f * h)
            lineTo(0.95f * w, 0.8f * h)
            lineTo(0.65f * w, 0.95f * h)
            lineTo(0.35f * w, 0.8f * h)
            lineTo(0.05f * w, 0.95f * h)
            close()
        }

        drawPath(
            path = path,
            color = tint,
            style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Vertical fold lines
        drawLine(
            color = tint,
            start = Offset(0.35f * w, 0.05f * h),
            end = Offset(0.35f * w, 0.8f * h),
            strokeWidth = stroke
        )
        drawLine(
            color = tint,
            start = Offset(0.65f * w, 0.2f * h),
            end = Offset(0.65f * w, 0.95f * h),
            strokeWidth = stroke
        )
    }
}

@Composable
fun IconPuertasDoor(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 20.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val stroke = 1.8.dp.toPx()
        val w = this.size.width
        val h = this.size.height

        // Outer door frame
        val doorPath = Path().apply {
            moveTo(0.15f * w, 0.95f * h)
            lineTo(0.15f * w, 0.1f * h)
            lineTo(0.85f * w, 0.1f * h)
            lineTo(0.85f * w, 0.95f * h)
        }
        drawPath(
            path = doorPath,
            color = tint,
            style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Bottom floor line
        drawLine(
            color = tint,
            start = Offset(0.05f * w, 0.95f * h),
            end = Offset(0.95f * w, 0.95f * h),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // Doorknob dot
        drawCircle(
            color = tint,
            radius = 1.5.dp.toPx(),
            center = Offset(0.72f * w, 0.55f * h)
        )
    }
}

@Composable
fun IconIncendioFlame(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 20.dp,
    filled: Boolean = false
) {
    Canvas(modifier = modifier.size(size)) {
        val stroke = 1.8.dp.toPx()
        val w = this.size.width
        val h = this.size.height

        val path = Path().apply {
            moveTo(0.5f * w, 0.05f * h)
            cubicTo(
                0.55f * w, 0.25f * h,
                0.85f * w, 0.45f * h,
                0.85f * w, 0.68f * h
            )
            cubicTo(
                0.85f * w, 0.88f * h,
                0.68f * w, 0.98f * h,
                0.5f * w, 0.98f * h
            )
            cubicTo(
                0.32f * w, 0.98f * h,
                0.15f * w, 0.88f * h,
                0.15f * w, 0.68f * h
            )
            cubicTo(
                0.15f * w, 0.48f * h,
                0.35f * w, 0.35f * h,
                0.45f * w, 0.25f * h
            )
            cubicTo(
                0.45f * w, 0.38f * h,
                0.55f * w, 0.5f * h,
                0.58f * w, 0.6f * h
            )
            cubicTo(
                0.62f * w, 0.45f * h,
                0.55f * w, 0.2f * h,
                0.5f * w, 0.05f * h
            )
            close()
        }

        if (filled) {
            drawPath(path = path, color = tint)
        } else {
            drawPath(
                path = path,
                color = tint,
                style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}
