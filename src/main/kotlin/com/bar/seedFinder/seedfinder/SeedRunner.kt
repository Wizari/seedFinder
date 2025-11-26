package com.bar.seedFinder.seedfinder

import com.bar.seedFinder.client.MathClient
import com.bar.seedFinder.dto.ExecuteRequest
import com.bar.seedFinder.dto.GameState
import com.bar.seedFinder.dto.NewGameRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SeedRunner(
    private val mathClient: MathClient
) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            logger.error("No seed provided. Usage: java -jar seedFinder-0.0.1-SNAPSHOT.jar <seed>")
            return
        }

        val seedArg = args[0]
        val seed: ULong = try {
            seedArg.toULong()
        } catch (e: NumberFormatException) {
            logger.error("Invalid seed format: $seedArg. Seed must be a valid unsigned long.")
            return
        }

        logger.info("Starting game round with seed: $seed")

        try {
            // Шаг 1: NewGame
            val newGameRequest = NewGameRequest("NewGame", seed)
            logger.info("Sending NewGame request: {}", objectMapper.writeValueAsString(newGameRequest))

            val newGameResponse = mathClient.newGame(newGameRequest)
            println("******************")
            println("Raw NewGame response: $newGameResponse")
            logger.info("NewGame response: {}", objectMapper.writeValueAsString(newGameResponse))

            // Проверяем структуру ответа
            if (newGameResponse.result == null) {
                logger.error("Result is null in NewGame response")
                return
            }

            if (newGameResponse.result.gameState == null) {
                logger.warn("GameState is null in NewGame response. Checking for error...")
                // Добавим проверку на наличие ошибок
                logger.info("Full response structure: {}", newGameResponse)
            }

            // Шаг 2: GetConfig
            val configResponse = mathClient.getConfig()
            logger.info("GetConfig response: {}", objectMapper.writeValueAsString(configResponse))

            // Если gameState null, не продолжаем
            val gameState = newGameResponse.result.gameState
            if (gameState == null) {
                logger.error("Initial gameState is null after NewGame. Cannot proceed.")
                logger.info("Available result fields: ${newGameResponse.result::class.java.declaredFields.map { it.name }}")
                return
            }

            // Продолжаем выполнение...
            proceedWithGame(gameState)

        } catch (e: Exception) {
            logger.error("Error running game round with seed $seed: ${e.message}", e)
        }
    }

    private fun proceedWithGame(initialGameState: GameState) {
        var gameState = initialGameState
        var actions = initialGameState.public?.actions ?: emptyList()

        logger.info("Initial actions: $actions")

        // Шаг 3: Выполняем действия циклически
        while (actions.isNotEmpty()) {
            val command = actions.first()
            when (command) {
                "Spin", "FreeSpin", "Close" -> {
                    logger.info("Executing command: $command")

                    val executeRequest = ExecuteRequest(
                        command = command,
                        denomination = 1,
                        lines = 243,
                        lineBet = 2,
                        betType = 0,
                        risk = false,
                        gameState = gameState,
                        demoId = -1,
                        demoSeed = -1L
                    )

                    logger.info("Sending Execute request: {}", objectMapper.writeValueAsString(executeRequest))
                    val executeResponse = mathClient.execute(executeRequest)
                    logger.info("$command response: {}", objectMapper.writeValueAsString(executeResponse))

                    // Обновляем gameState и actions для следующей итерации
                    gameState = executeResponse.result?.gameState ?: break
                    actions = executeResponse.result.gameState.public?.actions ?: emptyList()

                    logger.info("Updated actions: $actions")
                }

                else -> {
                    logger.warn("Unknown action: $command. Exiting loop.")
                    break
                }
            }
        }

        logger.info("Game round completed")
    }

    fun sayHello() {
        println("Hello World!")
    }
}