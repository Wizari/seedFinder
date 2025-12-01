package com.gmail.wizaripost.seedFinder.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ConfigResponse(
    @JsonProperty("result")
    val result: ConfigResult? = null
)

data class ConfigResult(
    @JsonProperty("ModelCore")
    val modelCore: ConfigModelCore? = null,

    @JsonProperty("mtrxMS")
    val matrixMS: MatrixMS? = null,

    @JsonProperty("dynMtrx")
    val dynamicMatrix: DynamicMatrix? = null,

    @JsonProperty("controllerReelStrips")
    val controllerReelStrips: List<ControllerReelStrip>? = null,

    @JsonProperty("PrizeTable")
    val prizeTable: List<PrizeTableItem>? = null,

    @JsonProperty("ScatterPrizes")
    val scatterPrizes: List<ScatterPrize>? = null,

    @JsonProperty("Jackpots")
    val jackpots: List<Jackpot>? = null,

    @JsonProperty("Risk")
    val risk: Risk? = null,

    @JsonProperty("rtp")
    val rtp: String? = null,

    @JsonProperty("version")
    val version: String? = null,

    @JsonProperty("modelId")
    val modelId: String? = null,

    @JsonProperty("status")
    val status: String? = null
)

data class ConfigModelCore(
    @JsonProperty("dnmAcc")
    val denominationAccount: Int? = null,

    @JsonProperty("lstSLB")
    val listSLB: List<Int>? = null,

    @JsonProperty("listSPD")
    val listSPD: List<Int>? = null,

    @JsonProperty("lstLAD")
    val listLAD: List<LADItem>? = null,

    @JsonProperty("betTypes")
    val betTypes: List<BetType>? = null,

    @JsonProperty("limitMaxPrize")
    val limitMaxPrize: Int? = null
)

data class LADItem(
    @JsonProperty("dnm")
    val denomination: Int? = null,

    @JsonProperty("lstLA")
    val listLA: List<Int>? = null
)

data class BetType(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("name")
    val name: String? = null,

    @JsonProperty("baseGameBet")
    val baseGameBet: Int? = null
)

data class MatrixMS(
    @JsonProperty("lnsGmtr")
    val linesGeometry: List<Any>? = null,

    @JsonProperty("cntnt")
    val content: List<List<Int>>? = null
)

data class DynamicMatrix(
    @JsonProperty("hght")
    val height: List<Int>? = null
)

data class ControllerReelStrip(
    @JsonProperty("idS")
    val idS: Int? = null,

    @JsonProperty("reelStrips")
    val reelStrips: List<List<Int>>? = null
)

data class PrizeTableItem(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("idSm")
    val idSm: Int? = null,

    @JsonProperty("amSm")
    val amSm: Int? = null,

    @JsonProperty("prz")
    val prize: Int? = null
)

data class ScatterPrize(
    @JsonProperty("amtS")
    val amountScatter: Int? = null,

    @JsonProperty("Prz")
    val prize: Int? = null
)

data class Jackpot(
    @JsonProperty("name")
    val name: String? = null,

    @JsonProperty("linked")
    val linked: String? = null
)

data class Risk(
    @JsonProperty("limitPicks")
    val limitPicks: Int? = null,

    @JsonProperty("limitPrize")
    val limitPrize: Int? = null
)





//data class ConfigResponse(
//        val result: Map<String, Any>? = null // Упрощаем, используем Map
//)
//
//data class ConfigResult(
//    val ModelCore: ModelCoreConfig? = null
//    // Добавьте другие поля конфигурации если нужно
//)
//
//data class ModelCoreConfig(
//    val dnmAcc: Int? = null,
//    val lstSLB: List<Int>? = null,
//    val lstLAD: List<LstLAD>? = null,
//    val betTypes: List<BetType>? = null
//    // Добавьте другие поля если нужно
//)
//
//data class LstLAD(
//    val dnm: Int? = null,
//    val lstLA: List<Int>? = null
//)
//
//data class BetType(
//    val id: Int? = null
//)
