package io.sorceweaver.app.domain.engine.ast.nodes

import io.sorceweaver.app.domain.models.EvaluationResult

class VarNode(
    private val name: String
) : Node {
    override fun evaluate(context: Map<String, Double>): EvaluationResult {
        val value = context[name] ?: throw IllegalArgumentException("Variable '$name' not found")
        return EvaluationResult(value, emptyList())
    }
}