package com.gmail.wizaripost.seedFinder.dto

data class NewGameRequest(
    val command: String,
    val seed: ULong
)