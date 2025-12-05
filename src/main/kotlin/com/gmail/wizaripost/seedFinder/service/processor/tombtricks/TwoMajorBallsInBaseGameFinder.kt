package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] 2 x major и/или 3 x гранд в базовой игре
*/

//@Service
class TwoMajorBallsInBaseGameFinder(private val om: ObjectMapper) : LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
        val matrix = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)
        if (height != 3) {
            return
        }


        if (matrix != null) {
            //Major
            var count = 0
            for (currentReel in matrix) {
                var iterator = 0
                for (symbol in currentReel) {
                    if (iterator > height+1) {
                        if (symbol.id == 4) {
                            count++
                        }
                    }
                    iterator++
                }
            }
            if (count >= 2) {
                log.info("Major: $seed")
                println("Major: $seed")
            }


            //Grand
            count = 0
            for (currentReel in matrix) {
                var iterator = 0
                for (symbol in currentReel) {
                    if (iterator > height+1) {
                        if (symbol.id == 5) {
                            count++
                        }
                    }
                    iterator++
                }
            }
            if (count >= 3) {
                log.info("Grand: $seed")
                println("Grand: $seed")
            }



        }
    }
}
