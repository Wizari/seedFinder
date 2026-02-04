package com.gmail.wizaripost.seedFinder.service.processor.codez

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.SedConverter
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


/*
* [Code Z] 30.01.26 Поиск коротких Gamble
*/

//@Service
class ZeroWinInRiskGame(
    private val om: ObjectMapper,
    private val utils: Utils,
    private val sedConverter: SedConverter
) : LoggingService(),
    ResultPostProcessor {
    private val sessionGambleBoolean = ConcurrentHashMap<Long, Boolean>()
    private val sessionPrefSeed = ConcurrentHashMap<Long, Long>()
    private val sessionBnkCount = ConcurrentHashMap<Long, Int>()

    override fun process(key: String, payload: Any) {
        var gambleBoolean = sessionGambleBoolean[0]
        var prefSeed: Long? = sessionPrefSeed[0]
//        var bnkCount = sessionBnkCount[0]

        if (key == "Spin") {
//            gambleBoolean = false
//            prefSeed = 0
////            bnkCount = 0
            sessionGambleBoolean[0] = false
            sessionPrefSeed[0] = 0
//            sessionBnkCount[0] = 0
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
//        val bnk = resp.result?.gameState?.public?.FreeSpinsRisk?.bnk
        if (key == "FreeSpin") {
            val seed = resp.result?.gameState?.private?.modelCore?.seed
//                sessionBnkCount[0] = bnk

            sessionGambleBoolean[0] = true
            seed?.let { sessionPrefSeed[0] = it or 0 }
//            sessionBnkCount[0] = bnk
        }
        if (key == "Close" && gambleBoolean == true) {
            var result = resp.result?.gameState?.public?.resultGC?.or(0L)
                if (result == 0L && prefSeed != null) {
                    println("Zero win gamble: $prefSeed -> ${sedConverter.toULong(prefSeed)}")
                }
            }
        }

    }
