package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.EvaluationResult

class EmptyNode : Node {
    override fun evaluate(context: Map<String, Double>): EvaluationResult {
        return EvaluationResult(
            0.0,
            emptyList()
        )
    }
}