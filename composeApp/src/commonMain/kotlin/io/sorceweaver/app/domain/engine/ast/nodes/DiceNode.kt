package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.DiceTracker
import io.sorceweaver.app.domain.models.EvaluationResult
import kotlin.random.Random

class DiceNode(
    private val left: Node,
    private val right: Node
) : Node {
    override fun evaluate(context: Map<String, Double>): EvaluationResult {

        val leftResult = left.evaluate(context)
        val rightResult = right.evaluate(context)

        val rawNumberOfDice = leftResult.total
        val rawNumberOfSides = rightResult.total

        require(rawNumberOfDice >= 1 && rawNumberOfSides >= 1){
            "Invalid number, all values must be positive: $rawNumberOfDice, $rawNumberOfSides"
        }
        require(rawNumberOfDice % 1 == 0.0 && rawNumberOfSides % 1 == 0.0){
            "Invalid number, all values must be integers: $rawNumberOfDice, $rawNumberOfSides"
        }

        val numberOfDice = rawNumberOfDice.toInt()
        val numberOfSides = rawNumberOfSides.toInt()

        var newTotal = 0.0

        val finalHistory = buildList{
            addAll(leftResult.history)
            addAll(rightResult.history)

            repeat(numberOfDice){
                val rollResult = Random.nextInt(1,numberOfSides + 1)
                newTotal += rollResult.toDouble()
                add(DiceTracker(numberOfSides, rollResult, true))
            }
        }
        return EvaluationResult(
            newTotal,
            finalHistory
        )
    }
}