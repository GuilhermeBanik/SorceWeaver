package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.EvaluationResult

class NumberNode(
    private val value: Double
) : Node {
    override fun evaluate(context: Map<String, Double>): EvaluationResult {
        return EvaluationResult(
            value,
            emptyList()
        )

    }
}