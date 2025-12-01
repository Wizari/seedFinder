package com.gmail.wizaripost.seedFinder.service.actions

import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.dto.SpinRequest
import com.gmail.wizaripost.seedFinder.service.ExtractConfigParameters
import org.springframework.stereotype.Service

@Service
class SpinService(
    private val mathClient: MathClient,
) {

    fun execute(gameId: String, gameResponse: GameResponse, configResponse: ConfigResponse): String {

        val executeRequest = SpinRequest(
            command = "Spin",
            denomination = configResponse.result?.modelCore?.listLAD?.get(0)?.denomination,
            lines = configResponse.result?.modelCore?.listLAD?.get(0)?.listLA?.get(0),
            lineBet = 2, // или извлекать из gameState, если меняется
            betType = configResponse.result?.modelCore?.listSLB?.last(),
            risk = false, // или извлекать из gameState/config
            gameState = gameResponse.result!!,
            demoId = -1,
            demoSeed = -1
        )
        return mathClient.executeSpin(gameId, executeRequest)
    }
}