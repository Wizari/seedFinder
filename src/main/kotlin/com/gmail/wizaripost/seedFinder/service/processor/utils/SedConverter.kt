package com.gmail.wizaripost.seedFinder.service.processor.utils

import org.springframework.stereotype.Service

@Service
class SedConverter {

    fun toULong(highSeed: Long, lowSeed: Long): ULong {
        val high = (highSeed shl 32).toULong()
        val low = lowSeed.toULong() and 0xffffffffU
        val seed: ULong = high or low
        return seed
    }

    fun toULong(seed: Long): ULong {
        val high = (seed shl 32).toULong()
        val low = seed.toULong() and 0xffffffffU
        val seed: ULong = high or low
        return seed
    }
}