package io.sorceweaver.app.engine


object DiceRoller {
    fun roll(expression: String): Int {

        val cleanExpr = expression.replace("\\s".toRegex(), "").lowercase()
        val regex = """^(\d*)d(\d+)([+-]\d+)?$""".toRegex()

        val match = regex.matchEntire(cleanExpr)
            ?: throw IllegalArgumentException("invalid expression: $expression")

        val (diceStr, sidesStr, modStr) = match.destructured
        val dice = if (diceStr.isEmpty()) 1 else diceStr.toInt()
        val sides = sidesStr.toInt()
        val modifier = if (modStr.isEmpty()) 0 else modStr.toInt()

        var total = 0
        for (i in 1..dice) {
            val rollResult = (1..sides).random()
            total += rollResult
        }
        return total + modifier
    }
}