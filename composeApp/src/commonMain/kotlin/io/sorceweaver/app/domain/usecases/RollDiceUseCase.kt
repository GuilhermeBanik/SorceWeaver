package io.sorceweaver.app.domain.usecases

import io.sorceweaver.app.domain.engine.ast.Lexer
import io.sorceweaver.app.domain.engine.ast.Parser
import io.sorceweaver.app.domain.models.EvaluationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CharacterRepository{
    suspend fun getCharacterAttributes(characterId: String): Map<String, Double>
}

class RollDiceUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(expression: String, characterId: String?): EvaluationResult {
        val contextMap = if (characterId != null) {
            repository.getCharacterAttributes(characterId)
        } else {
            emptyMap()
        }
        return withContext(Dispatchers.Default){
            val lexer = Lexer(expression)
            val tokens = lexer.tokenize()

            val parser = Parser(tokens)
            val ast = parser.parse()

            ast.evaluate(contextMap)
        }
    }
}