package com.gmail.wizaripost.seedFinder.dto


// Используется как для NewGame, так и для Execute
data class GameResponse(
    val result: Map<String, Any>? = null // Упрощаем, используем Map
)

data class GameResult(
    val gameState: GameState? = null
)

data class GameState(
    val `public`: PublicGameState? = null,
    val private: PrivateGameState? = null // Или другие поля приватного состояния
)

data class PublicGameState(
    val actions: List<String>? = null,
    val ModelCore: ModelCorePublic? = null
    // Добавьте другие поля public gameState если нужно
)

data class ModelCorePublic(
    val dnmAcc: Int? = null,
    val dnmPlr: Int? = null,
    val amntLn: Int? = null,
    val btLn: Int? = null,
    val btT: Int? = null,
    val betTtl: Int? = null,
    val mltSR: Int? = null,
    val rsltS: Int? = null,
    val rsltGC: Int? = null,
    val idLST: Int? = null,
    val idNST: Int? = null,
    val isGCFd: Boolean? = null,
    val isGCRl: Boolean? = null
    // Добавьте другие поля ModelCore если нужно
)

data class PrivateGameState(
    val ModelCore: ModelCorePrivate? = null
    // Добавьте другие поля private gameState если нужно
)

data class ModelCorePrivate(
    val seed: Long? = null,
    val amtRN: Int? = null,
    val lmtGCR: Int? = null,
    val amtGSC: Int? = null
    // Добавьте другие поля ModelCore если нужно
)
