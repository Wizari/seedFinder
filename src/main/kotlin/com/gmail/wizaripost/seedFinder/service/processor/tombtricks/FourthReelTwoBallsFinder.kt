package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service


/*
* [Tomb Tricks]
*/

//@Service
class FourthReelTwoBallsFinder(private val om: ObjectMapper) : LoggingService(), ResultPostProcessor {
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
            var count = 0
            var firstTrigger = false

            for (i in 0..2) {  // только индексы 0,1,2,3
                val currentReel = matrix.get(i)
                var iterator = 0
                for (symbol in currentReel) {
                    if (iterator > 4) {
                        if (symbol.id == 6 || symbol.id == 1) {
                            count++
                            firstTrigger = true
                        }
                    }
                    iterator++
                }
            }
            var trigger = false
            var iterator = 0
            for (symbol in matrix[3]) {
                if (iterator > 4) {
                    if (symbol.id == 6 || symbol.id == 1) {
                        count++
                        trigger = true
                    }
                }
                iterator++
            }


            var lastReelTrigger = false
            for (symbol in matrix[4]) {
                if (iterator > 4) {
                    if (symbol.id == 6 || symbol.id == 1) {
                        lastReelTrigger = true
                    }
                }
                iterator++
            }
            if (count == 0) {
                return
            }
            if (count > 2) {
                return
            }
            if (trigger && firstTrigger && lastReelTrigger) {
                println(seed)
            }
        }
    }
}
