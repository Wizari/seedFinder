package com.gmail.wizaripost.seedFinder.dto

data class RegisterRequest(
    val command: String,
    val risk: Boolean,
    val gameState: Any,
    val jackpots: List<JackpotItem>
    )

data class JackpotItem(
    val id: Any,
    val paid: Long
)