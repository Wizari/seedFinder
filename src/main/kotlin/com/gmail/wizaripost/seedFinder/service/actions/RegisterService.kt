package com.gmail.wizaripost.seedFinder.service.actions

import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.dto.JackpotItem
import com.gmail.wizaripost.seedFinder.dto.RegisterRequest
import org.springframework.stereotype.Service

@Service
class RegisterService(
    private val mathClient: MathClient,
) {

    fun execute(gameId: String, configResponse: GameResponse?): String {
        val gameState = configResponse!!.result!!["gameState"]!!

        val jackpotsResult = configResponse.result!!["jackpots"] as? Map<*, *>
        val triggerList = jackpotsResult?.get("trigger") as? List<*>

        val jackpotItems = triggerList
            ?.filterIsInstance<String>()
            ?.map { triggerId ->
                JackpotItem(
                    id = triggerId,
                    paid = getPaidForJackpot(triggerId)
                )
            } ?: emptyList()

        val executeRequest = RegisterRequest(
            command = "Register",
            risk = false,
            gameState = gameState,
            jackpots = jackpotItems
        )

        return mathClient.executeRegister(gameId, executeRequest)
    }

    // Функция для определения paid по id
    fun getPaidForJackpot(id: String): Long {
        return when {
            id.contains("Grand") -> 5000000
            id.contains("Major") -> 2500000
            else -> 2503030
        }
    }
}


