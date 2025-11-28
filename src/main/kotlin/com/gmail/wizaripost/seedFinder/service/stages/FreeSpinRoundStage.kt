package com.gmail.wizaripost.seedFinder.service.stages

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.actions.FreeSpinService
import com.gmail.wizaripost.seedFinder.service.actions.SpinService
import org.springframework.stereotype.Service

@Service
class FreeSpinRoundStage(
    private val spinService: FreeSpinService,
    private val objectMapper: ObjectMapper,
    private val resultPostProcessor: ResultPostProcessor,
    private val actionBuilder: ActionBuilder,
    ) : RoundStage {

    override fun valid(action: String): Boolean {
        return action.uppercase() == "FREESPIN"
    }

    override fun  execute(params: Map<String, Any>?): RoundStageResponse {
        if (params == null){
            throw Exception("params can't be null")
        }
        val gameId = params["gameId"] as String
        val payload = params["payload"] as GameResponse
        val responseString = spinService.execute(gameId, payload)
        val response: GameResponse = objectMapper.readValue(responseString)
        resultPostProcessor.process("FreeSpin", responseString)
        val nextAction = actionBuilder.getFirstAction(responseString)
        return RoundStageResponse(nextAction, response)
    }

}