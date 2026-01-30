package com.gmail.wizaripost.seedFinder.service.processor.codez

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.Utils
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


/*
* [Code Z] многопоточное только в Сlose не приходит сид. поэтому не передаются значения в раунд close
*/

//@Service
class SesFindShortRiskGame(
    private val om: ObjectMapper,
    private val utils: Utils
) : LoggingService(), ResultPostProcessor {

    // ConcurrentHashMap потокобезопасен
//    private val sessionMatrices = ConcurrentHashMap<Long, List<List<MatrixCell>>>()
    private val sessionMatrices = ConcurrentHashMap<Long, Int>()
    private val sessionGambleBoolean = ConcurrentHashMap<Long, Boolean>()

    override fun process(key: String, payload: Any) {
        val resp: GameStateResponse = om.readValue(payload as String)

        // Предполагаем, что у вас есть идентификатор сессии или пользователя
        val seed = resp.result?.gameState?.private?.modelCore?.seed
        val sessionId = seed ?: return


//        val count = sessionMatrices.getOrDefault(sessionId, 0)

//        if (key == "Spin") {
////            val count = sessionMatrices.getOrDefault(sessionId, 0)
//            return
//        }
//        val freeSpinRiskIsD = resp.result?.gameState?.public?.FreeSpinsRisk?.Activity?.isDisabled
//        if (key == "Gamble" && freeSpinRiskIsD == false) {
//            sessionMatrices[sessionId] = count + 1
////            println("$seed")
//        }
//
//        val bnk = resp.result?.gameState?.public?.FreeSpinsRisk?.bnk
//        if (key == "Gamble" && bnk && count <= 2) {
//            val seed = resp.result?.gameState?.private?.modelCore?.seed
//            println("[$count] $seed")
//        }

//        if (key == "Spin") {
////            val countBnk = sessionMatrices.getOrDefault(sessionId, 0)
//            return
//        }
        val countBnk = sessionMatrices.getOrDefault(sessionId, 0)

//        if (key == "Spin") {
////            val count = sessionMatrices.getOrDefault(sessionId, 0)
//            return
//        }
        val freeSpinRiskIsD = resp.result?.gameState?.public?.FreeSpinsRisk?.Activity?.isDisabled
        val bnk = resp.result?.gameState?.public?.FreeSpinsRisk?.bnk
        if (key == "Gamble" && freeSpinRiskIsD == false && bnk != null) {
            sessionGambleBoolean[sessionId] = true
            sessionMatrices[sessionId] = bnk
//            println("$seed")
        }
//        if (key == "Spin") {
//            println("[$key] $payload")
//        }
        val gambleBoolean = sessionGambleBoolean.getOrDefault(sessionId, false)

        if (key == "Spin" && gambleBoolean && countBnk <= 50) {
            val seed = resp.result?.gameState?.private?.modelCore?.seed
            println("[$countBnk] $seed")
        }
    }
}



