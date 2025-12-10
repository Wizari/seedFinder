package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.logging.LoggingService
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import com.gmail.wizaripost.seedFinder.service.processor.utils.MatrixMatcher
import org.springframework.stereotype.Service


/*
* [Tomb Tricks] поиск шаров по матрице
*         /**
         * 9 - любой символ или шар
         * 8 - любой шар
         *
         * 6 - Free
         * 5 - Grand
         * 4 - Major
         * 3 - ?
         * 2 - x10 (Super)
         * 1 - Golden
         * 0 - нет шара
         */
*/

@Service
class MatrixBallsFinder(private val om: ObjectMapper, private val matrixMatcher: MatrixMatcher) : LoggingService(),
    ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }
        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug
        val matrix = resp.result?.gameState?.public?.brilliantSpins?.matrix
        val height = resp.result?.gameState?.public?.dynMatrix?.height?.get(0)

        if (matrix == null || height == null) {
            return
        }

//        val matrixAsString = """
//        00000
//        00000
//        00000
//        00000
//        00000
//        00000
//        00000
//        00000
//    """.trimIndent()

        val matrixAsString = """
        00000
        00000
        00000
        00000
        00000
        00000
        00000
        88888
    """.trimIndent()

        val result = matrixMatcher.findMatrix(
            responseHeight = height,
            responseMatrix = matrix,
            ourHeight = height,
            ourMatrix = matrixAsString  // Можно передать любой из трех форматов!
        )
        if (result) {
            println("001 $seed")
            logSeed("$seed 1x001")
        }


        val matrixAsString2 = """
        00000
        00000
        00000
        00000
        00000
        00000
        88888
        00000
    """.trimIndent()

        if (matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = matrixAsString2
            )
        ) {
            println("010 $seed")
            logSeed("$seed 2x010")

        }

        val matrixAsString3 = """
        00000
        00000
        00000
        00000
        00000
        88888
        00000
        00000
    """.trimIndent()

        if (matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = matrixAsString3
            )
        ) {
            println("000100 $seed")
            logSeed("$seed 3x100")
        }


        if (height > 3 && matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        00000
        00000
        00000
        00000
        88888
        00000
        00000
        00000
    """.trimIndent()
            )
        ) {
            println("1000 $seed")
            logSeed("$seed 4x1000")
        }

        if (height > 4 && matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        00000
        00000
        00000
        88888
        00000
        00000
        00000
        00000
    """.trimIndent()
            )
        ) {
            println("10000 $seed")
            logSeed("$seed 5x10000")
        }

        if (height > 5 && matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        00000
        00000
        88888
        00000
        00000
        00000
        00000
        00000
    """.trimIndent()
            )
        ) {
            println("100000 $seed")
            logSeed("$seed 6x100000")
        }
            if (height > 6 &&  matrixMatcher.findMatrix(
                    responseHeight = height,
                    responseMatrix = matrix,
                    ourHeight = height,
                    ourMatrix = """
        00000
        88888
        00000
        00000
        00000
        00000
        00000
        00000
    """.trimIndent()
                )
            ) {
                println("7x $seed")
                logSeed("$seed 7x")
            }



        if (height > 7 && matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = 8,
                ourMatrix = """
        88888
        00000
        00000
        00000
        00000
        00000
        00000
        00000
    """.trimIndent()
            )
        ) {
            println("8x $seed")
            logSeed("$seed 8x")
        }















//s
        if (matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        80888
        80800
        88888
        00808
        88808
        00000
        00000
        00000
    """.trimIndent()
            )
        ) {
            println("!!!!!!s $seed")
            logSeed("!!!!!!s $seed")
        }
        if (matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        00000
        80888
        80800
        88888
        00808
        88808
        00000
        00000
    """.trimIndent()
            )
        ) {
            println("!!!!!!s $seed")
            logSeed("!!!!!!s $seed")
        }
        if (matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        00000
        00000
        80888
        80800
        88888
        00808
        88808
        00000
    """.trimIndent()
            )
        ) {
            println("!!!!!!s $seed")
            logSeed("!!!!!!s $seed")
        }
        if (matrixMatcher.findMatrix(
                responseHeight = height,
                responseMatrix = matrix,
                ourHeight = height,
                ourMatrix = """
        00000
        00000
        00000
        80888
        80800
        88888
        00808
        88808
    """.trimIndent()
            )
        ) {
            println("!!!!!!s $seed")
            logSeed("!!!!!!s $seed")
        }


    }
}