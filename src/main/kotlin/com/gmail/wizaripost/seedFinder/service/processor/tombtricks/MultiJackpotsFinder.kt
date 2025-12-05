package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service
/*
* [Tomb Tricks] Несколько джекпотов в одном раунде
* 1344747 ДВА ДЖЕКПОТА grand+major
* 2398879 ДВА ДЖЕКПОТА grand X2
* 11946555 ДВА ДЖЕКПОТА grand+major+Super
* 51965917 ДВА ДЖЕКПОТА grand+major в одном спине
52008850 ДВА ДЖЕКПОТА grand+major в одном спине +бомбы на штору
53228175 ДВА ДЖЕКПОТА Баг математики? 2 грандШара тригирнули гранд джекпот (+ Маджор)
62325131 ДВА ДЖЕКПОТА major X2 +super
64079318 ДВА ДЖЕКПОТА grand X2 +super+mega +шар маджора
66313693 ДВА ДЖЕКПОТА major X2 и один шар гранда
66568800 ДВА ДЖЕКПОТА major X2

*/
@Service
class MultiJackpotsFinder(private val om: ObjectMapper) : LoggingService(), ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key == "Close") {
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
        val listFJps = resp.result?.gameState?.private?.brilliantSpins?.listFJps

        if (listFJps == null) {
            return
        }
        if (listFJps.size < 2) {
            return
        }

        val seed = resp.result?.gameState?.private?.modelCore?.seed
        logSeed(seed)
        println(seed)
    }
}