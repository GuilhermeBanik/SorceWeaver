package io.sorceweaver.app.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UniversalCharacter(
    val systemId: String,
    val id: String = "",
    val name: String = "",

    val components: List<ComponentNode>
)
@Serializable
data class ComponentNode(
    val id: String,
    val name: String,
    val ui_type: String,
    val category: String,
    val data: JsonObject
)
