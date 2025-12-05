package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.dto.PrzItem
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] Вин Мумия + мультивин
*/

@Service
class BigSymbolAndMultiWinFinder(private val om: ObjectMapper) : LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug

        // Получаем данные о большом символе
        val bigSymbol = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()
        val bigSymbols = resp.result?.gameState?.public?.bigSymbolFeature?.tlc ?: emptyList()

        if (bigSymbol == null) {
            return
        }


        val matrix = resp.result?.gameState?.public?.matrixMS?.content

        for (bigSymbol in bigSymbols) {
            if (bigSymbol.row != 7 || bigSymbol.reel != 0) {
                return
            }
        }

        val bigSymbolRow = bigSymbol?.row
        val bigSymbolReel = bigSymbol?.reel

        // Получаем выигрышные комбинации и матрицу
        val lstPrz = resp.result?.gameState?.public?.gmtrPrz?.lstPrz

        if (lstPrz == null) {
            return
        }
        if (lstPrz.size <= 2) {
            return
        } else {
            println(seed)
        }


    }

}