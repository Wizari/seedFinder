package com.bar.seedFinder.dto

data class ExecuteRequest(
    val command: String,
    val denomination: Int,
    val lines: Int,
    val lineBet: Int,
    val betType: Int,
    val risk: Boolean,
    val gameState: GameState?,
    val demoId: Int,
    val demoSeed: Long
)