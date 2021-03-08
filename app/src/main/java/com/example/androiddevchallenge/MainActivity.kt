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
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.*
import kotlinx.coroutines.*
import java.lang.IllegalStateException
import java.time.Duration
import kotlin.math.max
import kotlin.math.min

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

@Composable
fun MyApp() {
    var started by remember { mutableStateOf(false) }

    var countdownValue by remember { mutableStateOf(Duration.ofMinutes(10L)) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (countdownView, button) = createRefs()

            CountdownView(
                started = started,
                onCountdownValueChange = {
                    countdownValue = it
                },
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(countdownView) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onEnded = { started = !started }
            )

            Button(
                onClick = { started = !started },
                enabled = !countdownValue.isZero,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
                modifier = Modifier
                    .background(color = Color.Green, shape = RoundedCornerShape(50))
                    .constrainAs(button) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, 16.dp)
                    }
            ) {
                Text(text = if (started) "Stop" else "Start")
            }

        }
    }

}

@Composable
fun CountdownView(
    onEnded: () -> Unit,
    onCountdownValueChange: (Duration) -> Unit,
    modifier: Modifier = Modifier,
    started: Boolean = false,
) {
    "Hoz2 triggered".hozLog()

    var animatableCounter by
    remember { mutableStateOf(Duration.ofMinutes(10L)) }

    "Hoz2 triggered animatableCounter = $animatableCounter".hozLog()

    ConstraintLayout(
        modifier = modifier
    ) {

        val (display, picker) = createRefs()

        LaunchedEffect(started) {
            if (started) {
                do {
                    animatableCounter = animatableCounter.minusSeconds(1L)
                    onCountdownValueChange(animatableCounter)
                    if (animatableCounter.isZero || animatableCounter.isNegative) {
                        onEnded()
                    }
                    delay(1000L)
                } while (started)
            }
        }

        if (started) {
            CountdownDisplay(
                currentValue = animatableCounter.toMillis(),
                modifier = Modifier.constrainAs(display) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
        } else {
            CountdownPicker(
                countdownValue = animatableCounter,
                onCountdownValueChange = object : CountdownPickerCallback {
                    override fun onHourDecadeChanged(hourDecade: Int) {
                        val currentDecade = (animatableCounter.toHours() / 10) * 10
                        animatableCounter = animatableCounter.minusHours(currentDecade).plusHours(hourDecade * 10L)
                        onCountdownValueChange(animatableCounter)
                    }

                    override fun onHourUnitChanged(hourUnit: Int) {
                        val currentUnit = animatableCounter.toHours().rem(10)
                        animatableCounter = animatableCounter.minusHours(currentUnit).plusHours(hourUnit.toLong())
                        onCountdownValueChange(animatableCounter)
                    }

                    override fun onMinuteDecadeChanged(minuteDecade: Int) {
                        val hours = animatableCounter.toHours()
                        val minutes = animatableCounter.minusHours(hours).toMinutes()
                        val minutesDecade = (minutes / 10) * 10
                        animatableCounter = animatableCounter.minusMinutes(minutesDecade).plusMinutes(minuteDecade * 10L)
                        onCountdownValueChange(animatableCounter)
                    }

                    override fun onMinuteUnitChanged(minuteUnit: Int) {
                        val hours = animatableCounter.toHours()
                        val currentUnit = animatableCounter.minusHours(hours).toMinutes().rem(10)
                        animatableCounter = animatableCounter.minusMinutes(currentUnit).plusMinutes(minuteUnit.toLong())
                        onCountdownValueChange(animatableCounter)
                    }

                    override fun onSecondDecadeChanged(secondDecade: Int) {
                        val hours = animatableCounter.toHours()
                        val minutes = animatableCounter.minusHours(hours).toMinutes()
                        val seconds = animatableCounter.minusHours(hours).minusMinutes(minutes).seconds
                        val secondsDecade = (seconds / 10) * 10
                        animatableCounter = animatableCounter.minusSeconds(secondsDecade).plusSeconds(secondDecade * 10L)
                        onCountdownValueChange(animatableCounter)
                    }

                    override fun onSecondUnitChanged(secondUnit: Int) {
                        val hours = animatableCounter.toHours()
                        val minutes = animatableCounter.minusHours(hours).toMinutes()
                        val seconds = animatableCounter.minusHours(hours).minusMinutes(minutes).seconds
                        val secondsUnit = seconds.rem(10)
                        animatableCounter = animatableCounter.minusSeconds(secondsUnit).plusSeconds(secondUnit.toLong())
                        onCountdownValueChange(animatableCounter)
                    }

                },
                modifier = Modifier.constrainAs(picker) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}

interface CountdownPickerCallback {
    fun onHourDecadeChanged(hourDecade: Int)
    fun onHourUnitChanged(hourUnit: Int)
    fun onMinuteDecadeChanged(minuteDecade: Int)
    fun onMinuteUnitChanged(minuteUnit: Int)
    fun onSecondDecadeChanged(secondDecade: Int)
    fun onSecondUnitChanged(secondUnit: Int)
}

@Composable
fun CountdownPicker(
    countdownValue: Duration,
    onCountdownValueChange: CountdownPickerCallback,
    modifier: Modifier = Modifier
) {
    val millis = countdownValue.toMillis()

    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60) % 60)
    val hours = (millis / (1000 * 60 * 60) % 24)

    val hourDecade = min(hours.toInt() / 10, 9)
    val minuteDecade = minutes.toInt() / 10
    val secondDecade = seconds.toInt() / 10

    ConstraintLayout(
        modifier = modifier
    ) {

        val (hourDecadeRef, hourUnitRef, hourColumn, minutesDecadeRef, minuteUnitRef, minuteColumn, secondDecadeRef, secondUnitRef, selectedLineRef) = createRefs()

        val cellHeightDp = 20.dp
        NumberPicker(
            selectableRange = 0..9,
            selectedNumber = hourDecade,
            onNumberSelect = { oldValue, newValue ->
                onCountdownValueChange.onHourDecadeChanged(
                    newValue
                )
            },
            cellHeightDp = cellHeightDp,
            modifier = Modifier.constrainAs(hourDecadeRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(hourUnitRef.start)
            }
        )
        NumberPicker(
            selectableRange = 0..9,
            selectedNumber = hours.rem(10).toInt(),
            onNumberSelect = { oldValue, newValue ->
                onCountdownValueChange.onHourUnitChanged(
                    newValue
                )
            },
            modifier = Modifier.constrainAs(hourUnitRef) {
                start.linkTo(hourDecadeRef.end)
                bottom.linkTo(hourDecadeRef.bottom)
                end.linkTo(hourColumn.start)
            }
        )

        Text(
            fontSize = 26.sp,
            text = ":",
            modifier = Modifier.constrainAs(hourColumn) {
                top.linkTo(hourUnitRef.top)
                bottom.linkTo(hourUnitRef.bottom, 6.dp)
                start.linkTo(hourUnitRef.end, 8.dp)
                end.linkTo(minutesDecadeRef.start, 8.dp)
            }
        )

        NumberPicker(
            selectableRange = 0..5,
            selectedNumber = minuteDecade,
            onNumberSelect = { oldValue, newValue ->
                onCountdownValueChange.onMinuteDecadeChanged(
                    newValue
                )
            },
            modifier = Modifier.constrainAs(minutesDecadeRef) {
                top.linkTo(parent.top, (cellHeightDp.value * 4).dp)
                start.linkTo(hourColumn.end)
                end.linkTo(minuteUnitRef.start)
            }
        )
        NumberPicker(
            selectableRange = 0..9,
            selectedNumber = minutes.rem(10).toInt(),
            onNumberSelect = { oldValue, newValue ->
                onCountdownValueChange.onMinuteUnitChanged(
                    newValue
                )
            },
            modifier = Modifier.constrainAs(minuteUnitRef) {
                start.linkTo(minutesDecadeRef.end)
                top.linkTo(parent.top)
                end.linkTo(minuteColumn.start)
            }
        )

        Text(
            fontSize = 26.sp,
            text = ":",
            modifier = Modifier.constrainAs(minuteColumn) {
                top.linkTo(minuteUnitRef.top)
                bottom.linkTo(minuteUnitRef.bottom, 6.dp)
                start.linkTo(minuteUnitRef.end, 8.dp)
                end.linkTo(secondDecadeRef.start, 8.dp)
            }
        )
        NumberPicker(
            selectableRange = 0..5,
            selectedNumber = secondDecade,
            onNumberSelect = { oldValue, newValue ->
                onCountdownValueChange.onSecondDecadeChanged(
                    newValue
                )
            },
            modifier = Modifier.constrainAs(secondDecadeRef) {
                top.linkTo(parent.top, (cellHeightDp.value * 4).dp)
                start.linkTo(minuteColumn.end)
                end.linkTo(secondUnitRef.start)
            }
        )
        NumberPicker(
            selectableRange = 0..9,
            selectedNumber = seconds.rem(10).toInt(),
            onNumberSelect = { oldValue, newValue ->
                onCountdownValueChange.onSecondUnitChanged(
                    newValue
                )
            },
            modifier = Modifier.constrainAs(secondUnitRef) {
                start.linkTo(secondDecadeRef.end)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        )

        Box(
            modifier = Modifier
                .requiredWidth((cellHeightDp.value * 8 + 8).dp)
                .requiredHeight(cellHeightDp)
                .border(border = BorderStroke(2.dp, Color.Black))
                .constrainAs(selectedLineRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
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
            .requiredHeight(50.dp)
            .fillMaxWidth()
    ) {

        val (hourDecadeRef, hourUnit, hourColumn, minuteDecadeRef, minuteUnit, minuteColumn, secondDecadeRef, secondUnit) = createRefs()

        Number(
            animatedNumber = hourDecadeValue.value,
            transition = hourDecadeTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(hourDecadeRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(hourUnit.start, 4.dp)
                }
        )

        Number(
            animatedNumber = hourUnitValue.value,
            transition = hourUnitTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(hourUnit) {
                    top.linkTo(parent.top)
                    start.linkTo(hourDecadeRef.end)
                    end.linkTo(hourColumn.start)
                }
        )

        Text(
            fontSize = 20.sp,
            text = ":",
            modifier = Modifier.constrainAs(hourColumn) {
                top.linkTo(hourUnit.top)
                bottom.linkTo(hourUnit.bottom)
                start.linkTo(hourUnit.end, 8.dp)
                end.linkTo(minuteDecadeRef.start, 8.dp)
            }
        )

        Number(
            animatedNumber = minuteDecadeValue.value,
            transition = minuteDecadeTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(minuteDecadeRef) {
                    top.linkTo(parent.top)
                    start.linkTo(hourColumn.end)
                    end.linkTo(minuteUnit.start, 4.dp)
                }
        )

        Number(
            animatedNumber = minuteUnitValue.value,
            transition = minuteUnitTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(minuteUnit) {
                    top.linkTo(parent.top)
                    start.linkTo(minuteDecadeRef.end)
                    end.linkTo(minuteColumn.start)
                }
        )

        Text(
            fontSize = 20.sp,
            text = ":",
            modifier = Modifier.constrainAs(minuteColumn) {
                top.linkTo(hourUnit.top)
                bottom.linkTo(hourUnit.bottom)
                start.linkTo(minuteUnit.end, 8.dp)
                end.linkTo(secondDecadeRef.start, 8.dp)
            }
        )

        Number(
            animatedNumber = secondDecadeValue.value,
            transition = secondDecadeTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(secondDecadeRef) {
                    top.linkTo(parent.top)
                    start.linkTo(minuteColumn.end)
                    end.linkTo(secondUnit.start, 4.dp)
                }
        )

        Number(
            animatedNumber = secondUnitValue.value,
            transition = secondUnitTransition,
            modifier = Modifier
                .requiredSize(50.dp)
                .constrainAs(secondUnit) {
                    top.linkTo(parent.top)
                    start.linkTo(secondDecadeRef.end)
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


fun notifySelectedItemChanged(
    offset: Float,
    cellNumber: Int,
    cellHeight: Float,
    previousSelectedItem: Int,
    onNumberSelect: (Int, Int) -> Unit
): Int {
    val selectedItem =
        (offset / cellHeight)
    if (previousSelectedItem != selectedItem.toInt()) {
        onNumberSelect(previousSelectedItem, selectedItem.toInt())
    }
    return selectedItem.toInt()
}

@Composable
fun NumberPicker(
    selectableRange: IntRange,
    selectedNumber: Int,
    onNumberSelect: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    cellHeightDp: Dp = 20.dp
) {
    "Hoz1 retriggered".hozLog()
    if (selectedNumber !in selectableRange) throw IllegalStateException("parameter selectableNumber should be in provided range")
    val cellHeight = cellHeightDp.value * LocalDensity.current.density
    val cellNumber = (selectableRange.last - selectableRange.first) + 1
    val offset = remember { Animatable(cellHeight * selectedNumber) }
    Column(
        modifier = modifier
            .requiredHeight((cellHeightDp.value * ((cellNumber * 2) - 1)).dp)
            .pointerInput(Unit) {
                coroutineScope {
                    var previousSelectedItem: Int = selectedNumber
                    while (true) {
                        val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                        offset.stop()

                        awaitPointerEventScope {

                            verticalDrag(pointerId) { change ->
                                launch {
                                    val newYOffset = offset.value + change.positionChange().y
                                    val correctedY =
                                        min(
                                            max(newYOffset, 0f),
                                            size.height.toFloat() - cellHeight * cellNumber
                                        )
                                    offset.snapTo(correctedY)
                                    previousSelectedItem = notifySelectedItemChanged(
                                        offset.targetValue,
                                        cellNumber,
                                        cellHeight,
                                        previousSelectedItem,
                                        onNumberSelect
                                    )
                                }
                            }
                        }
                        val rem = offset.value.rem(cellHeight)
                        val correction =
                            offset.value - if (rem <= cellHeight / 2) rem else (rem - cellHeight)
                        offset.animateTo(correction)
                        previousSelectedItem = notifySelectedItemChanged(
                            offset.targetValue,
                            cellNumber,
                            cellHeight,
                            previousSelectedItem,
                            onNumberSelect
                        )
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(0, offset.value.toInt())
                }
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.Blue, shape = RoundedCornerShape(CornerSize(50)))
            ) {
                val selectedCell =
                    (offset.value / cellHeight).toInt()
                selectableRange.reversed().forEachIndexed { index, i ->
                    NumberPickerCell(
                        text = "$i",
                        cellHeight = cellHeightDp,
                        isSelected = selectedCell == i
                    )
                }
            }
        }
    }
}

@Composable
fun NumberPickerCell(
    text: String,
    cellHeight: Dp,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .requiredSize(cellHeight),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White)
    }
}

fun String.hozLog() {
    Log.d("Hoz", this)
}

