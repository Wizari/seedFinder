package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] ФриСпины с бомбой + прайзАп + Спины в одном раунде
*/

@Service
class BombAndPrizeUpAndSpinsUpFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "FreeSpin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val amount = resp.result?.gameState?.public?.brilliantSpins?.amountResetSpins
        if (amount != null) {
            if (amount <= 4) {
                return
            }
        }

        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
//        val matrix = resp.result?.gameState?.public?.brilliantSpins?.matrix
//        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        val height = resp.result?.gameState?.public?.brilliantSpins?.height

        val transfer = resp.result?.gameState?.public?.brilliantSpins?.transfers?.firstOrNull()

        if (transfer == null) {
            return
        }

        val matrix = resp.result?.gameState?.public?.brilliantSpins?.transfers?.get(0)?.matrix

        if (matrix == null || height == null) {
            return
        }
        val newMatrix = utils.getVisibleMatrix(height, matrix)

        var freeSpin = 0
        var prizeUp = 0
        var bomb = 0
        for (i in 0..4) {  // только индексы 0,1,2,3
            val currentReel = newMatrix[i]
            for (symbol in currentReel) {
                val ballId = symbol.id
                when (ballId) {
                    7 -> {
                        freeSpin++
                    }  // wildcard - любой символ
                    8 -> {         // маска 1-6
                        prizeUp++
                    }

                    9 -> {         // маска 1-6
                        bomb++
                    }

                    else -> {

                    }
                }
            }
        }
//        if (bomb >= 2) {
//            println("[bomb >= 2][$bomb]: $seed")
//            logSeed("[FirstAction][bomb >= 2][$bomb]: $seed")
//        }
        if (freeSpin != 0 && prizeUp != 0 && bomb != 0) {
            println("[7/8/9][$freeSpin/$prizeUp/$bomb]: $seed")
            logSeed("[7/8/9][$freeSpin/$prizeUp/$bomb]: $seed")
        }


    }

}