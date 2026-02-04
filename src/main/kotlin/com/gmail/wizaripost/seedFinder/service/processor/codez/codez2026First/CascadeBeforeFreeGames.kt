package com.gmail.wizaripost.seedFinder.service.processor.codez.codez2026First

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Code Z] CascadeBeforeFreeGames
*/

//@Service
class CascadeBeforeFreeGames(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)

//        val matrix = data.gameRound?.gameState?.Cascade?.stg[0].cntnt
//        val matrices = resp.result?.gameState?.public?.Cascade?.stg?.get(0)?.cntnt
        val prizeList = resp.result?.gameState?.public?.Cascade?.stg?.get(0)?.lstPrz?.firstOrNull()

        val matrices = resp.result?.gameState?.public?.Cascade?.stg

        if (matrices == null) {
            return
        }
        if (matrices.get(0).cntnt == null) {
            return
        }


//        var haveBonus = false
//        for (matrix in matrices) {
//            for (symbol in matrix.cntnt!!.get(6)) {
//                if (symbol == 12) {
//                    haveBonus = true
//                }
//            }
//        }
//        if (!haveBonus) {
//            return
//        }
//        var symbol = 0
//
//        for (matrix in matrices) {
////            symbol = 0
//            for (reel in matrix.cntnt!!) {
//                for (row in reel) {
//                    if (row == 12) {
//                        symbol++
//                        if (symbol >= 2) {
//                            return
//                        }
//                    }
//                }
//            }
//        }


        val actions = resp.result?.gameState?.public?.actions


        if (matrices.size >= 2 && actions != null) {
            if (actions.size >= 2) {
                val seed = resp.result?.gameState?.private?.modelCore?.seed
                println("[CascadeBeforeFreeGames] $seed")
            }

        }
//
//        if (matrices == null || prizeList == null) {
//            return
//        }
//        val mask = prizeList.mask
//        if (mask == null) {
//            return
//        }
//
//
//
//        var count = 0
//        for (prize in mask) {
//            for (case in prize) {
//                if (case == 1) {
//                    count++
//                    break
//                }
//            }
//        }
//        if (count != 6) {
//            return
//        }
//
//        // Начинаем с первого списка как множество
//        var commonElements = matrices[0].toSet()
//
//        // Пересекаем с каждым следующим списком
//        for (i in 1 until matrices.size) {
//            commonElements = commonElements.intersect(matrices[i].toSet())
//        }
//
//        // Выводим результат
//        if (commonElements.isEmpty()) {
//            return
//        } else {
//            val seed = resp.result?.gameState?.private?.modelCore?.seed
//            println("[6 of a kind in free game] $seed")
//        }

    }
}