package com.gmail.wizaripost.seedFinder.service.stages

interface RoundStage{

    fun valid(action: String): Boolean

    fun execute(params: Map<String, Any>?): RoundStageResponse

}