package com.gmail.wizaripost.seedFinder.service.actions

import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.dto.RiskSpinRequest
import org.springframework.stereotype.Service

@Service
class RiskSpinService(
    private val mathClient: MathClient,
) {

    fun execute(gameId: String, configResponse: GameResponse?): String {
        val executeRequest = RiskSpinRequest(
            command = "Gamble",
            risk = false, // или извлекать из gameState/config
            gameState = configResponse!!.result!!["gameState"]!!,
        )
        return mathClient.executeRisk(gameId, executeRequest)
    }
}