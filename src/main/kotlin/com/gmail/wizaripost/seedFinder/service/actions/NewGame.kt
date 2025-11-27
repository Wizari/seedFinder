package com.gmail.wizaripost.seedFinder.service.actions

import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.dto.NewGameRequest
import org.springframework.stereotype.Service

@Service
class NewGame(
    private val mathClient: MathClient,
) {

    fun run(seed: ULong): GameResponse {
        // Шаг 1: NewGame
        val newGameRequest = NewGameRequest("NewGame", seed)
//        val newGameHeaders =
//            mapOf(ContentTypeInterceptor.CUSTOM_CONTENT_TYPE_HEADER to ContentTypeInterceptor.VND_API_JSON)
        var executeResponse = mathClient.newGame(newGameRequest)
        // Выводим ответ в консоль как JSON строку
//        println(objectMapper.writeValueAsString(executeResponse))
        println(executeResponse)
        return executeResponse
//            var gameState = newGameResponse.result?.get("gameState") as? Map<String, Any>
//            var gameState = objectMapper.writeValueAsString(executeResponse.result)
//            println("+++++++++++" + gameState)
    }


}