package io.sorceweaver.app.domain.models

data class DiceTracker(
    val sides: Int,
    val result: Int,
    var isKept: Boolean,
)

data class EvaluationResult(
    val total: Double,
    val history: List<DiceTracker>
)

