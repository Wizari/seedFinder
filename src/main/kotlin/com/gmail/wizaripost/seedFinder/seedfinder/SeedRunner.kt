package com.gmail.wizaripost.seedFinder.seedfinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.actions.NewGameService
import com.gmail.wizaripost.seedFinder.service.stages.ActionBuilder
import com.gmail.wizaripost.seedFinder.service.stages.RoundStage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class SeedRunner(
    private val mathClient: MathClient,
    private val newGameService: NewGameService,
    private val resultPostProcessor: ResultPostProcessor,
    private val objectMapper: ObjectMapper,
    private val roundStage: Set<RoundStage>,
    private val actionBuilder: ActionBuilder,
    ) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)



    fun run(seed: ULong) {

        var response: GameResponse? = null
        // Шаг 1: NewGame
        val gameId = "RumblingRun-variation-95"
        val responseNewGameString = newGameService.execute(gameId, seed)
        val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)

//        resultPostProcessor.process("NewGame", newGameResponse)
        var action: String = actionBuilder.getFirstAction(responseNewGameString)
//        logger.info("Executing command: $action")
        response = newGameResponse

        // Шаг 2: GetConfig
        val responseGetConfigString = mathClient.getConfig(gameId)
        val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
//        resultPostProcessor.process("GetConfig", configResponse)

        do {

            val stage = roundStage.find { it.valid(action) } ?: throw RuntimeException("Unknow stage $action")
            val gameResponse = response ?: throw RuntimeException("Response can't be null")
            val stageResponse = stage.execute(mapOf("gameId" to gameId, "payload" to gameResponse))
            action = stageResponse.nextAction
            response = stageResponse.response


        } while (action != "Spin")
        logger.info("Game round completed for seed: $seed")
    }

}