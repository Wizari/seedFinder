package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks]
*/

@Service
class FourthReelThreeBallsFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
        val matrix = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)

//        val result = booleanArrayOf(true, true, true, false, false)
////    var totalBalls = matrix.sumOf { reel -> reel.count { it == 1 } }

        if (matrix == null || height == null) {
            return
        }


        var newMatrix = utils.getVisibleMatrix(height, matrix)

//        if (height != 8) {
//            return
//        }

        var totalBalls = 0
        for (row in newMatrix[4].indices) {
            val ballId = newMatrix[3][row].id
            if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
//            if ( ballId == 6) {
                totalBalls++
//                println("есть шары на 5 риле: $seed")

            }
        }
        if (totalBalls >= 3) {
            println("есть шары на 4 риле: $seed")
        }


//        for (reel in matrix.indices) {
//            for (row in matrix[reel].indices) {
//                val ballId = matrix[reel][row].id
//                if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
//                    totalBalls++
//                }
//            }
//        }
//
//        if (totalBalls >= 6) {
//            println("seed 6/6+ balls: $seed")
//        }
//        if (totalBalls == 0) {
//            return
//        }

//        totalBalls = 0
//        for (reel in 0 until 4) {
//            for (row in matrix[reel].indices) {
//                val ballId = matrix[reel][row].id
//                if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
//                    totalBalls++
//                }
//            }
//        }



//        for (row in matrix[4].indices) {
//            val ballId = matrix[4][row].id
//            if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
////                totalBalls++
//                println("есть шары на 5 риле: $seed")
//
//            }
//        }
//        if ((totalBalls + matrix[4].size) >= 6) {
//            println("есть шары на 5 риле: $seed")
//        }
    }
}


//        if (height != 3) {
//            return
//        }
//
//        if (matrix != null) {
//            var count = 0
//            var firstTrigger = false
//
//            for (i in 0..2) {  // только индексы 0,1,2,3
//                val currentReel = matrix.get(i)
//                var iterator = 0
//                for (symbol in currentReel) {
//                    if (iterator > 4) {
//                        if (symbol.id == 6 || symbol.id == 1) {
//                            count++
//                            firstTrigger = true
//                        }
//                    }
//                    iterator++
//                }
//            }
//            var trigger = false
//            var iterator = 0
//            for (symbol in matrix[3]) {
//                if (iterator > 4) {
//                    if (symbol.id == 6 || symbol.id == 1) {
//                        count++
//                        trigger = true
//                    }
//                }
//                iterator++
//            }
//
//
//            var lastReelTrigger = false
//            for (symbol in matrix[4]) {
//                if (iterator > 4) {
//                    if (symbol.id == 6 || symbol.id == 1) {
//                        lastReelTrigger = true
//                    }
//                }
//                iterator++
//            }
//            if (count == 0) {
//                return
//            }
//            if (count > 2) {
//                return
//            }
//            if (trigger && firstTrigger && lastReelTrigger) {
//                println(seed)
//            }
//        }


//}
//}
