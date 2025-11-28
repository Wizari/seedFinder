package com.gmail.wizaripost.seedFinder.service.processor.tombtricks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.dto.GameStateResponse
import com.gmail.wizaripost.seedFinder.dto.PrzItem
import com.gmail.wizaripost.seedFinder.service.processor.ResultPostProcessor
import org.springframework.stereotype.Service

@Service
class BigSymbolWinFinder(private val om: ObjectMapper) : ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        if (key != "Spin") {
            return
        }

        val resp: GameStateResponse = om.readValue(payload as String)
        val seed = resp.result?.gameState?.private?.modelCore?.seed //TODO debug

        // Получаем данные о большом символе
        val bigSymbol = resp.result?.gameState?.public?.bigSymbolFeature?.tlc?.firstOrNull()
        val bigSymbols = resp.result?.gameState?.public?.bigSymbolFeature?.tlc ?: emptyList()

        for (bigSymbol in bigSymbols) {
            if (bigSymbol.row == 0 || bigSymbol.reel == 0) {
                return
            }
        }

        val matrix = resp.result?.gameState?.public?.matrixMS?.content

        if (matrix != null) {
            val firstReel = matrix[0]

            for (element in firstReel) {
                if (element == 1) {
                    return
                }
            }
        }

        val bigSymbolRow = bigSymbol?.row
        val bigSymbolReel = bigSymbol?.reel

        // Получаем выигрышные комбинации и матрицу
        val lstPrz = resp.result?.gameState?.public?.gmtrPrz?.lstPrz


            ?: throw RuntimeException("Required cap height")
        if (bigSymbolRow != 0 && bigSymbolReel != 0 && bigSymbolRow != null && bigSymbolReel != null && lstPrz != null && matrix != null) {
            // Создаем объединенную маску из всех выигрышных комбинаций
            val combinedMask = createCombinedMask(lstPrz, matrix.size, matrix[0].size)

            // Проверяем, есть ли выигрышные символы прямо под большим символом 2x2
            if (hasWinningSymbolsUnderBigSymbol(matrix, combinedMask, bigSymbolRow, bigSymbolReel, seed)) {
                println(resp.result.gameState.private?.modelCore?.seed)
            }
        }
    }

    private fun createCombinedMask(lstPrz: List<PrzItem>, rows: Int, cols: Int): Array<IntArray> {
        // Создаем пустую маску
        val combinedMask = Array(rows) { IntArray(cols) }

        // Объединяем все маски из lstPrz
        for (przItem in lstPrz) {
            val mask = przItem.mask ?: continue
            for (r in mask.indices) {
                for (c in mask[r].indices) {
                    if (r < rows && c < cols && mask[r][c] == 1) {
                        combinedMask[r][c] = 1
                    }
                }
            }
        }
        return combinedMask
    }

    private fun hasWinningSymbolsUnderBigSymbol(
        matrix: List<List<Int>>,
        combinedMask: Array<IntArray>,
        bigSymbolRow: Int,
        bigSymbolReel: Int,
        seed: Long?
    ): Boolean {
        val positionsUnderBigSymbol = listOf(
            Pair(bigSymbolRow, bigSymbolReel),
            Pair(bigSymbolRow, bigSymbolReel + 1),
            Pair(bigSymbolRow + 1, bigSymbolReel),
            Pair(bigSymbolRow + 1, bigSymbolReel + 1)
        )

        // Проверяем каждую позицию под большим символом
        for ((row, col) in positionsUnderBigSymbol) {
            // Проверяем границы матрицы
            if (row < matrix.size && col < matrix[row].size) {
                // Если в объединенной маске эта позиция отмечена как выигрышная
                if (combinedMask[col][row] == 1) {
                    return true
                }
            }
        }

        return false
    }
}