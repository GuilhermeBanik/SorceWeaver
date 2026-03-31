package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.EvaluationResult
import io.sorceweaver.app.domain.models.TokenType
import kotlin.math.pow

class MathNode(
    private val left: Node,
    private val operator: TokenType,
    private val right: Node
) : Node {

    override fun evaluate(context: Map<String, Double>): EvaluationResult {
        val leftResult = left.evaluate(context)
        val rightResult = right.evaluate(context)

        val newTotal = when (operator) {
            TokenType.PLUS -> leftResult.total + rightResult.total
            TokenType.MINUS -> leftResult.total - rightResult.total
            TokenType.MULTIPLY -> leftResult.total * rightResult.total
            TokenType.DIVIDE -> {
                require(rightResult.total != 0.0) {
                    "Division by zero"
                }
                leftResult.total / rightResult.total
            }
            TokenType.POWER -> leftResult.total.pow(rightResult.total)
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }

        val combinedHistory = leftResult.history + rightResult.history

        return EvaluationResult(
            newTotal,
            combinedHistory
        )
    }
}
