package com.gmail.wizaripost.seedFinder.service.stages

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.actions.RiskSpinService
import org.springframework.stereotype.Service

@Service
class RiskRoundStage(
    private val riskService: RiskSpinService,
    private val objectMapper: ObjectMapper,
    private val resultPostProcessor: ResultPostProcessor,
    private val actionBuilder: ActionBuilder,
    ) : RoundStage {

    override fun valid(action: String): Boolean {
        return action.uppercase() == "GAMBLE"
    }

    override fun  execute(params: Map<String, Any>?): RoundStageResponse {
        if (params == null){
            throw Exception("params can't be null")
        }
        val gameId = params["gameId"] as String
        val payload = params["payload"] as GameResponse
        val responseString = riskService.execute(gameId, payload)
        val response: GameResponse = objectMapper.readValue(responseString)
        resultPostProcessor.process("Gamble", responseString)
//        val nextAction = actionBuilder.getLastAction(responseString)
        val nextAction = actionBuilder.getFirstAction(responseString)
        return RoundStageResponse(nextAction, response)
    }

}