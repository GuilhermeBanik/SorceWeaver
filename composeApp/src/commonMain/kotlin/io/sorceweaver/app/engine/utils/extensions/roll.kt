package io.sorceweaver.app.engine.utils.extensions

fun Double.roll(faces: Int): Double{
    var total = 0.0
    for (i in 1..this.toInt()){
        val rollResult = (1..faces).random()
        total += rollResult.toDouble()
    }
    return total
}