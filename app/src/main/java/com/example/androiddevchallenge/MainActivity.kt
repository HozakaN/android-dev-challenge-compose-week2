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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.AnimatedNumbers
import com.example.androiddevchallenge.ui.theme.AnimatedNumber
import kotlinx.coroutines.*
import java.time.Duration
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = false) {
                MyApp()
//                Gesture()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar {
                Text(text = "Blabla")
            }
        }
    ) {
//        Gesture()
        CountdownView(
            countdownValue = Duration.ofMinutes(10L),
            modifier = Modifier.fillMaxSize()
        )
    }


//    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
////    Surface(color = MaterialTheme.colors.background) {
////        Text(text = "Ready... Set... GO!")
////    }
//    ConstraintLayout(
//        modifier = Modifier
//            .fillMaxSize()
//            .pointerInput(Unit) {
//                coroutineScope {
//                    while (true) {
//                        // Detect a tap event and obtain its position.
//                        val position = awaitPointerEventScope {
//                            awaitFirstDown().position
//                        }
//                        launch {
//                            // Animate to the tap position.
//                            "animating".hozLog()
//                            offset.animateTo(position)
//                        }
//                    }
//                }
//            }
//    ) {
//
//        val (startButton, swipeLayout, myBox) = createRefs()
//
//        Box(modifier = Modifier
//            .constrainAs(myBox) {
//                top.linkTo(parent.top, 16.dp)
//                start.linkTo(parent.start, 16.dp)
//            }
//            .background(color = Color.Blue)
//            .requiredSize(100.dp)
//            .offset { offset.value.toIntOffset() }
//            /*.padding(start = 16.dp, top = 16.dp)*//*.test()*/)
//
//        CountdownView(
//            modifier = Modifier
//                .constrainAs(startButton) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    bottom.linkTo(parent.bottom)
//                }/*.swipeToDismiss()*/
//        )
//
//        Image(
//            painter = painterResource(id = R.drawable.ic_android_black_24dp),
//            contentDescription = "",
//            modifier = Modifier
//                .requiredSize(100.dp)
//                .constrainAs(swipeLayout) {
//                    top.linkTo(parent.top, 16.dp)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                }
////                .swipeToDismiss()
//        )
//    }
}

@Composable
fun CountdownView(
    countdownValue: Duration,
    modifier: Modifier = Modifier
) {
    var started by remember { mutableStateOf(false) }

    "Recomposing all".hozLog()

    val animatableCounter = remember { Animatable(countdownValue.toMillis().toFloat()) }
    LaunchedEffect(started) {
        if (started) {
            do {
                animatableCounter.animateTo(animatableCounter.value - 1000L)
                delay(1000L)
            } while (started)
        }
    }

    ConstraintLayout(modifier = modifier) {

        "Recomposing CountdownDisplay layout".hozLog()
        val (button, display) = createRefs()

        CountdownDisplay(
            currentValue = animatableCounter.value.toLong(),
            modifier = Modifier.constrainAs(display) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(button.top, 16.dp)
            }
        )
        CountdownButton(
            started = started,
            onCountdownButtonClick = { started = !started },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun CountdownDisplay(
    currentValue: Long,
    modifier: Modifier = Modifier
) {

    val duration = Duration.ofMillis(currentValue)
    val hours = duration.toHours()
    val minutes = duration.minusHours(hours).toMinutes()
    val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds

    val hourDecade = min(hours.toInt() / 10, 9)
    val hourDecadeValue = rememberUpdatedState(newValue = findAppropriateAnimatedNumber(hourDecade))
    val hourUnitValue =
        rememberUpdatedState(newValue = findAppropriateAnimatedNumber(hours.rem(10).toInt()))

    val minuteDecade = minutes.toInt() / 10
    val minuteDecadeValue =
        rememberUpdatedState(newValue = findAppropriateAnimatedNumber(minuteDecade))
    val minuteUnitValue =
        rememberUpdatedState(newValue = findAppropriateAnimatedNumber(minutes.rem(10).toInt()))

    val secondDecade = seconds.toInt() / 10
    val secondDecadeValue =
        rememberUpdatedState(newValue = findAppropriateAnimatedNumber(secondDecade))
    val secondUnitValue =
        rememberUpdatedState(newValue = findAppropriateAnimatedNumber(seconds.rem(10).toInt()))

    val hourDecadeTransition = updateTransition(targetState = hourDecadeValue.value)
    val hourUnitTransition = updateTransition(targetState = hourUnitValue.value)

    val minuteDecadeTransition = updateTransition(targetState = minuteDecadeValue.value)
    val minuteUnitTransition = updateTransition(targetState = minuteUnitValue.value)

    val secondDecadeTransition = updateTransition(targetState = secondDecadeValue.value)
    val secondUnitTransition = updateTransition(targetState = secondUnitValue.value)

    ConstraintLayout(
        modifier = modifier
            .requiredHeight(100.dp)
            .fillMaxWidth()
    ) {

        val (hourDecade, hourUnit, minuteDecade, minuteUnit, secondDecade, secondUnit) = createRefs()

        Number(
            animatedNumber = hourDecadeValue.value,
            transition = hourDecadeTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(hourDecade) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(hourUnit.start, 8.dp)
                }
        )

        Number(
            animatedNumber = hourUnitValue.value,
            transition = hourUnitTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(hourUnit) {
                    top.linkTo(parent.top)
                    start.linkTo(hourDecade.end)
                    end.linkTo(minuteDecade.start, 8.dp)
                }
        )

        Number(
            animatedNumber = minuteDecadeValue.value,
            transition = minuteDecadeTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(minuteDecade) {
                    top.linkTo(parent.top)
                    start.linkTo(hourUnit.end)
                    end.linkTo(minuteUnit.start, 8.dp)
                }
        )

        Number(
            animatedNumber = minuteUnitValue.value,
            transition = minuteUnitTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(minuteUnit) {
                    top.linkTo(parent.top)
                    start.linkTo(minuteDecade.end)
                    end.linkTo(secondDecade.start, 8.dp)
                }
        )

        Number(
            animatedNumber = secondDecadeValue.value,
            transition = secondDecadeTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(secondDecade) {
                    top.linkTo(parent.top)
                    start.linkTo(minuteUnit.end)
                    end.linkTo(secondUnit.start, 8.dp)
                }
        )

        Number(
            animatedNumber = secondUnitValue.value,
            transition = secondUnitTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(secondUnit) {
                    top.linkTo(parent.top)
                    start.linkTo(secondDecade.end)
                    end.linkTo(parent.end, 8.dp)
                }
        )

    }
}

fun findAppropriateAnimatedNumber(digit: Int): AnimatedNumber {
    return when (digit) {
        0 -> {
            AnimatedNumbers.Zero
        }
        1 -> {
            AnimatedNumbers.One
        }
        2 -> {
            AnimatedNumbers.Two
        }
        3 -> {
            AnimatedNumbers.Three
        }
        4 -> {
            AnimatedNumbers.Four
        }
        5 -> {
            AnimatedNumbers.Five
        }
        6 -> {
            AnimatedNumbers.Six
        }
        7 -> {
            AnimatedNumbers.Seven
        }
        8 -> {
            AnimatedNumbers.Eight
        }
        9 -> {
            AnimatedNumbers.Nine
        }
        else -> AnimatedNumbers.Zero
    }
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
fun DrawOne(modifier: Modifier = Modifier) {
//    DrawNumber(
//        pathNumber = Numbers.Nine,
//        modifier = modifier
//    )
}

@Composable
fun Number(
    animatedNumber: AnimatedNumber,
    transition: Transition<AnimatedNumber>,
    modifier: Modifier = Modifier
) {
    Number(
        moveCommand = animatedNumber.moveCommand,
        cubicCommand1 = animatedNumber.cubicCommand1,
        cubicCommand2 = animatedNumber.cubicCommand2,
        cubicCommand3 = animatedNumber.cubicCommand3,
        cubicCommand4 = animatedNumber.cubicCommand4,
        transition = transition,
        modifier = modifier
    )
}

@Composable
fun Number(
    moveCommand: Pair<Float, Float>,
    cubicCommand1: List<Float>,
    cubicCommand2: List<Float>,
    cubicCommand3: List<Float>,
    cubicCommand4: List<Float>,
    transition: Transition<AnimatedNumber>,
    modifier: Modifier = Modifier
) {

    val moveCommandX by transition.animateFloat {
        moveCommand.first
    }
    val moveCommandY by transition.animateFloat {
        moveCommand.second
    }
    val cubicCommand11X by transition.animateFloat {
        cubicCommand1[0]
    }
    val cubicCommand11Y by transition.animateFloat {
        cubicCommand1[1]
    }
    val cubicCommand12X by transition.animateFloat {
        cubicCommand1[2]
    }
    val cubicCommand12Y by transition.animateFloat {
        cubicCommand1[3]
    }
    val cubicCommand13X by transition.animateFloat {
        cubicCommand1[4]
    }
    val cubicCommand13Y by transition.animateFloat {
        cubicCommand1[5]
    }
    val cubicCommand21X by transition.animateFloat {
        cubicCommand2[0]
    }
    val cubicCommand21Y by transition.animateFloat {
        cubicCommand2[1]
    }
    val cubicCommand22X by transition.animateFloat {
        cubicCommand2[2]
    }
    val cubicCommand22Y by transition.animateFloat {
        cubicCommand2[3]
    }
    val cubicCommand23X by transition.animateFloat {
        cubicCommand2[4]
    }
    val cubicCommand23Y by transition.animateFloat {
        cubicCommand2[5]
    }
    val cubicCommand31X by transition.animateFloat {
        cubicCommand3[0]
    }
    val cubicCommand31Y by transition.animateFloat {
        cubicCommand3[1]
    }
    val cubicCommand32X by transition.animateFloat {
        cubicCommand3[2]
    }
    val cubicCommand32Y by transition.animateFloat {
        cubicCommand3[3]
    }
    val cubicCommand33X by transition.animateFloat {
        cubicCommand3[4]
    }
    val cubicCommand33Y by transition.animateFloat {
        cubicCommand3[5]
    }
    val cubicCommand41X by transition.animateFloat {
        cubicCommand4[0]
    }
    val cubicCommand41Y by transition.animateFloat {
        cubicCommand4[1]
    }
    val cubicCommand42X by transition.animateFloat {
        cubicCommand4[2]
    }
    val cubicCommand42Y by transition.animateFloat {
        cubicCommand4[3]
    }
    val cubicCommand43X by transition.animateFloat {
        cubicCommand4[4]
    }
    val cubicCommand43Y by transition.animateFloat {
        cubicCommand4[5]
    }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val path = Path().apply {
            moveTo(moveCommandX * canvasWidth, moveCommandY * canvasWidth)
            cubicTo(
                cubicCommand11X * canvasWidth,
                cubicCommand11Y * canvasWidth,
                cubicCommand12X * canvasWidth,
                cubicCommand12Y * canvasWidth,
                cubicCommand13X * canvasWidth,
                cubicCommand13Y * canvasWidth
            )
            cubicTo(
                cubicCommand21X * canvasWidth,
                cubicCommand21Y * canvasWidth,
                cubicCommand22X * canvasWidth,
                cubicCommand22Y * canvasWidth,
                cubicCommand23X * canvasWidth,
                cubicCommand23Y * canvasWidth
            )
            cubicTo(
                cubicCommand31X * canvasWidth,
                cubicCommand31Y * canvasWidth,
                cubicCommand32X * canvasWidth,
                cubicCommand32Y * canvasWidth,
                cubicCommand33X * canvasWidth,
                cubicCommand33Y * canvasWidth
            )
            cubicTo(
                cubicCommand41X * canvasWidth,
                cubicCommand41Y * canvasWidth,
                cubicCommand42X * canvasWidth,
                cubicCommand42Y * canvasWidth,
                cubicCommand43X * canvasWidth,
                cubicCommand43Y * canvasWidth
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
//                        "current offset : ${offset.value.x}, ${offset.value.y}".hozLog()
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

fun String.hozLog() {
    Log.d("Hoz", this)
}
