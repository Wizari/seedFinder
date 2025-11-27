package com.gmail.wizaripost.seedFinder.seedfinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.actions.CloseService
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
    private val closeService: CloseService,

    ) {

    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    var response: GameResponse? = null

    fun run(seed: ULong) {
        // Шаг 1: NewGame
        val gameId = "RumblingRun-variation-95"
        val responseNewGameString = newGameService.execute(gameId, seed)
        val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)

        println("+++++++++++ NewGame.run:" + newGameResponse)

        var action: String = getFirstActionWithGson(responseNewGameString)
        println("1111*** " + action)

        // Шаг 2: GetConfig
        val responseGetConfigString = mathClient.getConfig(gameId)
        val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)

        // Выводим ответ в консоль как JSON строку
        println(objectMapper.writeValueAsString(configResponse))

        do {
            when (action) {
                "Spin" -> {
                    logger.info("Executing command: $action")

                    val responseString = spinService.execute(gameId, newGameResponse)
                    response = objectMapper.readValue(responseString)

                    println(objectMapper.writeValueAsString(response))
//                        action = getFirstActionWithGson(objectMapper.writeValueAsString(response))
                    action = getFirstActionWithGson(responseString)
                    println("NEXT ACTIONS ->>>>>>>>" + action)
                }

                "FreeSpin" -> {
                    logger.info("Executing command: $action")

                    val responseString = freeSpinService.execute(gameId, response)
                    response = objectMapper.readValue(responseString)

                    println(objectMapper.writeValueAsString(response))
                    action = getFirstActionWithGson(objectMapper.writeValueAsString(response))
                    println("NEXT ACTIONS ->>>>>>>>" + action)
                }

                "Close" -> {
                    logger.info("Executing command: $action")

                    val responseString = closeService.execute(gameId, response)
                    response = objectMapper.readValue(responseString)

                    println(objectMapper.writeValueAsString(response))
                    action = getFirstActionWithGson(objectMapper.writeValueAsString(response))
                    println("NEXT ACTIONS ->>>>>>>>" + action)

                    // После Close завершаем цикл
                    logger.info("Close command executed, ending game round.")
                }
            }

        } while (action != "Spin")
        logger.info("Game round completed for seed: $seed")
    }

    fun getFirstActionWithGson(jsonString: String): String {

        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        val result = jsonObject.getAsJsonObject("result")

        val action: String? = if (result.has("gameState")) {

            val gameState = result.getAsJsonObject("gameState")
            val public = gameState.getAsJsonObject("public")
            val actions = public.getAsJsonArray("actions")

            actions?.get(0)?.asString
        } else {

            val public = result.getAsJsonObject("public")
            val actions = public.getAsJsonArray("actions")

            actions?.get(0)?.asString
        }
        if (action.isNullOrEmpty()) {
            throw Exception("No actions were found")
        }
        return action
    }
}