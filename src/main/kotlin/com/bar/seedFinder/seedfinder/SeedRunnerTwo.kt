package com.bar.seedFinder.seedfinder


import com.bar.seedFinder.client.ContentTypeInterceptor
import com.bar.seedFinder.client.MathClient
import com.bar.seedFinder.dto.FreeSpinRequest
import com.bar.seedFinder.dto.NewGameRequest
import com.bar.seedFinder.dto.SpinRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonObject
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
            var executeResponse = mathClient.newGame(newGameRequest)
            // Выводим ответ в консоль как JSON строку
            println(objectMapper.writeValueAsString(executeResponse))

//            var gameState = newGameResponse.result?.get("gameState") as? Map<String, Any>
            var gameState = objectMapper.writeValueAsString(executeResponse.result)
            println("+++++++++++" + gameState)
//            var actions = getFirstActionWithGson(newGameResponse.result.toString())

            // Шаг 2: GetConfig
            val configResponse = mathClient.getConfig()
            // Выводим ответ в консоль как JSON строку
            println(objectMapper.writeValueAsString(configResponse))

            // Извлекаем начальные параметры из configResponse БОЛЕЕ БЕЗОПАСНО
            val (denomination, linesAmount, betType) = extractConfigParameters(configResponse.result)

//            var actions = extractActions(gameState)
//            var actions = getFirstActionWithGson(newGameResponse.result.toString())
            var actions = getFirstActionWithGson(objectMapper.writeValueAsString(executeResponse))

            println("1111*** " + actions)
            if (actions.isNullOrEmpty()) {
                logger.warn("No initial actions found after NewGame. Exiting loop.")
            } else {
                // Шаг 3: Выполняем действия циклически
                while (!actions.isNullOrEmpty()) {
//                    val command = actions.firstOrNull()
                    val command = actions
                    when (command) {
                        "Spin" -> {
                            logger.info("Executing command: $command")
                            val executeRequest = SpinRequest(
                                command = command,
                                denomination = denomination,
                                lines = linesAmount,
                                lineBet = 2, // или извлекать из gameState, если меняется
                                betType = betType,
                                risk = false, // или извлекать из gameState/config
//                                gameState = gameState,
                                gameState = executeResponse.result!!,
//result                                gameState = objectMapper.writeValueAsString(newGameResponse.result),
                                demoId = -1,
                                demoSeed = -1
                            )
                            executeResponse = mathClient.execute(executeRequest)
                            println(objectMapper.writeValueAsString(executeResponse))
                            actions = getFirstActionWithGson(objectMapper.writeValueAsString(executeResponse))
                            println("NEXT ACTIONS ->>>>>>>>" + actions)
                        }

                        "FreeSpin" -> {
                            logger.info("Executing command: $command")
                            val executeRequest = FreeSpinRequest(
                                command = command,
                                risk = false, // или извлекать из gameState/config
                                gameState = executeResponse!!.result!!["gameState"]!!,
                            )
                            executeResponse = mathClient.execute(executeRequest)
                            // Выводим ответ в консоль как JSON строку
                            println(objectMapper.writeValueAsString(executeResponse))
                            actions = getFirstActionWithGson(objectMapper.writeValueAsString(executeResponse))
                            println("NEXT ACTIONS ->>>>>>>>" + actions)
                        }

                        "Close" -> {
                            logger.info("Executing command: $command")
                            val executeRequest = SpinRequest(
                                command = command,
                                denomination = denomination,
                                lines = linesAmount,
                                lineBet = 2,
                                betType = betType,
                                risk = false,
                                gameState = executeResponse!!.result!!["gameState"]!!,
//                                gameState = objectMapper.writeValueAsString(executeResponse!!.result),
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


    fun getFirstActionWithGson(jsonString: String): String? {
        return try {

            val gson = Gson()
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val result = jsonObject.getAsJsonObject("result")

            if (result.has("gameState")) {

                val gameState = result.getAsJsonObject("gameState")
                val public = gameState.getAsJsonObject("public")
                val actions = public.getAsJsonArray("actions")

                actions?.get(0)?.asString
            } else {

                val public = result.getAsJsonObject("public")
                val actions = public.getAsJsonArray("actions")

                actions?.get(0)?.asString
            }
        } catch (e: Exception) {
            null
        }
    }
}