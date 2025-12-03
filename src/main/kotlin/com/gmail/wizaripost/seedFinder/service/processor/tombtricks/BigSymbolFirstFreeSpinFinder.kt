package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.dto.PrzItem
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service
/*
* [Tomb Tricks] Большая мумия на первом фри спине
*/
@Service
class BigSymbolFirstFreeSpinFinder(private val om: ObjectMapper) :LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "FreeSpin") {
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug

        // Получаем данные о большом символе
        val bigSymbol = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()
        val bigSymbols = resp.result?.gameState?.public?.bigSymbolFeature?.tlc ?: emptyList()

        val currentSpin = resp.result?.gameState?.public?.freeSpins?.currentSpin

        if (bigSymbol == null) {
            return
        }
        if (currentSpin != 1) {
            return
        }

        logSeed(seed)
        println(resp.result.gameState.private?.modelCore?.seed)
    }
}