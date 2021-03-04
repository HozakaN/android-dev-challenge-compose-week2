/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.Numbers
import com.example.androiddevchallenge.ui.theme.PathNumber
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = true) {
//                MyApp()
                Gesture()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
//    Surface(color = MaterialTheme.colors.background) {
//        Text(text = "Ready... Set... GO!")
//    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        // Detect a tap event and obtain its position.
                        val position = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        launch {
                            // Animate to the tap position.
                            "animating".hozLog()
                            offset.animateTo(position)
                        }
                    }
                }
            }
    ) {

        val (startButton, swipeLayout, myBox) = createRefs()

        Box(modifier = Modifier
            .constrainAs(myBox) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
            }
            .background(color = Color.Blue)
            .requiredSize(100.dp)
            .offset { offset.value.toIntOffset() }
            /*.padding(start = 16.dp, top = 16.dp)*//*.test()*/)

        CountdownView(
            modifier = Modifier
                .constrainAs(startButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }/*.swipeToDismiss()*/
        )

        Image(
            painter = painterResource(id = R.drawable.ic_android_black_24dp),
            contentDescription = "",
            modifier = Modifier
                .requiredSize(100.dp)
                .constrainAs(swipeLayout) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
//                .swipeToDismiss()
        )
    }
}

@Composable
fun CountdownView(
    modifier: Modifier = Modifier
) {
//    var eventPresentationType by remember { mutableStateOf(false) }
//    var started by remember { mutableStateOf(false) }
//    var truc by remember(calculation = {
//        mutableStateOf(false)
//    })
//    var value by remember { mutableStateOf<Boolean>(false) }
    val (started, setStarted) = remember { mutableStateOf(false) }

    CountdownButton(
        started = started,
        onCountdownButtonClick = { setStarted(!started) },
        modifier = modifier
    )
}

@Composable
fun CountdownDisplay(
    currentValue: Long,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    onCountdownEdit: (Long) -> Unit = {}
) {

}

@Composable
fun CountdownButton(
    started: Boolean,
    onCountdownButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onCountdownButtonClick() },
        modifier = modifier
    ) {
        Text(text = if (started) "Stop" else "Start")
    }
}

@Composable
fun NumberVerticalPager(
    selectedValue: Int,
    onValueSelected: (Int) -> Unit
) {

}
//
//@Preview("Light Theme", widthDp = 360, heightDp = 640)
//@Composable
//fun LightPreview() {
//    MyTheme {
//        MyApp()
//    }
//}
//
//@Preview("Dark Theme", widthDp = 360, heightDp = 640)
//@Composable
//fun DarkPreview() {
//    MyTheme(darkTheme = true) {
//        MyApp()
//    }
//}

@Preview
@Composable
fun PreviewDrawOne() {
    Box(modifier = Modifier.fillMaxSize()) {
        DrawOne(modifier = Modifier.requiredSize(100.dp))
    }
}

@Composable
fun TestComposable(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Green)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawLine(
                start = Offset(x = canvasWidth, y = 0f),
                end = Offset(x = 0f, y = canvasHeight),
                color = Color.Blue
            )
        }
    }
}

@Composable
fun DrawOne(modifier: Modifier = Modifier) {
    DrawNumber(
        pathNumber = Numbers.Nine,
        modifier = modifier
    )
//    Canvas(modifier = modifier) {
//        val canvasWidth = size.width
//        val path = Path().apply {
//            moveTo(0.425414364640884f * canvasWidth, 0.113259668508287f * canvasWidth)
//            cubicTo(
//                0.425414364640884f * canvasWidth,
//                0.113259668508287f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                0.113259668508287f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                0.113259668508287f * canvasWidth
//            )
//            cubicTo(
//                0.577348066298343f * canvasWidth,
//                0.113259668508287f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth
//            )
//            cubicTo(
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth
//            )
//            cubicTo(
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth,
//                0.577348066298343f * canvasWidth,
//                1f * canvasWidth
//            )
//        }
//        drawPath(path = path, color = Color.Black, style = Stroke(width = 3.dp.value * density))
//    }
}

@Composable
fun DrawNumber(pathNumber: PathNumber, modifier: Modifier = Modifier) {
    DrawNumber(
        moveCommand = pathNumber.moveCommand,
        cubicCommand1 = pathNumber.cubicCommand1,
        cubicCommand2 = pathNumber.cubicCommand2,
        cubicCommand3 = pathNumber.cubicCommand3,
        cubicCommand4 = pathNumber.cubicCommand4,
        modifier = modifier
    )
}

@Composable
fun DrawNumber(
    moveCommand: Pair<Float, Float>,
    cubicCommand1: List<Float>,
    cubicCommand2: List<Float>,
    cubicCommand3: List<Float>,
    cubicCommand4: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val path = Path().apply {
            moveTo(moveCommand.first * canvasWidth, moveCommand.second * canvasWidth)
            cubicTo(
                cubicCommand1[0] * canvasWidth,
                cubicCommand1[1] * canvasWidth,
                cubicCommand1[2] * canvasWidth,
                cubicCommand1[3] * canvasWidth,
                cubicCommand1[4] * canvasWidth,
                cubicCommand1[5] * canvasWidth
            )
            cubicTo(
                cubicCommand2[0] * canvasWidth,
                cubicCommand2[1] * canvasWidth,
                cubicCommand2[2] * canvasWidth,
                cubicCommand2[3] * canvasWidth,
                cubicCommand2[4] * canvasWidth,
                cubicCommand2[5] * canvasWidth
            )
            cubicTo(
                cubicCommand3[0] * canvasWidth,
                cubicCommand3[1] * canvasWidth,
                cubicCommand3[2] * canvasWidth,
                cubicCommand3[3] * canvasWidth,
                cubicCommand3[4] * canvasWidth,
                cubicCommand3[5] * canvasWidth
            )
            cubicTo(
                cubicCommand4[0] * canvasWidth,
                cubicCommand4[1] * canvasWidth,
                cubicCommand4[2] * canvasWidth,
                cubicCommand4[3] * canvasWidth,
                cubicCommand4[4] * canvasWidth,
                cubicCommand4[5] * canvasWidth
            )
        }
        drawPath(path = path, color = Color.Black, style = Stroke(width = 3.dp.value * density))
    }
}

@Composable
fun Gesture() {
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                val circleSize = 100.dp.value * density
//                val decay = splineBasedDecay<Float>(this)
                val offsetDecay = splineBasedDecay<Offset>(this)
                coroutineScope {
                    while (true) {
                        // Detect a tap event and obtain its position.
//                        val position = awaitPointerEventScope {
//                            awaitFirstDown().position
//                        }
                        val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                        val velocityTracker = VelocityTracker()
                        offset.stop()

                        awaitPointerEventScope {
                            verticalDrag(pointerId) { change ->
                                launch {
                                    val newXOffset = offset.value.x + change.positionChange().x
                                    val newYOffset = offset.value.y + change.positionChange().y
                                    "newXOffset = $newXOffset, (${size.width.toFloat()})".hozLog()
                                    val correctedOffset = Offset(
                                        min(
                                            max(newXOffset, 0f),
                                            size.width.toFloat() - circleSize
                                        ),
                                        min(max(newYOffset, 0f), size.height.toFloat() - circleSize)
                                    )
                                    offset.snapTo(correctedOffset)
                                }
                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    change.position
                                )
                            }
                        }
                        // No longer receiving touch events. Prepare the animation.
                        val velocity = velocityTracker.calculateVelocity()
//                        offsetDecay.calculateTargetValue(
//                            typeConverter = TwoWayConverter({ offset ->
//                                AnimationVector(offset.x, offset.y)
//                            }, {
//                                Offset(it.v1, it.v2)
//                            }),
//                            initialValue = offset.value,
//                            initialVelocity = Offset(velocity.x, velocity.y)
//                        )
//                        val targetOffsetX = decay.calculateTargetValue(
//                            offset.value.x,
//                            velocity.x
//                        )
//                        val targetOffsetY = decay.calculateTargetValue(
//                            offset.value.y,
//                            velocity.y
//                        )
                        // The animation stops when it reaches the bounds.
                        "current screen size : ${size.width}, ${size.height}".hozLog()
                        "current offset : ${offset.value.x}, ${offset.value.y}".hozLog()
                        offset.updateBounds(
                            lowerBound = Offset(0f, 0f),
                            upperBound = Offset(
                                size.width.toFloat() - circleSize,
                                size.height.toFloat() - circleSize
                            )
                        )
                        launch {
//                            val newX = if (targetOffsetX.absoluteValue <)
//                            if (targetOffsetX.absoluteValue <= size.width) {
//                                 Not enough velocity; Slide back.
//                                offset.animateTo(
//                                    targetValue = 0f,
//                                    initialVelocity = velocity
//                                )
//                            } else {
                            // The element was swiped away.
                            offset.animateDecay(Offset(velocity.x, velocity.y), offsetDecay)
//                            offset.animateDecay()
//                            offset.animateDecay(velocity, decay)
//                                onDismissed()
//                            }
                        }
//                        offset.updateBounds(
//                            lowerBound = -size.width.toFloat(),
//                            upperBound = size.width.toFloat()
//                        )
//                        launch {
//                            // Animate to the tap position.
//                            offset.animateTo(position)
//                        }
                    }
                }
            }
    ) {
//        TestComposable(
//            modifier = Modifier
//                .requiredSize(100.dp)
//                .offset { offset.value.toIntOffset() }
//        )
        DrawOne(modifier = Modifier
            .background(color = Color.Blue)
            .requiredSize(100.dp)
            .offset { offset.value.toIntOffset() })
//        Circle(modifier = Modifier.offset { offset.value.toIntOffset() })
    }
}

@Composable
fun Circle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = Color.Blue, shape = CircleShape)
            .requiredSize(50.dp)
    )
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

@Composable
fun FunComposable() {

}

fun Modifier.random2(
    onMouvement: (Offset) -> Unit
): Modifier = Modifier.pointerInput(Unit) {

}

fun Modifier.random(

): Modifier = composed {
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    pointerInput(Unit) {

    }.offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
}

fun Modifier.test(): Modifier = composed {
    var offsetY by remember { mutableStateOf(0f) }
    pointerInput(Unit) {
        coroutineScope {
            val pointerId = awaitPointerEventScope { awaitFirstDown().id }
            awaitPointerEventScope {
//                horizontalDrag(pointerId) { change ->
//                    "new horizontal drag".hozLog()
//                }
                verticalDrag(pointerId) { change ->
                    "new touch : ${change.position}, positionChange : (${change.positionChange().x},${change.positionChange().y})".hozLog()
                    // Update the animation value with touch events.
                    launch {
                        offsetY += change.positionChange().y
                    }
                }
//                verticalDrag(pointerId) { change ->
//                    "new touch : ${change.position}, positionChange : (${change.positionChange().x},${change.positionChange().y})".hozLog()
//                    // Update the animation value with touch events.
//                    launch {
//                        offsetY += change.positionChange().y
////                        offsetY.snapTo(
////                            offsetY.value + change.positionChange().x
////                        )
//                    }
//                    velocityTracker.addPosition(
//                        change.uptimeMillis,
//                        change.position
//                    )
            }
        }
    }
        .offset {
            "offsetting to ${offsetY.roundToInt()}".hozLog()
            IntOffset(0, offsetY.roundToInt())
        }
}
//
//@Composable
//fun NumberPicker(
//    state: MutableState<Int>,
//    modifier: Modifier = Modifier,
//    range: IntRange? = null,
//    textStyle: TextStyle = LocalTextStyle.current,
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val numbersColumnHeight = 36.dp
//    val halvedNumbersColumnHeight = numbersColumnHeight / 2
//    val halvedNumbersColumnHeightPx = with(LocalDensity.current) { halvedNumbersColumnHeight.toPx() }
//
//    fun animatedStateValue(offset: Float): Int = state.value - (offset / halvedNumbersColumnHeightPx).toInt()
//
//    val animatedOffset = remember { Animatable(0f) }.apply {
//        if (range != null) {
//            val offsetRange = remember(state.value, range) {
//                val value = state.value
//                val first = -(range.last - value) * halvedNumbersColumnHeightPx
//                val last = -(range.first - value) * halvedNumbersColumnHeightPx
//                first..last
//            }
//            updateBounds(offsetRange.start, offsetRange.endInclusive)
//        }
//    }
//    val coercedAnimatedOffset = animatedOffset.value % halvedNumbersColumnHeightPx
//    val animatedStateValue = animatedStateValue(animatedOffset.value)
//
//    Column(
//        modifier = modifier
//            .wrapContentSize()
//            .draggable(
//                state = rememberDraggableState(onDelta = {
//                    coroutineScope.launch {
//                        animatedOffset.snapTo(animatedOffset.value + it)
//                    }
//                }),
//                orientation = Orientation.Vertical,
//                onDragStopped = { velocity ->
//                    coroutineScope.launch {
//                        val flingConfig = FlingConfig(
//                            decayAnimation = FloatExponentialDecaySpec(
//                                frictionMultiplier = 20f
//                            ),
//                            adjustTarget = { target ->
//                                val coercedTarget = target % halvedNumbersColumnHeightPx
//                                val coercedAnchors = listOf(
//                                    -halvedNumbersColumnHeightPx,
//                                    0f,
//                                    halvedNumbersColumnHeightPx
//                                )
//                                val coercedPoint =
//                                    coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
//                                val base =
//                                    halvedNumbersColumnHeightPx * (target / halvedNumbersColumnHeightPx).toInt()
//                                val adjusted = coercedPoint + base
//                                TargetBasedAnimation(adjusted, SpringSpec())
//                            }
//                        )
//                        val endValue = animatedOffset.fling(
//                            initialVelocity = velocity,
//                            flingConfig = flingConfig,
//                        ).endState.value
//
//                        state.value = animatedStateValue(endValue)
//                        animatedOffset.snapTo(0f)
//                    }
//                }
//            )
//    ) {
//        val spacing = 4.dp
//
//        val arrowColor = MaterialTheme.colors.onSecondary.copy(alpha = ContentAlpha.disabled)
//
////        Arrow(direction = ArrowDirection.UP, tint = arrowColor)
//
//        Spacer(modifier = Modifier.height(spacing))
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .offset(y = coercedAnimatedOffset.dp)
////                .offset(y = { coercedAnimatedOffset.roundToInt() })
//        ) {
//            val baseLabelModifier = Modifier.align(Alignment.Center)
//            ProvideTextStyle(textStyle) {
//                Label(
//                    text = (animatedStateValue - 1).toString(),
//                    modifier = baseLabelModifier
//                        .offset(y = -halvedNumbersColumnHeight)
//                        .alpha(coercedAnimatedOffset / halvedNumbersColumnHeightPx)
//                )
//                Label(
//                    text = animatedStateValue.toString(),
//                    modifier = baseLabelModifier
//                        .alpha(1 - abs(coercedAnimatedOffset) / halvedNumbersColumnHeightPx)
//                )
//                Label(
//                    text = (animatedStateValue + 1).toString(),
//                    modifier = baseLabelModifier
//                        .offset(y = halvedNumbersColumnHeight)
//                        .alpha(-coercedAnimatedOffset / halvedNumbersColumnHeightPx)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(spacing))
//
////        Arrow(direction = ArrowDirection.DOWN, tint = arrowColor)
//    }
//}

//fun Modifier.swipeToDismiss(): Modifier = composed {
//    val offsetX = remember { Animatable(0f) }
//    pointerInput(Unit) {
//        // Used to calculate fling decay.
//        val decay = splineBasedDecay<Float>(this)
//        // Use suspend functions for touch events and the Animatable.
//        coroutineScope {
//            while (true) {
//                // Detect a touch down event.
//                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
//                "pointerId : $pointerId".hozLog()
//                val velocityTracker = VelocityTracker()
//                // Stop any ongoing animation
//                offsetX.stop()
//                awaitPointerEventScope {
//                    horizontalDrag(pointerId) { change ->
//                        "new touch : ${change.position}, positionChange : ${change.positionChange().x}".hozLog()
//                        // Update the animation value with touch events.
//                        launch {
//                            offsetX.snapTo(
//                                offsetX.value + change.positionChange().x
//                            )
//                        }
//                        velocityTracker.addPosition(
//                            change.uptimeMillis,
//                            change.position
//                        )
//                    }
//                }
//                // No longer receiving touch events. Prepare the animation.
//                val velocity = velocityTracker.calculateVelocity().x
//                "new velocity : $velocity".hozLog()
//                val targetOffsetX = decay.calculateTargetValue(
//                    offsetX.value,
//                    velocity
//                )
//                "targetOffsetX : $targetOffsetX"
//                // The animation stops when it reaches the bounds.
//                offsetX.updateBounds(
//                    lowerBound = -size.width.toFloat(),
//                    upperBound = size.width.toFloat()
//                )
//                "right before last coroutine".hozLog()
//                launch {
//                    "last coroutine".hozLog()
//                    if (targetOffsetX.absoluteValue <= size.width) {
//                        // Not enough velocity; Slide back.
//                        "Not enough velocity; Slide back.".hozLog()
//                        offsetX.animateTo(
//                            targetValue = 0f,
//                            initialVelocity = velocity
//                        )
//                    } else {
//                        // The element was swiped away.
//                        offsetX.animateDecay(velocity, decay)
//                        "onDismiss call".hozLog()
//                        onDismissed()
//                    }
//                }
//            }
//        }
//    }.offset {
//        "offsetting to ${offsetX.value.roundToInt()}".hozLog()
//        IntOffset(offsetX.value.roundToInt(), 0)
//    }
//}


fun onDismissed() {

}

@Composable
private fun Label(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // FIXME: Empty to disable text selection
            })
        }
    )

}

//@Preview
//@Composable
//fun PreviewNumberPicker() {
//    Box(modifier = Modifier.fillMaxSize()) {
//        NumberPicker(
//            state = mutableStateOf(9),
//            range = 0..10,
//            modifier = Modifier.align(Alignment.Center)
//        )
//    }
//}

fun String.hozLog() {
    Log.d("Hoz", this)
}
