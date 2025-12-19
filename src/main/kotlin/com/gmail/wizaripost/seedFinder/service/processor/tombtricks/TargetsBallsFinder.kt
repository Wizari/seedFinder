package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] 1 особый шар (surep, mega, grand, major) при старте голден спинов:


*    * 9 - bomb
     * 8 - prizeUp
     * 7 - addSpins
     * 6 - FreeBall
     * 5 - Grand
     * 4 - Major
     * 3 - ?
     * 2 - ?
     * 1 - GoldenBall
     * 0 - нет шара
*/

//@Service
class TargetsBallsFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "FreeSpin") {
//        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val amount = resp.result?.gameState?.public?.brilliantSpins?.amountResetSpins
        if (amount != null) {
//            if (amount <= 3) {
            if (amount == 4) {
                return
            }
        }

        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
        val matrixBalls = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        val heightBalls = resp.result?.gameState?.public?.brilliantSpins?.height
        if (matrixBalls == null || height == null) {
            return
        }

        val transfer = resp.result?.gameState?.public?.brilliantSpins?.transfers?.firstOrNull()
        if (transfer == null || heightBalls == null) {
            return
        }
        val matrixTransfers = resp.result?.gameState?.public?.brilliantSpins?.transfers?.get(0)?.matrix
        if (matrixTransfers == null) {
            return
        }
        val newMatrix = utils.getVisibleMatrix(heightBalls, matrixTransfers)
        val newBallsMatrix = utils.getVisibleMatrix(height, matrixBalls)

        var golden = 0
        var superBall = 0
        var megaBall = 0
        var major = 0
        var grand = 0
        var freeBalls = 0
        var upSpin = 0
        var prizeUp = 0
        var bomb = 0

        var count = 0

        for (i in 0..4) {  // только индексы 0,1,2,3
//            val currentReel = newMatrix[i]
            val currentReel = newMatrix[i]
            for (symbol in currentReel) {
                val ballId = symbol.id
//                if (ballId != 0) {
//                    count++
//                }
                when (ballId) {
//                    1 -> {
//                        golden++
//                    }
                    2 -> {
                        superBall++
                    }
                    3 -> {
                        megaBall++
                    }
                    4 -> {
                        major++
                    }
                    5 -> {
                        grand++
                    }
//                    6 -> {
//                        freeBalls++
//                    }

//                    7 -> {
//                        upSpin++
//                    }
//                    8 -> {
//                        prizeUp++
//                    }
//
//                    9 -> {
//                        bomb++
//                    }

                    else -> {

                    }
                }
            }
        }
//        if (count == 6) {
            if (superBall >= 2) {
                println("[superBallX$superBall]: $seed")
                logSeed("[superBallX$superBall]: $seed")
            }
            if (megaBall >= 2) {
                println("[megaBallX$megaBall]: $seed")
                logSeed("[megaBallX$megaBall]: $seed")
            }
            if (major >= 2) {
                println("[majorX$major]: $seed")
                logSeed("[majorX$major]: $seed")
            }
            if (grand >= 2) {
                println("[grandX$grand]: $seed")
                logSeed("[grandX$major]: $seed")
            }
//        }
    }

}
