package com.gmail.wizaripost.seedFinder.service.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.dto.SpinRequest
import com.gmail.wizaripost.seedFinder.service.ExtractConfigParameters
import org.springframework.stereotype.Service

@Service
class SpinService(
    private val extractConfigParameters: ExtractConfigParameters,
    private val mathClient: MathClient,
) {

    fun execute(gameId: String, configResponse: GameResponse): String {
        val (denomination, linesAmount, betType) = extractConfigParameters.extractTriple(configResponse.result)

        val executeRequest = SpinRequest(
            command = "Spin",
            denomination = denomination,
            lines = linesAmount,
            lineBet = 2, // или извлекать из gameState, если меняется
            betType = betType,
            risk = false, // или извлекать из gameState/config
            gameState = configResponse.result!!,
            demoId = -1,
            demoSeed = -1
        )
        return mathClient.executeSpin(gameId, executeRequest)
    }
}