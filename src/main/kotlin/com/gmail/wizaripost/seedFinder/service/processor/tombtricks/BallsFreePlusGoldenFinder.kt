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

//@Service
class BallsFreePlusGoldenFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
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
        val matrixBalls = resp.result?.gameState?.public?.brilliantSpins?.matrix
//        val heightBalls = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        val height = resp.result?.gameState?.public?.brilliantSpins?.height
        val transfer = resp.result?.gameState?.public?.brilliantSpins?.transfers?.firstOrNull()

        if (transfer == null) {
            return
        }

        val matrix = resp.result?.gameState?.public?.brilliantSpins?.transfers?.get(0)?.matrix

        if (matrix == null || height == null) {
            return
        }
        if (matrixBalls == null) {
            return
        }
        val newBallsMatrix = utils.getVisibleMatrix(height, matrixBalls)
        val newMatrix = utils.getVisibleMatrix(height, matrix)

        var freeSpin = 0
        var prizeUp = 0
        var bomb = 0
        var golden = 0
        var freeBalls = 0

        for (i in 0..4) {  // только индексы 0,1,2,3
            val currentReel = newBallsMatrix[i]
            for (symbol in currentReel) {
                val ballId = symbol.id
                when (ballId) {
                    1 -> { //freeBall
                        golden++
                    }

                    6 -> { //golden
                        freeBalls++
                    }

                    else -> {

                    }
                }
            }
        }
        if (freeBalls == 0 || golden == 0) {
            return
        }

        for (i in 0..4) {  // только индексы 0,1,2,3
            val currentReel = newMatrix[i]
            for (symbol in currentReel) {
                val ballId = symbol.id
                when (ballId) {
//                    1 -> {
//                        goldeb++
//                    }
//                    5 -> {
//                        freeBalls++
//                    }
//                    7 -> {
//                        freeSpin++
//                    }
                    8 -> {
                        prizeUp++
                    }
//
//                    9 -> {
//                        bomb++
//                    }

                    else -> {

                    }
                }
            }
        }
        if (prizeUp >= 1) {
            println("[fre+golden+prizeup]: $seed")
            logSeed("[fre+golden+prizeup]: $seed")
        }
    }

}