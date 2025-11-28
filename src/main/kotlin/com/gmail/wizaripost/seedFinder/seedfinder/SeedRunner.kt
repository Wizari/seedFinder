package com.gmail.wizaripost.seedFinder.seedfinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.actions.NewGameService
import com.gmail.wizaripost.seedFinder.service.stages.RoundStage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class SeedRunner(
    private val mathClient: MathClient,
    private val newGameService: NewGameService,
    private val resultPostProcessor: ResultPostProcessor,
    private val objectMapper: ObjectMapper,
    private val roundStage: Set<RoundStage>
) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)



    fun run(seed: ULong) {

        var response: GameResponse? = null
        // Шаг 1: NewGame
        val gameId = "RumblingRun-variation-95"
        val responseNewGameString = newGameService.execute(gameId, seed)
        val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)

//        resultPostProcessor.process("NewGame", newGameResponse)
        var action: String = getFirstAction(responseNewGameString)
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

    fun getFirstAction(jsonString: String): String {

        val jsonNode = objectMapper.readTree(jsonString)

        val result = jsonNode.get("result")

        val action: String? = if (result.has("gameState")) {
            val gameState = result.get("gameState")
            val public = gameState.get("public")
            val actions = public.get("actions")

            actions?.get(0)?.asText()
        } else {
            val public = result.get("public")
            val actions = public.get("actions")

            actions?.get(0)?.asText()
        }

        if (action.isNullOrEmpty()) {
            throw Exception("No actions were found")
        }
        return action
    }
}