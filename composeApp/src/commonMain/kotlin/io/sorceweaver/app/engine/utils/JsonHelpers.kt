package io.sorceweaver.app.engine.utils

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull

fun JsonObject.getInt(key: String): Int? {
    return (this[key] as? JsonPrimitive)?.intOrNull
}

fun JsonObject.getString(key: String): String? {
    return (this[key] as? JsonPrimitive)?.contentOrNull
}

fun JsonObject.getBoolean(key: String): Boolean? {
    return (this[key] as? JsonPrimitive)?.booleanOrNull
}