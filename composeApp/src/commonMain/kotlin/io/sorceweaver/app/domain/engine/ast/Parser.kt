package io.sorceweaver.app.domain.engine.ast

import io.sorceweaver.app.domain.engine.ast.nodes.DiceNode
import io.sorceweaver.app.domain.engine.ast.nodes.DropLowestNode
import io.sorceweaver.app.domain.engine.ast.nodes.KeepHighestNode
import io.sorceweaver.app.domain.engine.ast.nodes.MathNode
import io.sorceweaver.app.domain.engine.ast.nodes.Node
import io.sorceweaver.app.domain.engine.ast.nodes.NumberNode
import io.sorceweaver.app.domain.engine.ast.nodes.VarNode
import io.sorceweaver.app.domain.models.Token
import io.sorceweaver.app.domain.models.TokenType
import io.sorceweaver.app.domain.models.TokenType.DICE
import io.sorceweaver.app.domain.models.TokenType.DIVIDE
import io.sorceweaver.app.domain.models.TokenType.DROP_LOWEST
import io.sorceweaver.app.domain.models.TokenType.EOF
import io.sorceweaver.app.domain.models.TokenType.KEEP_HIGHEST
import io.sorceweaver.app.domain.models.TokenType.MINUS
import io.sorceweaver.app.domain.models.TokenType.MULTIPLY
import io.sorceweaver.app.domain.models.TokenType.NUMBER
import io.sorceweaver.app.domain.models.TokenType.PAREN_LEFT
import io.sorceweaver.app.domain.models.TokenType.PAREN_RIGHT
import io.sorceweaver.app.domain.models.TokenType.PLUS
import io.sorceweaver.app.domain.models.TokenType.VARIABLE

class Parser(private val tokens: List<Token>) {

    private var position = 0
    private val currentToken: Token
        get() = if (position >= tokens.size) Token(EOF, "") else tokens[position]

    private fun advance() {
        position++
    }

    private fun getBindingPower(
        token: TokenType
    ): Int{
        return when(token){
            PLUS, MINUS -> 1
            MULTIPLY, DIVIDE -> 2
            DICE -> 3
            KEEP_HIGHEST -> 4
            DROP_LOWEST -> 4
            else -> 0
        }
    }

    private fun parsePrefix(): Node{
        val token = currentToken
        advance()
        return when(token.type){
            NUMBER -> {
                val value: Double = token.value.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: ${token.value}")
                NumberNode(value)
            }
            VARIABLE -> {
                VarNode(token.value)
            }
            PAREN_LEFT -> {
                val innerExpression = parseExpression()
                if(currentToken.type != PAREN_RIGHT){
                    throw IllegalArgumentException("Missing closing parenthesis")
                }
                advance()
                innerExpression
            }
            DICE -> {
                val right = parseExpression()
                DiceNode(NumberNode(1.0), right)
            }
            else -> throw IllegalArgumentException("Invalid prefix token: ${token.type}")
        }
    }

    private fun parseInfix(
        left: Node
    ): Node{
        val token = currentToken
        advance()
        val precedence = getBindingPower(token.type)
        return when(token.type){
            PLUS, MINUS, MULTIPLY, DIVIDE -> {
                MathNode(left, token.type, parseExpression(precedence))
            }
            KEEP_HIGHEST -> {
                KeepHighestNode(left, parseExpression(precedence))
            }
            DROP_LOWEST -> {
                DropLowestNode(left, parseExpression(precedence))
            }
            DICE -> {
                DiceNode(left, parseExpression(precedence)
                )
            }
            else -> throw IllegalArgumentException("Invalid infix token: ${token.type}")
        }
    }
     private fun parseExpression(
         precedence: Int = 0
     ): Node{
         var leftNode = parsePrefix()
         while(precedence < getBindingPower(currentToken.type)){
             leftNode = parseInfix(leftNode)
         }
         return leftNode
     }

    fun parse(): Node{
        val expression = parseExpression()
        if (currentToken.type != EOF){
            throw IllegalArgumentException("Invalid expression")
        }
        return expression
    }
}