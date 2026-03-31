package io.sorceweaver.app.domain.models

enum class TokenType {

    // ========================
    // Literais e Identificadores
    // ========================
    NUMBER,
    VARIABLE,

    // ========================
    // Agrupamento
    // ========================
    PAREN_LEFT,   // (
    PAREN_RIGHT,  // )

    // ========================
    // Operadores Matemáticos
    // ========================
    PLUS,         // +
    MINUS,        // -
    MULTIPLY,     // *
    DIVIDE,       // /
    POWER,        // ^

    // ========================
    // Dados (RPG Core)
    // ========================
    DICE,             // d   (ex: 1d20)
    KEEP_HIGHEST,     // kh  (ex: 4d6kh3)
    DROP_LOWEST,      // dl  (ex: 4d6dl1)
    EXPLODE,          // !   (ex: 1d6!)

    // TODO: adicionar suporte a reroll (ex: r, rr)
    // TODO: adicionar sistema de sucesso (ex: s>5)

    // ========================
    // Comparação (Condicionais)
    // ========================
    GREATER,          // >
    LESS,             // <
    GREATER_EQUAL,    // >=
    LESS_EQUAL,       // <=
    EQUAL,            // ==   // TODO: considerar permitir "=" como alias
    NOT_EQUAL,        // !=

    // ========================
    // Lógicos
    // ========================
    AND,              // &&
    OR,               // ||
    NOT,              // !

    // ========================
    // Condicional (Ternário)
    // ========================
    QUESTION,         // ?
    COLON,            // :

    // ========================
    // Controle
    // ========================
    EOF
}

data class Token(
    val type: TokenType,
    val value: String
)