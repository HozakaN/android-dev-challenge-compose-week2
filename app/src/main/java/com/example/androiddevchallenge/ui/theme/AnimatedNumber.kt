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
package com.example.androiddevchallenge.ui.theme

data class AnimatedNumber(
    val moveCommand: Pair<Float, Float>,
    val cubicCommand1: List<Float>,
    val cubicCommand2: List<Float>,
    val cubicCommand3: List<Float>,
    val cubicCommand4: List<Float>
) {

    override fun toString(): String {
        return when (this) {
            AnimatedNumbers.Zero -> {
                "0"
            }
            AnimatedNumbers.One -> {
                "1"
            }
            AnimatedNumbers.Two -> {
                "2"
            }
            AnimatedNumbers.Three -> {
                "3"
            }
            AnimatedNumbers.Four -> {
                "4"
            }
            AnimatedNumbers.Five -> {
                "5"
            }
            AnimatedNumbers.Six -> {
                "6"
            }
            AnimatedNumbers.Seven -> {
                "7"
            }
            AnimatedNumbers.Eight -> {
                "8"
            }
            AnimatedNumbers.Nine -> {
                "9"
            }
            else -> "unknown"
        }
    }

    operator fun inc(): AnimatedNumber = when (this) {
        AnimatedNumbers.Zero -> {
            AnimatedNumbers.One
        }
        AnimatedNumbers.One -> {
            AnimatedNumbers.Two
        }
        AnimatedNumbers.Two -> {
            AnimatedNumbers.Three
        }
        AnimatedNumbers.Three -> {
            AnimatedNumbers.Four
        }
        AnimatedNumbers.Four -> {
            AnimatedNumbers.Five
        }
        AnimatedNumbers.Five -> {
            AnimatedNumbers.Six
        }
        AnimatedNumbers.Six -> {
            AnimatedNumbers.Seven
        }
        AnimatedNumbers.Seven -> {
            AnimatedNumbers.Eight
        }
        AnimatedNumbers.Eight -> {
            AnimatedNumbers.Nine
        }
        AnimatedNumbers.Nine -> {
            AnimatedNumbers.Zero
        }
        else -> AnimatedNumbers.Zero
    }
}
