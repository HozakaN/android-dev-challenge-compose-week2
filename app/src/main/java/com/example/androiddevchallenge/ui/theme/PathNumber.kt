package com.example.androiddevchallenge.ui.theme

data class PathNumber(
    val moveCommand: Pair<Float, Float>,
    val cubicCommand1: List<Float>,
    val cubicCommand2: List<Float>,
    val cubicCommand3: List<Float>,
    val cubicCommand4: List<Float>
) {

    operator fun inc(): PathNumber = when(this) {
        Numbers.Zero -> {
            Numbers.One
        }
        Numbers.One -> {
            Numbers.Two
        }
        Numbers.Two -> {
            Numbers.Three
        }
        Numbers.Three -> {
            Numbers.Four
        }
        Numbers.Four -> {
            Numbers.Five
        }
        Numbers.Five -> {
            Numbers.Six
        }
        Numbers.Six -> {
            Numbers.Seven
        }
        Numbers.Seven -> {
            Numbers.Eight
        }
        Numbers.Eight -> {
            Numbers.Nine
        }
        Numbers.Nine -> {
            Numbers.Zero
        }
        else -> Numbers.Zero
    }

}