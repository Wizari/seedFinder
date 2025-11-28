package com.gmail.wizaripost.seedFinder.seedfinder


import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.stages.RoundStage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class SeedRunner(
    private val roundStage: Set<RoundStage>,
) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)


    fun run(gameId: String, payload: GameResponse?) {
        var response: GameResponse? = payload
        var action = "Spin"
        do {
            val stage = roundStage.find { it.valid(action) } ?: throw RuntimeException("Unknow stage $action")
            val gameResponse = response ?: throw RuntimeException("Response can't be null")
            val stageResponse = stage.execute(mapOf("gameId" to gameId, "payload" to gameResponse))
            action = stageResponse.nextAction
            response = stageResponse.response
        } while (action != "Spin")
    }

}