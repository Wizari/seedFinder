package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] Требуется, чтобы большая выигрышная мумия был виден наполовину снизу + наполовину за шторой
*/

//@Service
class HalfVisibleBigSymbolFinder(private val om: ObjectMapper) : LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug

        // Получаем данные о большом символе
        val bigSymbol = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()
        val bigSymbols = resp.result?.gameState?.public?.bigSymbolFeature?.tlc ?: emptyList()

        var count = 0
        for (bigSymbol in bigSymbols) {
//            if (bigSymbol.row == 7 && bigSymbol.reel == 0) {
            if (bigSymbol.row == 7) {
                count++
//                println(seed)
            }
            if (bigSymbol.row == 4 && bigSymbol.reel == 0) {
//                println(seed)
                count++
            }
            if (count >= 2) {
                println(seed)
            }

        }
    }
}