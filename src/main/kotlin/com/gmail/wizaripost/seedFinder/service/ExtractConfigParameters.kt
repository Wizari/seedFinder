package com.gmail.wizaripost.seedFinder.service

import com.gmail.wizaripost.seedFinder.seedfinder.SeedRunner
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExtractConfigParameters {
    private val logger = LoggerFactory.getLogger(SeedRunner::class.java)


    fun extractTriple(configResult: Map<String, Any>?): Triple<Int, Int, Int> {
        // Извлекаем ModelCore
        val modelCore = configResult?.get("ModelCore") as? Map<String, Any>
        if (modelCore == null) {
//            logger.warn("ModelCore not found in config. Using defaults.")
            return Triple(1, 243, 0)
        }

        // Извлекаем lstLAD (предполагаем, что это список объектов)
        val lstLAD = modelCore["lstLAD"] as? List<*>
        val firstLAD = lstLAD?.firstOrNull() as? Map<String, Any>
        val denomination = firstLAD?.get("dnm") as? Int ?: run {
            logger.warn("Denomination not found in lstLAD[0]. Using default 1.")
            1
        }
        val linesList = firstLAD?.get("lstLA") as? List<*>
        val linesAmount = linesList?.firstOrNull() as? Int ?: run {
            logger.warn("Lines amount not found in lstLAD[0].lstLA. Using default 243.")
            243
        }

        // Извлекаем betTypes (предполагаем, что это список объектов)
        val betTypes = modelCore["betTypes"] as? List<*>
        val firstBetType = betTypes?.firstOrNull() as? Map<String, Any>
        val betTypeId = firstBetType?.get("id") as? Int ?: run {
            logger.warn("Bet type ID not found in betTypes[0]. Using default 0.")
            0
        }

        logger.info("Extracted config: denomination=$denomination, lines=$linesAmount, betType=$betTypeId")
        return Triple(denomination, linesAmount, betTypeId)
    }


}
