package io.sorceweaver.app.engine.math

import io.sorceweaver.app.engine.utils.extensions.roll
import kotlin.math.pow

sealed class Operator(
    val symbol: String,
    val precedence: Int,
    val rightAssociativity: Boolean
) : Comparable<Operator> {
    companion object{
        fun fromString(representation:String) = when(representation){
            addition.symbol -> addition
            subtraction.symbol -> subtraction
            multiplication.symbol -> multiplication
            division.symbol -> division
            exponentiation.symbol -> exponentiation
            roll.symbol -> roll
            else -> null
        }
    }
    object addition : Operator("+", 1, false)
    object subtraction : Operator("-", 1, false)
    object multiplication : Operator("*", 2, false)
    object division : Operator("/", 2, false)
    object exponentiation : Operator("^", 3, true)
    object roll : Operator("d", 4, false)

    fun operate(right: Double, left: Double): Double = when(this){
        is addition -> left.plus(right)
        is subtraction -> left.minus(right)
        is multiplication -> left.times(right)
        is division -> left.div(right)
        is exponentiation -> left.pow(right)
        is roll -> left.roll(right.toInt())
    }

    override fun compareTo(other: Operator): Int{
        return when{
            this.precedence > other.precedence -> 1
            this.precedence == other.precedence -> 0
            else -> -1
        }
    }
    override fun toString(): String = symbol
}
