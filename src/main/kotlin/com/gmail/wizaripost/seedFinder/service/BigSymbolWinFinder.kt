package com.gmail.wizaripost.seedFinder.service


//@Service
//class ResultPostProcessorImpl(private val om: ObjectMapper) : ResultPostProcessor {
//    override fun process(key: String, payload: Any) {
//        if (key != "Spin") {
//            return
//        }
//        val resp: GameStateResponse = om.readValue(payload as String)
//        val row = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()?.row
//        val reel = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()?.reel
//        val capHeight = resp.result?.gameState?.public?.dynMatrix?.height?.firstOrNull() ?: throw RuntimeException("Required cap height")
//        if (row == capHeight + 1){
//            println("Найден Seed ${resp.result.gameState.private?.modelCore?.seed}")
//        }
////        println(payload)
//    }
//}