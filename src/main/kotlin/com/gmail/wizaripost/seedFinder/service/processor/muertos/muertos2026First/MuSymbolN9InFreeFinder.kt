package com.gmail.wizaripost.seedFinder.service.processor.muertos.muertos2026First

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils


/*
* [Code Z] Найти сид c [9] символом >= Х2 во FreeSpin
*/

//@Service
class MuSymbolN9InFreeFinder(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key == "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)

//        val matrix = data.gameRound?.gameState?.Cascade?.stg[0].cntnt
        val matrices = resp.result?.gameState?.public?.Cascade?.stg
        if (matrices == null) {
            return
        }
        val winInThisSpin = resp.result?.gameState?.public?.modelCore?.resultS
        if (winInThisSpin == 0 || winInThisSpin == null) {
            return
        }

        var symbol = 0
        for (matrix in matrices.dropLast(1)) {
            symbol = 0
            for (reel in matrix.cntnt!!) {
                for (row in reel) {
                    if (row == 9) {
                        symbol++
                    }
                }

            }

        }
        if (symbol >= 6) {
            val seed = resp.result?.gameState?.private?.modelCore?.seed
            println("[$symbol] $seed")
            logSeed("[$symbol] $seed")
        }
    }
}