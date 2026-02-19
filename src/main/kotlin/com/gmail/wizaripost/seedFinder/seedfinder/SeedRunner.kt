package com.gmail.wizaripost.seedFinder.seedfinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.stages.RoundStage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class SeedRunner(
    private val roundStage: Set<RoundStage>,
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)

    fun run(gameId: String, seed: ULong, configResponse: ConfigResponse) {

        var response: GameResponse =
            objectMapper.readValue("{ \"result\": { \"private\": { \"seed\": $seed }, \"public\": { \"actions\": [  \"Spin\" ] }}}")
        var action = "Spin"
        do {
//            val stage = roundStage.find { it.valid(action) } ?: throw RuntimeException("Unknow stage $action")
            val stage = roundStage.findLast { it.valid(action) } ?: throw RuntimeException("Unknown stage $action")
            val gameResponse = response ?: throw RuntimeException("Response can't be null")
            val stageResponse = stage.execute(
                mapOf(
                    "gameId" to gameId, "payload" to gameResponse, "configResponse" to configResponse
                )
            )

            if (stageResponse.nextAction == "FreeSpin") {

                val shouldRegister = isTriggerPresent(stageResponse.response)

                if (shouldRegister) {
                    action = "Register"
                } else {
                    action = stageResponse.nextAction
                }

            } else {
                action = stageResponse.nextAction
            }
//            action = stageResponse.nextAction
            response = stageResponse.response
        } while (action != "Spin")
    }



    fun isTriggerPresent(response: GameResponse): Boolean {
        val result = response.result ?: return false

        // Получаем jackpots из result
        val jackpots = result["jackpots"] ?: return false

        // Проверяем что jackpots это Map
        if (jackpots !is Map<*, *>) return false

        // Получаем trigger
        val trigger = jackpots["trigger"] ?: return false

        // Проверяем что trigger это коллекция и она не пустая
        return when (trigger) {
            is Collection<*> -> trigger.isNotEmpty()
            is Array<*> -> trigger.isNotEmpty()
            else -> false
        }
    }

}