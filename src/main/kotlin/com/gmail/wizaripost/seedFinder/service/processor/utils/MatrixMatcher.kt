package com.gmail.wizaripost.seedFinder.service.processor.utils

import com.gmail.wizaripost.seedFinder.dto.MatrixCell
import org.springframework.stereotype.Service

@Service
class MatrixMatcher {

        /**
         * 9 - любой символ или шар
         * 8 - любой шар
         *
         * 6 - Free
         * 5 - Grand
         * 4 - Major
         * 3 - ?
         * 2 - ?
         * 1 - Golden
         * 0 - нет шара
         * Универсальный парсер для ourMatrix
         * Поддерживает несколько форматов:
         * 1. List<List<Int>> - готовый массив
         * 2. String - многострочное представление
         * 3. Map<Int, List<Int>> - по релам
         */
        private fun parseOurMatrix(input: Any): List<List<Int>> {
            return when (input) {
                is List<*> -> {
                    // Уже готовый List<List<Int>>
                    @Suppress("UNCHECKED_CAST")
                    input as? List<List<Int>> ?: throw IllegalArgumentException("Invalid matrix format")
                }
                is String -> {
                    // Парсинг из строки (удобно для конфигов)
                    parseStringMatrix(input)
                }
                is Map<*, *> -> {
                    // Парсинг из Map<Int, List<Int>>
                    parseMapMatrix(input)
                }
                else -> throw IllegalArgumentException("Unsupported matrix format: ${input::class.simpleName}")
            }
        }

        private fun parseStringMatrix(input: String): List<List<Int>> {
            val lines = input.lines()
                .map { it.trim() }
                .filter { it.isNotBlank() && !it.startsWith("//") }

            if (lines.isEmpty()) return emptyList()

            // Определяем размеры: каждая строка = один ряд
            val rowCount = lines.size
            val reelCount = lines[0].length

            // Создаем матрицу релов x рядов (транспонируем)
            val matrix = MutableList(reelCount) { MutableList(rowCount) { 0 } }

            for (row in 0 until rowCount) {
                val line = lines[row]
                for (reel in 0 until minOf(reelCount, line.length)) {
                    matrix[reel][row] = line[reel].toString().toIntOrNull() ?: 0
                }
            }

            return matrix
        }

        private fun parseMapMatrix(input: Map<*, *>): List<List<Int>> {
            // Ожидается Map<reelIndex, List<values>>
            val maxReel = input.keys.filterIsInstance<Int>().maxOrNull() ?: -1
            if (maxReel < 0) return emptyList()

            val reelCount = maxReel + 1
            val maxRowSize = input.values
                .filterIsInstance<List<*>>()
                .maxOfOrNull { it.size } ?: 0

            val matrix = MutableList(reelCount) { MutableList(maxRowSize) { 0 } }

            input.forEach { (key, value) ->
                if (key is Int && value is List<*>) {
                    val reel = key
                    if (reel < reelCount) {
                        for (row in value.indices) {
                            matrix[reel][row] = (value[row] as? Int) ?: 0
                        }
                    }
                }
            }

            return matrix
        }

        /**
         * Основной метод сравнения матриц
         * @param ourMatrix может быть:
         *   - List<List<Int>>: [[2,0,0,0,0,0,0,0], [2,0,0,0,0,0,0,1], ...]
         *   - String: "22222\n00000\n00000\n01234" (каждый ряд в строке)
         *   - Map<Int, List<Int>>: mapOf(0 to listOf(2,0,0,0,0,0,0,0), ...)
         */
        fun findMatrix(
            responseHeight: Int,
            responseMatrix: List<List<MatrixCell>>,
            ourHeight: Int,
            ourMatrix: Any  // Изменено на Any для поддержки разных форматов
        ): Boolean {
            // Парсим нашу матрицу
            val parsedOurMatrix = try {
                parseOurMatrix(ourMatrix)
            } catch (e: Exception) {
                println("Error parsing matrix: ${e.message}")
                return false
            }

            // Проверяем размеры
            if (ourHeight != responseHeight) {
                return false
            }
            if (parsedOurMatrix.size != responseMatrix.size) {
                println("Error parsing matrix incorrect size: ${parsedOurMatrix.javaClass.simpleName}")
                return false
            }

            // Сравниваем поэлементно
            for (reel in responseMatrix.indices) {
                val responseRow = responseMatrix[reel]
                val ourRow = parsedOurMatrix.getOrNull(reel) ?: return false

                if (responseRow.size != ourRow.size) return false

                for (row in responseRow.indices) {
                    val ourValue = ourRow[row]
                    val responseValue = responseRow[row].id

                    when (ourValue) {
                        9 -> continue  // wildcard - любой символ
                        8 -> {         // маска 1-6
                            if (responseValue !in 1..6) {
                                return false
                            }
                        }
                        else -> {
                            if (ourValue != responseValue) {
                                return false
                            }
                        }
                    }
                }
            }

            return true
        }

        /**
         * Вспомогательный метод для отладки - визуализация матрицы
         */
        fun visualizeMatrix(matrix: Any): String {
            val parsed = parseOurMatrix(matrix)

            return buildString {
                appendLine("Matrix ${parsed.size}x${parsed.firstOrNull()?.size ?: 0}:")
                appendLine("Reels →, Rows ↓")

                // Транспонируем для отображения рядов
                val rowCount = parsed.firstOrNull()?.size ?: 0
                for (row in 0 until rowCount) {
                    for (reel in parsed.indices) {
                        val value = parsed[reel].getOrNull(row) ?: 0
                        append(value)
                    }
                    appendLine("  // row $row")
                }
            }
        }
    }

    // Пример использования
//    fun main() {
//        val matcher = MatrixMatcher()
//
//        // Пример response матрицы
//        val responseMatrix = listOf(
//            listOf(MatrixCell(2), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0)),
//            listOf(MatrixCell(2), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(1)),
//            listOf(MatrixCell(2), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(2)),
//            listOf(MatrixCell(2), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(3)),
//            listOf(MatrixCell(2), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(0), MatrixCell(4))
//        )
//
//        // Три разных способа задать нашу матрицу:
//
//        // 1. Как List<List<Int>>
//        val matrixAsList = listOf(
//            listOf(2, 0, 0, 0, 0, 0, 0, 0),
//            listOf(2, 0, 0, 0, 0, 0, 0, 1),
//            listOf(2, 0, 0, 0, 0, 0, 0, 2),
//            listOf(2, 0, 0, 0, 0, 0, 0, 3),
//            listOf(2, 0, 0, 0, 0, 0, 0, 4)
//        )
//
//        // 2. Как строка (удобно для конфига)
//        val matrixAsString = """
//        22222
//        00000
//        00000
//        00000
//        01234
//    """.trimIndent()
//
//        // 3. Как Map (удобно для JSON)
//        val matrixAsMap = mapOf(
//            0 to listOf(2, 0, 0, 0, 0, 0, 0, 0),
//            1 to listOf(2, 0, 0, 0, 0, 0, 0, 1),
//            2 to listOf(2, 0, 0, 0, 0, 0, 0, 2),
//            3 to listOf(2, 0, 0, 0, 0, 0, 0, 3),
//            4 to listOf(2, 0, 0, 0, 0, 0, 0, 4)
//        )
//
//        // Все три варианта должны дать одинаковый результат
//        println("Matrix as list:")
//        println(matcher.visualizeMatrix(matrixAsList))
//
//        println("Matrix as string:")
//        println(matcher.visualizeMatrix(matrixAsString))
//
//        println("Matrix as map:")
//        println(matcher.visualizeMatrix(matrixAsMap))
//
//        // Проверка совпадения
//        val result = matcher.findMatrix(
//            responseHeight = 8,
//            responseMatrix = responseMatrix,
//            ourHeight = 8,
//            ourMatrix = matrixAsString  // Можно передать любой из трех форматов!
//        )
//
//        println("Matrices match: $result")
//    }
//
//    // Для использования в Spring
//    @Component
//    class MatrixMatchingService {
//        private val matcher = MatrixMatcher()
//
//        fun checkWinPattern(
//            responseMatrix: List<List<MatrixCell>>,
//            pattern: Any  // Любой поддерживаемый формат
//        ): Boolean {
//            return matcher.findMatrix(
//                responseHeight = responseMatrix.firstOrNull()?.size ?: 0,
//                responseMatrix = responseMatrix,
//                ourHeight = 8,  // или вычислять из pattern
//                ourMatrix = pattern
//            )
//        }
//    }
//}