package io.sorceweaver.app.engine.math

class ExpressionCalculator {

    companion object{
        val regex = listOf<Regex>(
            Regex("\\s"),
            Regex("(?<=^|[+*/^d()\\-])d"),
            Regex("(?<=^|\\()-"),
            Regex("(?<=[+*/^d])-"),
            Regex("""~?\d+(\.\d+)?|[+*/^d()\-]""")
        )
    }
    fun calculate(expression: String): Double {
        var cleanStr = expression.replace(regex[0], "")
        cleanStr = cleanStr.replace(regex[1], "1d")
        cleanStr = cleanStr.replace(regex[2], "0-")
        cleanStr = cleanStr.replace(regex[3], "~")
        val tokens = regex[4].findAll(cleanStr).map { it.value }.toList()
        val values = mutableListOf<Double>()
        val ops = mutableListOf<String>()

        fun applyTopOperator() {
            if (ops.isEmpty()) return
            val opToken = ops.removeLast()
            val operator = Operator.fromString(opToken)
                ?: throw IllegalArgumentException("Operador inválido: $opToken")
            if (values.size < 2) {
                throw IllegalArgumentException("Erro de sintaxe: Faltam números para o operador '$opToken'")
            }
            val right = values.removeLast()
            val left = values.removeLast()
            values.add(operator.operate(right, left))
        }

        for (token in tokens) {
            when {
                token.startsWith("~") || token[0].isDigit() -> {
                    val baseNumber = token.replace("~", "").toDouble()
                    val finalNumber = if (token.startsWith("~")) baseNumber.unaryMinus() else baseNumber
                    values.add(finalNumber)
                }
                token == "(" -> {
                    ops.add(token)
                }
                token == ")" -> {
                    while (ops.isNotEmpty() && ops.last() != "(") applyTopOperator()
                    if (ops.isEmpty() || ops.last() != "(") {
                        throw IllegalArgumentException("Erro de sintaxe: Parênteses ')' sem correspondente aberto.")
                    }
                    ops.removeLast()
                }
                else -> {
                    val currentOp = Operator.fromString(token)
                        ?: throw IllegalArgumentException("Símbolo desconhecido: $token")
                    while (ops.isNotEmpty() && ops.last() != "(") {
                        val topOp = Operator.fromString(ops.last())!!
                        val isLessPrecedent = currentOp < topOp
                        val isSamePrecedentLeftAssoc = (currentOp.compareTo(topOp) == 0) && !currentOp.rightAssociativity
                        if (isLessPrecedent || isSamePrecedentLeftAssoc) applyTopOperator()
                        else break
                    }
                    ops.add(token)
                }
            }
        }

        while (ops.isNotEmpty()) {

            if (ops.last() == "(") {
                throw IllegalArgumentException("Erro de sintaxe: Parênteses '(' aberto e não fechado.")
            }
            applyTopOperator()
        }

        if (values.size != 1) {
            throw IllegalArgumentException("Erro de sintaxe: Expressão malformada.")
        }

        return values.removeLast()
    }
}

fun String.toResult(): Double{
    val calculator = ExpressionCalculator()
    return calculator.calculate(this)
}