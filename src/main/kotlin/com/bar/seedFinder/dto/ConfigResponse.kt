package com.bar.seedFinder.dto

data class ConfigResponse(
    val result: Map<String, Any>? = null // Упрощаем, используем Map
)

data class ConfigResult(
    val ModelCore: ModelCoreConfig? = null
    // Добавьте другие поля конфигурации если нужно
)

data class ModelCoreConfig(
    val dnmAcc: Int? = null,
    val lstSLB: List<Int>? = null,
    val lstLAD: List<LstLAD>? = null,
    val betTypes: List<BetType>? = null
    // Добавьте другие поля если нужно
)

data class LstLAD(
    val dnm: Int? = null,
    val lstLA: List<Int>? = null
)

data class BetType(
    val id: Int? = null
)
