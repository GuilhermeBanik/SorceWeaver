package io.sorceweaver.app.domain.engine.ast

import io.sorceweaver.app.domain.models.Token
import io.sorceweaver.app.domain.models.TokenType

class Lexer(private val input: String) {
    private var position = 0

    private val currentChar: Char?
        get() = if (position >= input.length) null else input[position]

    private fun advance() {
        position++
    }

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (true) {
            val char = currentChar ?: break

            when {
                char.isWhitespace() -> advance()

                char.isDigit() -> tokens.add(Token(TokenType.NUMBER, captureNumber()))

                char == 'd' -> {
                    tokens.add(Token(TokenType.DICE, "d"))
                    advance()
                }

                char.isLetter() || char == '_' -> {
                    tokens.add(consumeIdentifier())
                }

                char == '+' -> { tokens.add(Token(TokenType.PLUS, "+")); advance() }
                char == '-' -> { tokens.add(Token(TokenType.MINUS, "-")); advance() }
                char == '*' -> { tokens.add(Token(TokenType.MULTIPLY, "*")); advance() }
                char == '/' -> { tokens.add(Token(TokenType.DIVIDE, "/")); advance() }
                char == '^' -> { tokens.add(Token(TokenType.POWER, "^")); advance() }
                char == '!' -> { tokens.add(Token(TokenType.EXPLODE, "!")); advance() }
                char == '(' -> { tokens.add(Token(TokenType.PAREN_LEFT, "(")); advance() }
                char == ')' -> { tokens.add(Token(TokenType.PAREN_RIGHT, ")")); advance() }

                else -> throw IllegalArgumentException(
                    "Erro de Sintaxe: Caractere não reconhecido -> '$char' na posição $position"
                )
            }
        }

        tokens.add(Token(TokenType.EOF, ""))
        return tokens
    }

    private fun captureNumber(): String {
        val startPos = position
        var hasDot = false

        while (true) {
            val char = currentChar ?: break

            if (char.isDigit()) {
                advance()
            } else if (char == '.' && !hasDot) {
                hasDot = true
                advance()
            } else {
                break
            }
        }
        return input.substring(startPos, position)
    }

    private fun consumeIdentifier(): Token {
        val startPos = position

        while (true) {
            val char = currentChar ?: break
            if (char.isLetter()) {
                advance()
            } else {
                break
            }
        }

        val text = input.substring(startPos, position)

        val type = when (text.lowercase()) {
            "d" -> TokenType.DICE
            "kh" -> TokenType.KEEP_HIGHEST
            "dl" -> TokenType.DROP_LOWEST
            else -> TokenType.VARIABLE
        }

        return Token(type, text)
    }
}