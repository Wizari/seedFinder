package com.gmail.wizaripost.seedFinder.service.processor.codez

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Code Z] Найти сид с Х2 шотами[13] символами
*/

@Service
class SymbolN13Finder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)

//        val matrix = data.gameRound?.gameState?.Cascade?.stg[0].cntnt
        val matrices = resp.result?.gameState?.public?.Cascade?.stg
        if (matrices == null) {
            return
        }
        var symbol = 0
        for (matrix in matrices) {
            symbol = 0
            for (reel in matrix.cntnt!!) {
                for (row in reel) {
                    if (row == 13) {
                        symbol++
                    }
                }

            }

        }
        if (symbol >= 3) {
            val seed = resp.result?.gameState?.private?.modelCore?.seed
            println("[$symbol] $seed")
            logSeed("[$symbol] $seed")
        }
    }
}