package com.gmail.wizaripost.seedFinder.service.processor.codez

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Code Z] 6 of a kind in free game
*/

@Service
class SixOfAKindInFreeGame(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key == "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)

//        val matrix = data.gameRound?.gameState?.Cascade?.stg[0].cntnt
        val matrices = resp.result?.gameState?.public?.Cascade?.stg?.get(0)?.cntnt
        val prizeList = resp.result?.gameState?.public?.Cascade?.stg?.get(0)?.lstPrz?.firstOrNull()
        if (matrices == null || prizeList == null) {
            return
        }
        val mask = prizeList.mask
        if (mask == null) {
            return
        }



        var count = 0
        for (prize in mask) {
            for (case in prize) {
                if (case == 1) {
                    count++
                    break
                }
            }
        }
        if (count != 6) {
            return
        }

        // Начинаем с первого списка как множество
        var commonElements = matrices[0].toSet()

        // Пересекаем с каждым следующим списком
        for (i in 1 until matrices.size) {
            commonElements = commonElements.intersect(matrices[i].toSet())
        }

        // Выводим результат
        if (commonElements.isEmpty()) {
            return
        } else {
            val seed = resp.result?.gameState?.private?.modelCore?.seed
            println("[6 of a kind in free game] $seed")
        }

    }
}