package com.bar.seedFinder.seedfinder


import com.bar.seedFinder.client.ContentTypeInterceptor
import com.bar.seedFinder.client.MathClient
import com.bar.seedFinder.dto.ExecuteRequest
import com.bar.seedFinder.dto.NewGameRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SeedRunnerTwo(
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
            val newGameHeaders =
                mapOf(ContentTypeInterceptor.CUSTOM_CONTENT_TYPE_HEADER to ContentTypeInterceptor.VND_API_JSON)
            val newGameResponse = mathClient.newGame(newGameRequest)
            // Выводим ответ в консоль как JSON строку
            println(objectMapper.writeValueAsString(newGameResponse))

            var gameState = newGameResponse.result?.get("actions") as? Map<String, Any>
//            if (gameState == null) {
//                logger.error("Initial gameState is null after NewGame. Exiting.")
//                return
//            }

            // Шаг 2: GetConfig
            val configResponse = mathClient.getConfig()
            // Выводим ответ в консоль как JSON строку
            println(objectMapper.writeValueAsString(configResponse))

            // Извлекаем начальные параметры из configResponse БОЛЕЕ БЕЗОПАСНО
            val (denomination, linesAmount, betType) = extractConfigParameters(configResponse.result)

            var actions = extractActions(gameState)
            if (actions.isNullOrEmpty()) {
                logger.warn("No initial actions found after NewGame. Exiting loop.")
            } else {
                // Шаг 3: Выполняем действия циклически
                while (!actions.isNullOrEmpty()) {
                    val command = actions.firstOrNull()
                    when (command) {
                        "Spin", "FreeSpin" -> {
                            logger.info("Executing command: $command")
                            val executeRequest = ExecuteRequest(
                                command = command,
                                denomination = denomination,
                                lines = linesAmount,
                                lineBet = 2, // или извлекать из gameState, если меняется
                                betType = betType,
                                risk = false, // или извлекать из gameState/config
                                gameState = gameState,
                                demoId = -1,
                                demoSeed = -1L
                            )
                            val executeResponse = mathClient.execute(executeRequest)
                            // Выводим ответ в консоль как JSON строку
                            println(objectMapper.writeValueAsString(executeResponse))

                            // Обновляем gameState и actions для следующей итерации
                            gameState = executeResponse.result?.get("gameState") as? Map<String, Any>
                            actions = extractActions(gameState)

                        }

                        "Close" -> {
                            logger.info("Executing command: $command")
                            val executeRequest = ExecuteRequest(
                                command = command,
                                denomination = denomination,
                                lines = linesAmount,
                                lineBet = 2,
                                betType = betType,
                                risk = false,
                                gameState = gameState,
                                demoId = -1,
                                demoSeed = -1L
                            )
                            val executeResponse = mathClient.execute(executeRequest)
                            // Выводим ответ в консоль как JSON строку
                            println(objectMapper.writeValueAsString(executeResponse))

                            // После Close завершаем цикл
                            logger.info("Close command executed, ending game round.")
                            break
                        }

                        else -> {
                            logger.warn("Unknown action: $command. Exiting loop.")
                            break
                        }
                    }
                }
            }

            logger.info("Game round completed for seed: $seed")

        } catch (e: Exception) {
            logger.error("Error running game round with seed $seed: ${e.message}", e)
        }
    }

    // Вспомогательная функция для извлечения actions из gameState
    private fun extractActions(gameState: Map<String, Any>?): List<String>? {
        val gameStatePublic = gameState?.get("public") as? Map<String, Any>
        return gameStatePublic?.get("actions") as? List<String>
    }

    // ИСПРАВЛЕННАЯ функция извлечения параметров из GetConfig
    private fun extractConfigParameters(configResult: Map<String, Any>?): Triple<Int, Int, Int> {
        // Извлекаем ModelCore
        val modelCore = configResult?.get("ModelCore") as? Map<String, Any>
        if (modelCore == null) {
            logger.warn("ModelCore not found in config. Using defaults.")
            return Triple(1, 243, 0)
        }

        // Извлекаем lstLAD (предполагаем, что это список объектов)
        val lstLAD = modelCore["lstLAD"] as? List<*>
        val firstLAD = lstLAD?.firstOrNull() as? Map<String, Any>
        val denomination = firstLAD?.get("dnm") as? Int ?: run {
            logger.warn("Denomination not found in lstLAD[0]. Using default 1.")
            1
        }
        val linesList = firstLAD?.get("lstLA") as? List<*>
        val linesAmount = linesList?.firstOrNull() as? Int ?: run {
            logger.warn("Lines amount not found in lstLAD[0].lstLA. Using default 243.")
            243
        }

        // Извлекаем betTypes (предполагаем, что это список объектов)
        val betTypes = modelCore["betTypes"] as? List<*>
        val firstBetType = betTypes?.firstOrNull() as? Map<String, Any>
        val betTypeId = firstBetType?.get("id") as? Int ?: run {
            logger.warn("Bet type ID not found in betTypes[0]. Using default 0.")
            0
        }

        logger.info("Extracted config: denomination=$denomination, lines=$linesAmount, betType=$betTypeId")
        return Triple(denomination, linesAmount, betTypeId)
    }
}