package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] Шесть шаров + большая idle мумия -win
*/

//@Service
class SixBallsAndMummyFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
        val matrix = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        val bigSymbols = resp.result?.gameState?.public?.bigSymbolFeature?.tlc ?: emptyList()
        val lstPrz = resp.result?.gameState?.public?.gmtrPrz?.lstPrz

        if (lstPrz == null) {
            return
        }
        if (lstPrz.isEmpty()) {
            return
        }

//        if (listFJps.size < 2) {
//            return
//        }
//


        if (bigSymbols.isEmpty()) {
            return
        }
        for (bigSymbol in bigSymbols) {
            if (bigSymbol.reel == 0) {
                return
            }
        }

        if (matrix == null || height == null) {
            return
        }
        if (height <= 6) {
            return
        }
        val newMatrix = utils.getVisibleMatrix(height, matrix)
        var totalBalls = 0

//        for (i in 0..4) {  // только индексы 0,1,2,3,4
        for (i in 0..newMatrix.size - 1) {  // только индексы 0,1,2,3,4
            val currentReel = newMatrix[i]
            for (symbol in currentReel) {
                val ballId = symbol.id
                if (ballId == 1 || ballId == 2 || ballId == 3 || ballId == 4 || ballId == 5 || ballId == 6) {
                    totalBalls++
                }
            }
        }

        if (totalBalls < 6) {
            return
        }
        println(seed)
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
    }
}