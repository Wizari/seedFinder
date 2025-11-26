package com.bar.seedFinder.dto

data class NewGameRequest(
    val command: String,
    val seed: ULong
)