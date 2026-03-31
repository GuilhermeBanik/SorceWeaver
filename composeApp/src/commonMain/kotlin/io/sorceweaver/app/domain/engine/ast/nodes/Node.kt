package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.EvaluationResult

interface Node {
    fun evaluate(context: Map<String, Double> = emptyMap()): EvaluationResult
}