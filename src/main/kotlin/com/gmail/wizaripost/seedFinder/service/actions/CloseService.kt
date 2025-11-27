package com.gmail.wizaripost.seedFinder.service.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.CloseRequest
import com.gmail.wizaripost.seedFinder.dto.FreeSpinRequest
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.ExtractConfigParameters
import org.springframework.stereotype.Service

@Service
class CloseService(
    private val mathClient: MathClient,
) {

    fun execute(gameId: String, configResponse: GameResponse?): String {
        val test = configResponse!!.result!!["gameState"]!!
        println(test)
        val executeRequest = CloseRequest(
            command = "Close",
            gameState = configResponse.result!!["gameState"]!!,
        )
        return mathClient.executeClose(gameId, executeRequest)
    }
}