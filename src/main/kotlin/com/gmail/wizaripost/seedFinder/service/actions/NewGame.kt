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
        val newGameRequest = NewGameRequest("NewGame", seed)
        var executeResponse = mathClient.newGame(newGameRequest)
        println(executeResponse)
        return executeResponse
    }


}