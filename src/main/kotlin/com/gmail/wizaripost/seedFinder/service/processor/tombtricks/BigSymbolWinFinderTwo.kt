package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] Поиск большой мумии в разных поз.
*/

//@Service
class BigSymbolWinFinderTwo(private val om: ObjectMapper) : LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug

        // Получаем данные о большом символе
        val bigSymbol = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()
        val bigSymbols = resp.result?.gameState?.public?.bigSymbolFeature?.tlc ?: emptyList()
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        if (height == 8) {
            return
        }
        if (bigSymbol == null) {
            return
        }


        for (bigSymbol in bigSymbols) {
            if (bigSymbol.row == 0 && bigSymbol.reel != 0 && bigSymbol.height == 1) {
                val matrix = resp.result?.gameState?.public?.matrixMS?.content

                if (matrix != null) {
                    val firstReel = matrix[0]

                    var count = 0
                    for (element in matrix[0]) {
                        if (element == 1) {
                            count++
                        }
                    }
                    if (count == 0) {
                        println(seed)
                    }
                }
            }
        }

    }
}