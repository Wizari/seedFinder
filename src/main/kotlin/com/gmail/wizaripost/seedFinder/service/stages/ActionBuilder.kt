package com.gmail.wizaripost.seedFinder.service.stages

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class ActionBuilder(
    private val objectMapper: ObjectMapper
) {

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

    fun getLastAction(jsonString: String): String {

        val jsonNode = objectMapper.readTree(jsonString)

        val result = jsonNode.get("result")

        val action: String? = if (result.has("gameState")) {
            val gameState = result.get("gameState")
            val public = gameState.get("public")
            val actions = public.get("actions")

            actions?.last()?.asText()
        } else {
            val public = result.get("public")
            val actions = public.get("actions")

            actions?.last()?.asText()
        }

        if (action.isNullOrEmpty()) {
            throw Exception("No actions were found")
        }
        return action
    }
}