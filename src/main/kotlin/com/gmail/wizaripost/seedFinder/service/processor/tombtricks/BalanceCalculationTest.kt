package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.utils.BalanceManager
import org.springframework.stereotype.Service

/*
* [Tomb Tricks] BalanceCalculationTest
*/
//@Service
class BalanceCalculationTest(
    private val om: ObjectMapper,
    private val balanceManager: BalanceManager,
) : ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        val resp: GameStateResponse = om.readValue(payload as String)
        val income = resp.result?.gameState?.public?.resultGC ?: 0
        val seed = resp.result?.gameState?.private?.modelCore?.seed

        if (key != "Close") {
            println("seed: $seed")

            return
        }
        balanceManager.increase(income)
        println("income: $income")
        println("balance: ${balanceManager.getCurrentBalance()}")

    }
}