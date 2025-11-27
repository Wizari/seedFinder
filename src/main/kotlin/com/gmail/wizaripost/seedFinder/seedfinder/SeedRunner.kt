package com.gmail.wizaripost.seedFinder.seedfinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.actions.CloseService
import com.gmail.wizaripost.seedFinder.service.actions.FreeSpinService
import com.gmail.wizaripost.seedFinder.service.actions.NewGameService
import com.gmail.wizaripost.seedFinder.service.actions.SpinService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class SeedRunner(
    private val mathClient: MathClient,
    private val newGameService: NewGameService,
    private val spinService: SpinService,
    private val freeSpinService: FreeSpinService,
    private val closeService: CloseService,
    private val resultPostProcessor: ResultPostProcessor

) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    var response: GameResponse? = null

    fun run(seed: ULong) {
        // Шаг 1: NewGame
        val gameId = "RumblingRun-variation-95"
        val responseNewGameString = newGameService.execute(gameId, seed)
        val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)

        resultPostProcessor.process("NewGame", newGameResponse)
        var action: String = getFirstActionWithGson(responseNewGameString)
        logger.info("Executing command: $action")

        // Шаг 2: GetConfig
        val responseGetConfigString = mathClient.getConfig(gameId)
        val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
        resultPostProcessor.process("GetConfig", configResponse)


        do {
            when (action) {
                "Spin" -> {
                    logger.info("Executing command: $action")

                    val responseString = spinService.execute(gameId, newGameResponse)
                    response = objectMapper.readValue(responseString)
                    resultPostProcessor.process("Spin", responseString)

                    action = getFirstActionWithGson(responseString)
                    logger.info("->> NEXT ACTIONS: $action")

                }

                "FreeSpin" -> {
                    logger.info("Executing command: $action")

                    val responseString = freeSpinService.execute(gameId, response)
                    response = objectMapper.readValue(responseString)
                    resultPostProcessor.process("FreeSpin", responseString)

                    action = getFirstActionWithGson(responseString)
                    logger.info("->> NEXT ACTIONS: $action")
                }

                "Close" -> {
                    logger.info("Executing command: $action")

                    val responseString = closeService.execute(gameId, response)
                    response = objectMapper.readValue(responseString)

                    resultPostProcessor.process("Close", responseString)

                    action = getFirstActionWithGson(responseString)
                    logger.info("->> NEXT ACTIONS: $action")

                    // После Close завершаем цикл
                    logger.info("Close command executed, ending game round.")
                }
            }

        } while (action != "Spin")
        logger.info("Game round completed for seed: $seed")
    }

    fun getFirstActionWithGson(jsonString: String): String {
        val objectMapper = ObjectMapper()
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