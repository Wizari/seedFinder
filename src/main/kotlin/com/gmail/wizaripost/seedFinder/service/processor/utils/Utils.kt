package com.gmail.wizaripost.seedFinder.service.processor.utils

import com.gmail.wizaripost.seedFinder.dto.MatrixCell
import org.springframework.stereotype.Service

@Service
class Utils() {

    fun getVisibleMatrix(height: Int, matrix: List<List<MatrixCell>>): List<List<MatrixCell>> {
        val rows = 5
        val visibleHeight = height

        val newMatrix = MutableList(rows) { mutableListOf<MatrixCell>() }

        for (reel in 0 until rows) {
            for (offset in 0 until visibleHeight) {
                // Вычисляем индекс строки, начиная с конца
                val rowIndex = matrix[reel].size - visibleHeight + offset

                if (rowIndex >= 0 && rowIndex < matrix[reel].size) {
                    newMatrix[reel].add(matrix[reel][rowIndex])
                } else {
                    newMatrix[reel].add(MatrixCell())
                }
            }
        }
        return newMatrix
    }
//
//    fun findMatrix(
//        responseHeight: Int,
//        responseMatrix: List<List<MatrixCell>>,
//        ourHeight: Int,
//        ourMatrix: List<List<Int>>
//    ): Boolean {
//        if (ourHeight != responseHeight || ourMatrix.size != responseMatrix.size) {
//            return false
//        }
//        for (reel in 0 until responseMatrix.size) {
//            for (row in 0 until responseMatrix[reel].size) {
//                if (9 != ourMatrix[reel][row]) {
//
//                } else
//                    if (responseMatrix[reel][row].id != ourMatrix[reel][row]) {
//                        return false
//                    }
//            }
//
//
//        }
//        return true
//
//    }
}

