package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.EvaluationResult

class DropLowestNode(
    private val targetNode: Node,
    private val amountNode: Node,
) : Node {
    override fun evaluate(context: Map<String, Double>): EvaluationResult {
        val targetResult = targetNode.evaluate(context)
        val amountResult = amountNode.evaluate(context)

        require(amountResult.total >= 1 && amountResult.total % 1 == 0.0){
            "values must be positive integers: ${amountResult.total}"
        }

        val dropAmount = amountResult.total.toInt()
        val originalHistory = targetResult.history

        val indicesToDrop = originalHistory
            .mapIndexed { index, tracker -> index to tracker.result }
            .sortedBy { it.second }
            .take(dropAmount)
            .map { it.first }
            .toSet()

        val modifiedHistory = originalHistory.mapIndexed { index, tracker ->
            tracker.copy(isKept = index !in indicesToDrop)
        }

        val recalculatedTotal = modifiedHistory
            .filter { it.isKept }
            .sumOf { it.result }
            .toDouble()

        return EvaluationResult(
            recalculatedTotal,
            modifiedHistory
        )
    }
}
