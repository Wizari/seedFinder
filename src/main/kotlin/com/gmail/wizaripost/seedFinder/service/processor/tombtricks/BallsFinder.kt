package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] Песочница для поиска Шаров
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
//TODO Не пашет
//@Service
class BallsFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val amount = resp.result?.gameState?.public?.brilliantSpins?.amountResetSpins
//        if (amount != null) {
//            if (amount <= 5) {
////            if (amount != 4) {
//                return
//            }
//        }

        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
        val matrixBalls = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        val heightBalls = resp.result?.gameState?.public?.brilliantSpins?.height
        if (matrixBalls == null || height == null) {
            return
        }

//        val transfer = resp.result?.gameState?.public?.brilliantSpins?.transfers?.firstOrNull()
//        if (transfer == null) {
//            return
//        }
        println("${height}")

        println("123123123")
        val matrixTransfers = resp.result?.gameState?.public?.brilliantSpins?.transfers?.get(0)?.matrix
        println("123123123")

        println("${matrixTransfers?.size}")
        if (matrixTransfers == null) {
            return
        }
        val newBallsMatrix = utils.getVisibleMatrix(height, matrixBalls)
        val newMatrix = utils.getVisibleMatrix(height, matrixTransfers)

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
            val currentReel = matrixTransfers[i]
            for (symbol in currentReel) {
                val ballId = symbol.id
                if (ballId != 0) {
                    count++
                }
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
//        if (count == 6) {
//            if (superBall != 0) {
//                println("[superBallX$superBall]: $seed")
//                logSeed("[superBallX$superBall]: $seed")
//            }
//            if (megaBall != 0) {
//                println("[megaBallX$megaBall]: $seed")
//                logSeed("[megaBallX$megaBall]: $seed")
//            }
//            if (major != 0) {
//                println("[majorX$major]: $seed")
//                logSeed("[majorX$major]: $seed")
//            }
//            if (grand != 0) {
//                println("[grandX$major]: $seed")
//                logSeed("[grandX$major]: $seed")
//            }
//        }

//        var golden = 0
//        var superBall = 0
//        var megaBall = 0
//        var major = 0
//        var grand = 0
//        var freeBalls = 0
//        var upSpin = 0
//        var prizeUp = 0
//        var bomb = 0

        if (count == 6) {
            if (major == 0 && grand == 0 && freeBalls == 0 && upSpin == 0 && prizeUp == 0 &&  bomb == 0) {
                if ((superBall == 0 && megaBall == 1) || (superBall == 1 && megaBall == 0)) {
                    println("[fast golden (+super or mega)$superBall]: $seed")
                    logSeed("[fast golden (+super or mega)[$superBall]: $seed")
                }
            }

//                if (superBall != 0) {
//                println("[superBallX$superBall]: $seed")
//                logSeed("[superBallX$superBall]: $seed")
//            }
//            if (megaBall != 0) {
//                println("[megaBallX$megaBall]: $seed")
//                logSeed("[megaBallX$megaBall]: $seed")
//            }
//            if (major != 0) {
//                println("[majorX$major]: $seed")
//                logSeed("[majorX$major]: $seed")
//            }
//            if (grand != 0) {
//                println("[grandX$major]: $seed")
//                logSeed("[grandX$major]: $seed")
//            }
        }


    }

}


//    override fun process(key: String, payload: Any) {
//        if (key != "Spin") {
//            return
//        }
//        val resp: GameStateResponse = om.readValue(payload as String)
//        val seed = resp.result?.gameState?.private?.modelCore?.seed
//        val matrix = resp.result?.gameState?.public?.brilliantSpins?.matrix
//        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
//
//        if (matrix == null || height == null) {
//            return
//        }
//        val newMatrix = utils.getVisibleMatrix(height, matrix)
//        var totalBalls = 0
//
//        for (i in 0..4) {  // только индексы 0,1,2,3
//            val currentReel = newMatrix[i]
//            for (symbol in currentReel) {
//                val ballId = symbol.id
//                if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
//                    totalBalls++
//                }
//            }
//        }
//
//        if (totalBalls < 1) {
//            return
//        }
//
//        var ballsInCurrentReel = 0
//        for (row in newMatrix[0].indices) {
//            val ballId = newMatrix[3][row].id
//            if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
////            if (ballId == 6) {
//                ballsInCurrentReel++
//            }
//        }
//        val reel = 3
//        if (ballsInCurrentReel == reel) {
//            println("есть шары на $reel риле: $seed")
//        }
//    }
//}