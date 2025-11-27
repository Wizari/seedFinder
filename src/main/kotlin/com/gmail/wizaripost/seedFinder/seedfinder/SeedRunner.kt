package com.gmail.wizaripost.seedFinder.seedfinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.actions.FreeSpinService
import com.gmail.wizaripost.seedFinder.service.actions.NewGameService
import com.gmail.wizaripost.seedFinder.service.actions.SpinService
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class SeedRunner(
    private val mathClient: MathClient,
    private val newGameService: NewGameService,
    private val spinService: SpinService,
    private val freeSpinService: FreeSpinService,

    ) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    var response: String = ""
    var spinResponse: GameResponse? = null

    fun run(seed: ULong) {


        try {
            // Шаг 1: NewGame
            val gameId = "RumblingRun-variation-95"
            response = newGameService.execute(gameId, seed)
            var newGameResponse: GameResponse = objectMapper.readValue(response)

            println("+++++++++++ NewGame.run:" + newGameResponse)

            // Шаг 2: GetConfig

            response = mathClient.getConfig(gameId)
            val configResponse: ConfigResponse = objectMapper.readValue(response)

            // Выводим ответ в консоль как JSON строку
            println(objectMapper.writeValueAsString(configResponse))

            // Извлекаем начальные параметры из configResponse БОЛЕЕ БЕЗОПАСНО
//            val (denomination, linesAmount, betType) = extractConfigParameters(configResponse.result)
//            val configResponse: ConfigResponse = objectMapper.readValue(jsonString)

//            val (denomination, linesAmount, betType) = extractConfigParameters(objectMapper.writeValue(configResponse, ConfigResponse))

            var actions = getFirstActionWithGson(objectMapper.writeValueAsString(newGameResponse))

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

                            response = spinService.execute(gameId, newGameResponse)
                            spinResponse = objectMapper.readValue(response)

                            println(objectMapper.writeValueAsString(spinResponse))
                            actions = getFirstActionWithGson(objectMapper.writeValueAsString(spinResponse))
                            println("NEXT ACTIONS ->>>>>>>>" + actions)
                        }

                        "FreeSpin" -> {
                            logger.info("Executing command: $command")

                            response = freeSpinService.execute(gameId, spinResponse)
                            var gameResponse: GameResponse = objectMapper.readValue(response)

                            println(objectMapper.writeValueAsString(gameResponse))
                            actions = getFirstActionWithGson(objectMapper.writeValueAsString(gameResponse))
                            println("NEXT ACTIONS ->>>>>>>>" + actions)
                        }
//
//                        "Close" -> {
//                            logger.info("Executing command: $command")
//                            val executeRequest = SpinRequest(
//                                command = command,
//                                denomination = denomination,
//                                lines = linesAmount,
//                                lineBet = 2,
//                                betType = betType,
//                                risk = false,
//                                gameState = executeResponse!!.result!!["gameState"]!!,
////                                gameState = objectMapper.writeValueAsString(executeResponse!!.result),
//                                demoId = -1,
//                                demoSeed = -1L
//                            )
//                            val executeResponse = mathClient.executeSpin(executeRequest)
//                            // Выводим ответ в консоль как JSON строку
//                            println(objectMapper.writeValueAsString(executeResponse))
//
//                            // После Close завершаем цикл
//                            logger.info("Close command executed, ending game round.")
//                            break
//                        }

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

//    // ИСПРАВЛЕННАЯ функция извлечения параметров из GetConfig
//    private fun extractConfigParameters(configResult: Map<String, Any>?): Triple<Int, Int, Int> {
//        // Извлекаем ModelCore
//        val modelCore = configResult?.get("ModelCore") as? Map<String, Any>
//        if (modelCore == null) {
//            logger.warn("ModelCore not found in config. Using defaults.")
//            return Triple(1, 243, 0)
//        }
//
//        // Извлекаем lstLAD (предполагаем, что это список объектов)
//        val lstLAD = modelCore["lstLAD"] as? List<*>
//        val firstLAD = lstLAD?.firstOrNull() as? Map<String, Any>
//        val denomination = firstLAD?.get("dnm") as? Int ?: run {
//            logger.warn("Denomination not found in lstLAD[0]. Using default 1.")
//            1
//        }
//        val linesList = firstLAD?.get("lstLA") as? List<*>
//        val linesAmount = linesList?.firstOrNull() as? Int ?: run {
//            logger.warn("Lines amount not found in lstLAD[0].lstLA. Using default 243.")
//            243
//        }
//
//        // Извлекаем betTypes (предполагаем, что это список объектов)
//        val betTypes = modelCore["betTypes"] as? List<*>
//        val firstBetType = betTypes?.firstOrNull() as? Map<String, Any>
//        val betTypeId = firstBetType?.get("id") as? Int ?: run {
//            logger.warn("Bet type ID not found in betTypes[0]. Using default 0.")
//            0
//        }
//
//        logger.info("Extracted config: denomination=$denomination, lines=$linesAmount, betType=$betTypeId")
//        return Triple(denomination, linesAmount, betTypeId)
//    }


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