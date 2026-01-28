package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.dto.MatrixCell
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


/*
* [Tomb Tricks] 2 особых шар (surep, mega, grand, major) на закруте голден спинов


*    * 9 - bomb
     * 8 - prizeUp
     * 7 - addSpins
     * 6 - FreeBall
     * 5 - Grand
     * 4 - Major
     * 3 - megaBall
     * 2 - superBall
     * 1 - GoldenBall
     * 0 - нет шара
*/

//@Service
class FindSimpleGoldenGameAndMega(
    private val om: ObjectMapper,
    private val utils: Utils
) : LoggingService(), ResultPostProcessor {

    // ConcurrentHashMap потокобезопасен
    private val sessionMatrices = ConcurrentHashMap<Long, List<List<MatrixCell>>>()

    override fun process(key: String, payload: Any) {
        val resp: GameStateResponse = om.readValue(payload as String)

        // Предполагаем, что у вас есть идентификатор сессии или пользователя
        val seed = resp.result?.gameState?.private?.modelCore?.seed

        val sessionId = seed ?: return

        if (key == "Spin") {
            resp.result?.gameState?.public?.brilliantSpins?.matrix?.let {
                sessionMatrices[sessionId] = it
            }
            return
        }

        val initialsMatrix = sessionMatrices[sessionId]
        if (initialsMatrix == null) {
            println("No matrix found for session: $sessionId")
            return
        }
        if (key != "FreeSpin") {
//        if (key != "Spin") {
            return
        }
        val amount = resp.result?.gameState?.public?.brilliantSpins?.amountResetSpins
        if (amount != null) {
//            if (amount <= 3) {
//            if (amount == 4) {
//                return
//            }
        }

        val matrixBalls = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        val heightBalls = resp.result?.gameState?.public?.brilliantSpins?.height
        if (matrixBalls == null || height == null || initialsMatrix == null) {
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
        val newTransferMatrix = utils.getVisibleMatrix(heightBalls, matrixTransfers)
        val newBallsMatrix = utils.getVisibleMatrix(height, matrixBalls)
        val newinitialsMatrix = utils.getVisibleMatrix(height, initialsMatrix)

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


        for (i in 0..4) {
            val currentReel = newBallsMatrix[i]
            val pefMatrix = newinitialsMatrix!![i]
            for (i in 0..currentReel.size-1) {
                val symbol = currentReel[i]
                val pefSymbol = pefMatrix[i]
//                println(pefSymbol.id)
//                println(symbol.id)
                val ballId = symbol.id
//                if (ballId != 0) {
                    count++
//                }
                if (pefSymbol.id == 0) {
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
                    6 -> {
                        freeBalls++
                    }

                    7 -> {
                        upSpin++
                    }
                    8 -> {
                        prizeUp++
                    }

                    9 -> {
                        bomb++
                    }

                        else -> {

                        }
                    }
                }
            }

        }
//        println("123")
//        if (count == 6) {
//        if (superBall >= 2) {
//            println("[superBallX$superBall]: $seed")
//            logSeed("[superBallX$superBall]: $seed")
//        }
//        if (megaBall >= 2) {
//            println("[megaBallX$megaBall]: $seed")
//            logSeed("[megaBallX$megaBall]: $seed")
//        }
//        if (major >= 2) {
//            println("[majorX$major]: $seed")
//            logSeed("[majorX$major]: $seed")
//        }
//        if (grand >= 2) {
//            println("[grandX$grand]: $seed")
//            logSeed("[grandX$major]: $seed")
//        }

        if (count >= 6) {
            if (major == 0 && grand == 0 && freeBalls == 0 && upSpin == 0 && prizeUp == 0 &&  bomb == 0) {
                if ((superBall == 0 && megaBall == 1) || (superBall == 1 && megaBall == 0)) {
                    println("[fast golden (+super or mega)$superBall]: $seed")
                    logSeed("[fast golden (+super or mega)[$superBall]: $seed")
                }
                }
            }
        sessionMatrices[sessionId] = matrixBalls
    }
}



