package com.gmail.wizaripost.seedFinder.service.actions

import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.dto.NewGameRequest
import org.springframework.stereotype.Service

@Service
class NewGameService(
    private val mathClient: MathClient,
) {

    fun execute(gameId: String, seed: ULong): String {
        val newGameRequest = NewGameRequest("NewGame", seed)
        var executeResponse = mathClient.newGame(gameId, newGameRequest)
        println(executeResponse)
        return executeResponse
    }


}