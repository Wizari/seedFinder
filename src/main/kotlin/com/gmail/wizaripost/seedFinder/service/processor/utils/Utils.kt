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
}

