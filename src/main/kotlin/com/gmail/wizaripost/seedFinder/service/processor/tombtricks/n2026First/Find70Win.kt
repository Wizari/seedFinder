package com.gmail.wizaripost.seedFinder.service.processor.tombtricks.n2026First

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] фаун с выигрышем в 70 монет
*/

//@Service
class Find70Win(private val om: ObjectMapper, private val utils: Utils) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        var totalPrize = resp.result?.gameState?.public?.modelCore?.resultGC

        if (totalPrize == 5000) {
            val seed = resp.result?.gameState?.private?.modelCore?.seed?.or(0L)
            println(seed)
        }
    }
}