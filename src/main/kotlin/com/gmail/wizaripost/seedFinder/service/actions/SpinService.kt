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

        var maxBaseBet = configResponse.result?.modelCore?.listSLB?.last()?: 2
//        val baseGameBet = configResponse.result?.modelCore?.betTypes?.get(0)?.baseGameBet?: 1
//        var betMultiplayer TODO для игры с бет множителем(multiplier)(Legends OH)
//        var bet = maxBaseBet * baseGameBet


        val executeRequest = SpinRequest(
            command = "Spin",
            denomination = configResponse.result?.modelCore?.listLAD?.get(0)?.denomination,
            lines = configResponse.result?.modelCore?.listLAD?.get(0)?.listLA?.get(0),
            lineBet = maxBaseBet,
            betType = configResponse.result?.modelCore?.betTypes?.get(0)?.id,
            risk = false,
            gameState = gameResponse.result!!,
            demoId = -1,
            demoSeed = -1
        )
        return mathClient.executeSpin(gameId, executeRequest)
    }
}