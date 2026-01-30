package com.gmail.wizaripost.seedFinder.service.processor.tombtricks.n2026First

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] Wild in freeGame ..  Wild in freeGame + wild за шторой
*/

//@Service
class FindWildInFreeGame(
    private val om: ObjectMapper,
    private val utils: Utils
) : LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        val resp: GameStateResponse = om.readValue(payload as String)
        if (key == "Spin") {
            return
        }
        val currentSpin = resp.result?.gameState?.public?.freeSpins?.currentSpin

        if (currentSpin != 1) {
            return
        }
        val prize = resp.result?.gameState?.public?.gmtrPrz?.prize
        if (prize == 0) {
            return
        }
        val wildMutipliersFeature = resp.result?.gameState?.public?.wildMultipliersFeature?.multipliers?.firstOrNull()
        if (wildMutipliersFeature == null) {
            return
        }
        val matrix = resp.result?.gameState?.public?.matrixMS?.content
        if (matrix == null) {
            return
        }


        var count = 0
        for (row in matrix) {
            for (symbol in row) {
                if (symbol == 13) {
                    count++
                }
            }

        }

        if (count == 0) {
            return
        }
//      Wild in first freeGame
        if (count >= 2) {
            val seed = resp.result?.gameState?.private?.modelCore?.seed
            println("$seed")
        }


//      Wild in first freeGame + wild за шторой
//      Wild in first freeGame + wild за шторой
//      Wild in first freeGame + wild за шторой
//      Wild in first freeGame + wild за шторой  !!!!!!!!!

//        if (count <= 1) {
//            return
//        }
//
//        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
////        println(height)
//        if (height == null) {return}
//
//        count = 0
//        var first =false
//        var second =false
//        for (currentReel in matrix) {
//            var iterator = 0
//            for (symbol in currentReel) {
//                if (iterator > height + 1) {
//                    if (symbol == 12) {
//                        count++
//                        first = true
//                    }
//                }
//                if (iterator < height) {
//                    if (symbol == 12) {
//                        second = true
//                        count++
//                    }
//                }
//                iterator++
//            }
//        }
//
//        if (first && second) {
//            val seed = resp.result?.gameState?.private?.modelCore?.seed
//            println("$seed")
//        }

    }
}