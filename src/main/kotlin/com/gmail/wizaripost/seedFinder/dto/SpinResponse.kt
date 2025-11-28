package com.gmail.wizaripost.seedFinder.dto

import com.fasterxml.jackson.annotation.JsonProperty



data class GameStateResponse(
    @JsonProperty("result")
    val result: Result? = null
)

data class Result(
    @JsonProperty("gameState")
    val gameState: GameStateStage? = null
)

data class GameStateStage(
    @JsonProperty("public")
    val public: PublicState? = null,

    @JsonProperty("private")
    val `private`: PrivateState? = null,

    @JsonProperty("isComplete")
    val isComplete: Boolean? = null
)

data class PublicState(
    @JsonProperty("dnm")
    val dnm: Int? = null,

    @JsonProperty("bet")
    val bet: Int? = null,

    @JsonProperty("rsltGC")
    val resultGC: Int? = null,

    @JsonProperty("actions")
    val actions: List<String>? = null,

    @JsonProperty("ModelCore")
    val modelCore: ModelCore? = null,

    @JsonProperty("BigSymbolFeature")
    val bigSymbolFeature: BigSymbolFeature? = null,

    @JsonProperty("BrilliantSpins")
    val brilliantSpins: BrilliantSpins? = null,

    @JsonProperty("dynMtrx")
    val dynMatrix: DynMatrix? = null,

    @JsonProperty("mtrxMS")
    val matrixMS: Matrix? = null
)

data class PrivateState(

    @JsonProperty("ModelCore")
    val modelCore: ModelCore? = null,

    @JsonProperty("isCompleted")
    val isCompleted: Boolean? = null,

    @JsonProperty("is_prime_state")
    val isPrimeState: Boolean? = null
)

data class ModelCore(
    @JsonProperty("dnmAcc")
    val dnmAcc: Int? = null,

    @JsonProperty("dnmPlr")
    val dnmPlr: Int? = null,

    @JsonProperty("amntLn")
    val amountLine: Int? = null,

    @JsonProperty("btLn")
    val betLine: Int? = null,

    @JsonProperty("btT")
    val betTotal: Int? = null,

    @JsonProperty("betTtl")
    val betTotalAmount: Int? = null,

    @JsonProperty("mltSR")
    val multiplierSR: Int? = null,

    @JsonProperty("rsltS")
    val resultS: Int? = null,

    @JsonProperty("rsltGC")
    val resultGC: Int? = null,

    @JsonProperty("idLST")
    val idLST: Int? = null,

    @JsonProperty("idNST")
    val idNST: Int? = null,

    @JsonProperty("isGCFd")
    val isGCFd: Boolean? = null,

    @JsonProperty("isGCRl")
    val isGCRl: Boolean? = null,

    @JsonProperty("seed")
    val seed: Long? = null
)

data class BigSymbolFeature(
    @JsonProperty("tlc")
    val tlc: List<Tlc>? = null
)

data class Tlc(
    @JsonProperty("rl")
    val reel: Int? = null,

    @JsonProperty("rw")
    val row: Int? = null,

    @JsonProperty("h")
    val height: Int? = null
)

data class BrilliantSpins(
    @JsonProperty("Activity")
    val activity: Activity? = null,

    @JsonProperty("amtRstSp")
    val amountRestSpins: Int? = null,

    @JsonProperty("totPrz")
    val totalPrize: Int? = null,

    @JsonProperty("lstRdJps")
    val listRoundJumps: List<Any>? = null,

    @JsonProperty("lstLSpJps")
    val listLastSpinJumps: List<Any>? = null,

    @JsonProperty("bmbTrgs")
    val bombTriggers: List<Any>? = null,

    @JsonProperty("trnsf")
    val transfers: List<Any>? = null,

    @JsonProperty("mtrx")
    val matrix: List<List<MatrixItem>>? = null,

    @JsonProperty("hght")
    val height: Int? = null
)

data class Activity(
    @JsonProperty("isA")
    val isActive: Boolean? = null,

    @JsonProperty("isAd")
    val isActivated: Boolean? = null,

    @JsonProperty("isEA")
    val isExtraActive: Boolean? = null,

    @JsonProperty("isD")
    val isDisabled: Boolean? = null
)

data class MatrixItem(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("grp")
    val group: Int? = null,

    @JsonProperty("val")
    val value: Int? = null
)

data class DynMatrix(
    @JsonProperty("hght")
    val height: List<Int>? = null
)

data class Matrix(
    @JsonProperty("cntnt")
    val content: List<List<Int>>? = null,

    @JsonProperty("avbl")
    val available: List<List<Int>>? = null
)