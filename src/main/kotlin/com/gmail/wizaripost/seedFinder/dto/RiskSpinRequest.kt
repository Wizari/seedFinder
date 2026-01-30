package com.gmail.wizaripost.seedFinder.dto

data class RiskSpinRequest(
    val command: String,
    val risk: Boolean,
    val gameState: Any,
)