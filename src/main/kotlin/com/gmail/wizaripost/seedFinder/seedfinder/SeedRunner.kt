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
            val gameResponse = response
                ?: throw RuntimeException("Response can't be null")
            val stageResponse =
                stage.execute(mapOf(
                        "gameId" to gameId, "payload" to gameResponse, "configResponse" to configResponse
                    )
                )
            action = stageResponse.nextAction
            response = stageResponse.response
        } while (action != "Spin")
    }

}