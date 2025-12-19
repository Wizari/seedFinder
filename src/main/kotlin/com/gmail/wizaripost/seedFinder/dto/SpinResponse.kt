package com.gmail.wizaripost.seedFinder.dto

import com.fasterxml.jackson.annotation.JsonProperty

// Исправленные классы для Cascade
data class Cascade(
    @JsonProperty("stg")
    val stg: List<CascadeStage>? = null
)

data class CascadeStage(
    @JsonProperty("cntnt")
    val cntnt: List<List<Int>>? = null,

    @JsonProperty("wldLr")
    val wldLr: List<WildLayerItem>? = null,

    @JsonProperty("mltSR")
    val mltSR: Int? = null,

    @JsonProperty("rsltS")
    val rsltS: Int? = null,

    @JsonProperty("lstPrz")
    val lstPrz: List<PrzItem>? = null
)

data class WildLayerItem(
    @JsonProperty("rl")
    val rl: Int? = null,

    @JsonProperty("rw")
    val rw: Int? = null
)

data class WildLayer(
    @JsonProperty("wldLr")
    val wldLr: List<WildLayerItem>? = null
)

data class FreeSpinsRiskPublic(
    @JsonProperty("Activity")
    val Activity: Activity? = null,

    @JsonProperty("bnk")
    val bnk: Int? = null
)

data class FreeSpinsRiskPrivate(
    @JsonProperty("trigger")
    val trigger: Int? = null,

    @JsonProperty("seed")
    val seed: Int? = null,

    @JsonProperty("amtRN")
    val amtRN: Int? = null
)

data class SpinTypeCtrl(
    @JsonProperty("lstST")
    val lstST: List<Int>? = null
)

// Обновленный PublicState с правильной структурой Cascade
data class PublicState(
    @JsonProperty("dnm")
    val dnm: Int? = null,

    @JsonProperty("bet")
    val bet: Int? = null,

    @JsonProperty("rsltGC")
    val resultGC: Long? = null,

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
    val matrixMS: Matrix? = null,

    // Существующие дополнительные поля
    @JsonProperty("mtrxBS")
    val matrixBS: Matrix? = null,

    @JsonProperty("controllerReelStrips")
    val controllerReelStrips: ControllerReelStrips? = null,

    @JsonProperty("GmtrPrz")
    val gmtrPrz: GmtrPrz? = null,

    @JsonProperty("ScatteredFeature")
    val scatteredFeature: ScatteredFeature? = null,

    // ИСПРАВЛЕНО: Правильная структура Cascade со stg
    @JsonProperty("Cascade")
    val Cascade: Cascade? = null,

    @JsonProperty("WildLayer")
    val WildLayer: WildLayer? = null,

    @JsonProperty("FreeSpinsRisk")
    val FreeSpinsRisk: FreeSpinsRiskPublic? = null,

    // Ваши существующие новые поля
    @JsonProperty("WildMutipliersFeature")
    val wildMultipliersFeature: WildMutipliersFeature? = null,

    @JsonProperty("FreeSpins")
    val freeSpins: FreeSpinsPublic? = null
)

// Остальные классы остаются такими же, как в вашем исходном коде
data class WildMutipliersFeature(
    @JsonProperty("mlt")
    val multipliers: List<Any>? = null
)

data class FreeSpinsPublic(
    @JsonProperty("Activity")
    val activity: Activity? = null,

    @JsonProperty("amtAddSp")
    val amountAddSpins: Int? = null,

    @JsonProperty("amtTtlSp")
    val amountTotalSpins: Int? = null,

    @JsonProperty("amtRstSp")
    val amountRestSpins: Int? = null,

    @JsonProperty("currSp")
    val currentSpin: Int? = null,

    @JsonProperty("multTtl")
    val multiplierTotal: Int? = null,

    @JsonProperty("multSp")
    val multiplierSpin: Int? = null,

    @JsonProperty("multSpInc")
    val multiplierSpinInc: Int? = null,

    @JsonProperty("totPrz")
    val totalPrize: Int? = null
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

    // НОВОЕ ПОЛЕ ИЗ JSON
    @JsonProperty("isGCRl")
    val isGCRl: Boolean? = null,

    @JsonProperty("seed")
    val seed: Long? = null
)

data class GmtrPrz(
    @JsonProperty("Prize")
    val prize: Int? = null,

    @JsonProperty("lstPrz")
    val lstPrz: List<PrzItem>? = null
)

data class PrzItem(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("amnt")
    val amount: Int? = null,

    @JsonProperty("val")
    val value: Int? = null,

    @JsonProperty("idS")
    val idS: Int? = null,

    @JsonProperty("len")
    val length: Int? = null,

    @JsonProperty("noLn")
    val noLn: Int? = null,

    @JsonProperty("dir")
    val direction: Int? = null,

    @JsonProperty("mask")
    val mask: List<List<Int>>? = null
)

data class ControllerReelStrips(
    @JsonProperty("idS")
    val idS: Int? = null
)

data class ScatteredFeature(
    @JsonProperty("isA")
    val isActive: Boolean? = null,

    @JsonProperty("Prz")
    val prize: Int? = null
)

data class PrivateModelCore(
    @JsonProperty("seed")
    val seed: Long? = null,

    @JsonProperty("amtRN")
    val amountRN: Int? = null,

    @JsonProperty("lmtGCR")
    val limitGCR: Int? = null,

    @JsonProperty("amtGSC")
    val amountGSC: Int? = null
)

data class FreeSpinsPrivate(
    @JsonProperty("initOut")
    val initOut: Int? = null,

    @JsonProperty("subTpSp")
    val subTpSp: Int? = null,

    @JsonProperty("amtGenSp")
    val amountGenSp: Int? = null,

    @JsonProperty("amtMaxSp")
    val amountMaxSp: Int? = null,

    @JsonProperty("minPrz")
    val minPrize: Int? = null,

    @JsonProperty("perc")
    val percentage: Double? = null,

    @JsonProperty("corrSp")
    val corrSp: Int? = null,

    @JsonProperty("isLck")
    val isLock: Boolean? = null
)

data class BrilliantSpins(
    @JsonProperty("idAST")
    val idAST: Int? = null,

    @JsonProperty("actFtr")
    val actFtr: Int? = null,

    @JsonProperty("actPrz")
    val actPrz: Int? = null,

    @JsonProperty("wtSTTA")
    val wtSTTA: Boolean? = null,

    @JsonProperty("listFJps")
    val listFJps: List<Any>? = null,

    @JsonProperty("Activity")
    val activity: Activity? = null,

    @JsonProperty("amtRstSp")
    val amountResetSpins: Int? = null,

    @JsonProperty("totPrz")
    val totalPrize: Int? = null,

    @JsonProperty("lstRdJps")
    val listRoundJackpots: List<JackpotState>? = null,

    @JsonProperty("lstLSpJps")
    val listLastSpinJackpots: List<JackpotState>? = null,

    @JsonProperty("bmbTrgs")
    val bombTriggers: List<BombTrigger>? = null,

    @JsonProperty("trnsf")
    val transfers: List<Transfer>? = null,

    @JsonProperty("cntnt")
    val content: List<List<Int>>? = null,

    @JsonProperty("mtrx")
    val matrix: List<List<MatrixCell>>? = null,

    @JsonProperty("hght")
    val height: Int? = null
)

data class JackpotState(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("paid")
    val paid: Int? = null
)

data class BombTrigger(
    @JsonProperty("rl")
    val row: Int? = null,

    @JsonProperty("rw")
    val column: Int? = null,

    @JsonProperty("trgs")
    val triggers: List<Trigger>? = null
)

data class Trigger(
    @JsonProperty("rl")
    val row: Int? = null,

    @JsonProperty("rw")
    val column: Int? = null
)

data class Transfer(
    @JsonProperty("type")
    val type: Int? = null,

    @JsonProperty("mtrx")
    val matrix: List<List<MatrixCell>>? = null
)

data class MatrixCell(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("grp")
    val group: Int? = null,

    @JsonProperty("val")
    val value: Int? = null
)

data class PrivateJackpots(
    @JsonProperty("trigger")
    val trigger: List<Any>? = null
)

data class Jackpots(
    @JsonProperty("contribute")
    val contribute: List<String>? = null
)

data class PrivateState(
    @JsonProperty("ModelCore")
    val modelCore: PrivateModelCore? = null,

    @JsonProperty("isCompleted")
    val isCompleted: Boolean? = null,

    @JsonProperty("is_prime_state")
    val isPrimeState: Boolean? = null,

    @JsonProperty("FreeSpins")
    val freeSpins: FreeSpinsPrivate? = null,

    @JsonProperty("BrilliantSpins")
    val brilliantSpins: BrilliantSpins? = null,

    @JsonProperty("jackpots")
    val jackpots: PrivateJackpots? = null,

    // НОВЫЕ ПОЛЯ ИЗ JSON
    @JsonProperty("FreeSpinsRisk")
    val FreeSpinsRisk: FreeSpinsRiskPrivate? = null,

    @JsonProperty("SpinTypeCtrl")
    val SpinTypeCtrl: SpinTypeCtrl? = null
)

data class GameStateStage(
    @JsonProperty("public")
    val public: PublicState? = null,

    @JsonProperty("private")
    val `private`: PrivateState? = null,

    @JsonProperty("isComplete")
    val isComplete: Boolean? = null
)

data class Result(
    @JsonProperty("gameState")
    val gameState: GameStateStage? = null,

    @JsonProperty("jackpots")
    val jackpots: Jackpots? = null
)

data class GameStateResponse(
    @JsonProperty("result")
    val result: Result? = null
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










//package com.gmail.wizaripost.seedFinder.dto
//
//import com.fasterxml.jackson.annotation.JsonProperty
//
//// Новые классы для недостающих полей
//data class WildMutipliersFeature(
//    @JsonProperty("mlt")
//    val multipliers: List<Any>? = null
//)
//
//data class FreeSpinsPublic(
//    @JsonProperty("Activity")
//    val activity: Activity? = null,
//
//    @JsonProperty("amtAddSp")
//    val amountAddSpins: Int? = null,
//
//    @JsonProperty("amtTtlSp")
//    val amountTotalSpins: Int? = null,
//
//    @JsonProperty("amtRstSp")
//    val amountRestSpins: Int? = null,
//
//    @JsonProperty("currSp")
//    val currentSpin: Int? = null,
//
//    @JsonProperty("multTtl")
//    val multiplierTotal: Int? = null,
//
//    @JsonProperty("multSp")
//    val multiplierSpin: Int? = null,
//
//    @JsonProperty("multSpInc")
//    val multiplierSpinInc: Int? = null,
//
//    @JsonProperty("totPrz")
//    val totalPrize: Int? = null
//)
//
//// Дополняем PublicState с новыми полями
//data class PublicState(
//    @JsonProperty("dnm")
//    val dnm: Int? = null,
//
//    @JsonProperty("bet")
//    val bet: Int? = null,
//
//    @JsonProperty("rsltGC")
//    val resultGC: Long? = null,
//
//    @JsonProperty("actions")
//    val actions: List<String>? = null,
//
//    @JsonProperty("ModelCore")
//    val modelCore: ModelCore? = null,
//
//    @JsonProperty("BigSymbolFeature")
//    val bigSymbolFeature: BigSymbolFeature? = null,
//
//    @JsonProperty("BrilliantSpins")
//    val brilliantSpins: BrilliantSpins? = null,
//
//    @JsonProperty("dynMtrx")
//    val dynMatrix: DynMatrix? = null,
//
//    @JsonProperty("mtrxMS")
//    val matrixMS: Matrix? = null,
//
//    // Существующие дополнительные поля
//    @JsonProperty("mtrxBS")
//    val matrixBS: Matrix? = null,
//
//    @JsonProperty("controllerReelStrips")
//    val controllerReelStrips: ControllerReelStrips? = null,
//
//    @JsonProperty("GmtrPrz")
//    val gmtrPrz: GmtrPrz? = null,
//
//    @JsonProperty("ScatteredFeature")
//    val scatteredFeature: ScatteredFeature? = null,
//    // Новые поля из JSON
//    @JsonProperty("Cascade")
//    val cascade: List<CascadeItem>? = null,
//
//    @JsonProperty("WildLayer")
//    val wildLayer: WildLayer? = null,
//
//    @JsonProperty("FreeSpinsRisk")
//    val freeSpinsRisk: FreeSpinsRiskPublic? = null,
//
//    // Существующие новые поля
//    @JsonProperty("WildMutipliersFeature")
//    val wildMultipliersFeature: WildMutipliersFeature? = null,
//
//    @JsonProperty("FreeSpins")
//    val freeSpins: FreeSpinsPublic? = null
//    )
//
//// Остальные классы остаются без изменений:
//data class GmtrPrz(
//    @JsonProperty("Prize")
//    val prize: Int? = null,
//
//    @JsonProperty("lstPrz")
//    val lstPrz: List<PrzItem>? = null
//)
//
//data class PrzItem(
//    @JsonProperty("id")
//    val id: Int? = null,
//
//    @JsonProperty("amnt")
//    val amount: Int? = null,
//
//    @JsonProperty("val")
//    val value: Int? = null,
//
//    @JsonProperty("idS")
//    val idS: Int? = null,
//
//    @JsonProperty("len")
//    val length: Int? = null,
//
//    @JsonProperty("noLn")
//    val noLn: Int? = null,
//
//    @JsonProperty("dir")
//    val direction: Int? = null,
//
//    @JsonProperty("mask")
//    val mask: List<List<Int>>? = null
//)
//
//data class ControllerReelStrips(
//    @JsonProperty("idS")
//    val idS: Int? = null
//)
//
//data class ScatteredFeature(
//    @JsonProperty("isA")
//    val isActive: Boolean? = null,
//
//    @JsonProperty("Prz")
//    val prize: Int? = null
//)
//
//data class PrivateModelCore(
//    @JsonProperty("seed")
//    val seed: Long? = null,
//
//    @JsonProperty("amtRN")
//    val amountRN: Int? = null,
//
//    @JsonProperty("lmtGCR")
//    val limitGCR: Int? = null,
//
//    @JsonProperty("amtGSC")
//    val amountGSC: Int? = null
//)
//
//data class FreeSpinsPrivate(
//    @JsonProperty("initOut")
//    val initOut: Int? = null,
//
//    @JsonProperty("subTpSp")
//    val subTpSp: Int? = null,
//
//    @JsonProperty("amtGenSp")
//    val amountGenSp: Int? = null,
//
//    @JsonProperty("amtMaxSp")
//    val amountMaxSp: Int? = null,
//
//    @JsonProperty("minPrz")
//    val minPrize: Int? = null,
//
//    @JsonProperty("perc")
//    val percentage: Double? = null,
//
//    @JsonProperty("corrSp")
//    val corrSp: Int? = null,
//
//    @JsonProperty("isLck")
//    val isLock: Boolean? = null
//)
//
////data class BrilliantSpins(
////    @JsonProperty("listFJps")
////    val listFJps: List<Any>? = null,
//
////    @JsonProperty("idAST")
////    val idAST: Int? = null,
////
////    @JsonProperty("actFtr")
////    val actFtr: Int? = null,
////
////    @JsonProperty("actPrz")
////    val actPrz: Int? = null,
////
////    @JsonProperty("wtSTTA")
////    val wtSTTA: Boolean? = null
////)
//
//data class BrilliantSpins(
//    @JsonProperty("idAST")
//    val idAST: Int? = null,
//
//    @JsonProperty("actFtr")
//    val actFtr: Int? = null,
//
//    @JsonProperty("actPrz")
//    val actPrz: Int? = null,
//
//    @JsonProperty("wtSTTA")
//    val wtSTTA: Boolean? = null,
//
//    @JsonProperty("listFJps")
//    val listFJps: List<Any>? = null,
//
//    @JsonProperty("Activity")
//    val activity: Activity? = null,
//
//    @JsonProperty("amtRstSp")
//    val amountResetSpins: Int? = null,
//
//    @JsonProperty("totPrz")
//    val totalPrize: Int? = null,
//
//    @JsonProperty("lstRdJps")
//    val listRoundJackpots: List<JackpotState>? = null,
//
//    @JsonProperty("lstLSpJps")
//    val listLastSpinJackpots: List<JackpotState>? = null,
//
//    @JsonProperty("bmbTrgs")
//    val bombTriggers: List<BombTrigger>? = null,
//
//    @JsonProperty("trnsf")
//    val transfers: List<Transfer>? = null,
//
//    @JsonProperty("cntnt")
//    val content: List<List<Int>>? = null,
//
//    @JsonProperty("mtrx")
//    val matrix: List<List<MatrixCell>>? = null,
//
//    @JsonProperty("hght")
//    val height: Int? = null
//)
//
//data class JackpotState(
//    @JsonProperty("id")
//    val id: Int? = null,
//
//    @JsonProperty("paid")
//    val paid: Int? = null
//)
//
//data class BombTrigger(
//    @JsonProperty("rl")
//    val row: Int? = null,
//
//    @JsonProperty("rw")
//    val column: Int? = null,
//
//    @JsonProperty("trgs")
//    val triggers: List<Trigger>? = null
//)
//
//data class Trigger(
//    @JsonProperty("rl")
//    val row: Int? = null,
//
//    @JsonProperty("rw")
//    val column: Int? = null
//)
//
//data class Transfer(
//    @JsonProperty("type")
//    val type: Int? = null,
//
//    @JsonProperty("mtrx")
//    val matrix: List<List<MatrixCell>>? = null
//)
//
//data class MatrixCell(
//    @JsonProperty("id")
//    val id: Int? = null,
//
//    @JsonProperty("grp")
//    val group: Int? = null,
//
//    @JsonProperty("val")
//    val value: Int? = null
//)
//
//data class PrivateJackpots(
//    @JsonProperty("trigger")
//    val trigger: List<Any>? = null
//)
//
//data class Jackpots(
//    @JsonProperty("contribute")
//    val contribute: List<String>? = null
//)
//
//// Обновляем PrivateState с правильными типами
//data class PrivateState(
//    @JsonProperty("ModelCore")
//    val modelCore: PrivateModelCore? = null,
//
//    @JsonProperty("isCompleted")
//    val isCompleted: Boolean? = null,
//
//    @JsonProperty("is_prime_state")
//    val isPrimeState: Boolean? = null,
//
//    @JsonProperty("FreeSpins")
//    val freeSpins: FreeSpinsPrivate? = null,
//
//    @JsonProperty("BrilliantSpins")
//    val brilliantSpins: BrilliantSpins? = null,
//
//    @JsonProperty("jackpots")
//    val jackpots: PrivateJackpots? = null,
//
//    // Новое поле из JSON
//    @JsonProperty("FreeSpinsRisk")
//    val freeSpinsRisk: FreeSpinsRiskPrivate? = null,
//
//// Новые поля из JSON
//    @JsonProperty("SpinTypeCtrl")
//    val spinTypeCtrl: SpinTypeCtrl? = null
//)
//
//// Остальные ваши классы остаются без изменений:
//data class GameStateStage(
//    @JsonProperty("public")
//    val public: PublicState? = null,
//
//    @JsonProperty("private")
//    val `private`: PrivateState? = null,
//
//    @JsonProperty("isComplete")
//    val isComplete: Boolean? = null
//)
//
//data class Result(
//    @JsonProperty("gameState")
//    val gameState: GameStateStage? = null,
//
//    @JsonProperty("jackpots")
//    val jackpots: Jackpots? = null
//)
//
//data class GameStateResponse(
//    @JsonProperty("result")
//    val result: Result? = null
//)
//
//
//data class ModelCore(
//    @JsonProperty("dnmAcc")
//    val dnmAcc: Int? = null,
//
//    @JsonProperty("dnmPlr")
//    val dnmPlr: Int? = null,
//
//    @JsonProperty("amntLn")
//    val amountLine: Int? = null,
//
//    @JsonProperty("btLn")
//    val betLine: Int? = null,
//
//    @JsonProperty("btT")
//    val betTotal: Int? = null,
//
//    @JsonProperty("betTtl")
//    val betTotalAmount: Int? = null,
//
//    @JsonProperty("mltSR")
//    val multiplierSR: Int? = null,
//
//    @JsonProperty("rsltS")
//    val resultS: Int? = null,
//
//    @JsonProperty("rsltGC")
//    val resultGC: Int? = null,
//
//    @JsonProperty("idLST")
//    val idLST: Int? = null,
//
//    @JsonProperty("idNST")
//    val idNST: Int? = null,
//
//    @JsonProperty("isGCFd")
//    val isGCFd: Boolean? = null,
//
//    @JsonProperty("isGCRl")
//    val isGCRl: Boolean? = null,
//
//    @JsonProperty("seed")
//    val seed: Long? = null
//)
//
//data class Activity(
//    @JsonProperty("isA")
//    val isActive: Boolean? = null,
//
//    @JsonProperty("isAd")
//    val isActivated: Boolean? = null,
//
//    @JsonProperty("isEA")
//    val isExtraActive: Boolean? = null,
//
//    @JsonProperty("isD")
//    val isDisabled: Boolean? = null
//)
//
//
//data class BigSymbolFeature(
//    @JsonProperty("tlc")
//    val tlc: List<Tlc>? = null
//)
//
//data class Tlc(
//    @JsonProperty("rl")
//    val reel: Int? = null,
//
//    @JsonProperty("rw")
//    val row: Int? = null,
//
//    @JsonProperty("h")
//    val height: Int? = null
//)
//
//data class DynMatrix(
//    @JsonProperty("hght")
//    val height: List<Int>? = null
//)
//
//data class Matrix(
//    @JsonProperty("cntnt")
//    val content: List<List<Int>>? = null,
//
//    @JsonProperty("avbl")
//    val available: List<List<Int>>? = null
//)
//
//data class CascadeItem(
//    @JsonProperty("cntnt")
//    val content: List<List<Int>>? = null,
//
//    @JsonProperty("wldLr")
//    val wildLayers: List<WildLayerItem>? = null,
//
//    @JsonProperty("mltSR")
//    val multiplierSR: Int? = null,
//
//    @JsonProperty("rsltS")
//    val resultS: Int? = null,
//
//    @JsonProperty("lstPrz")
//    val prizeList: List<PrzItem>? = null
//)
//
//
//data class WildLayerItem(
//    @JsonProperty("rl")
//    val row: Int? = null,
//
//    @JsonProperty("rw")
//    val column: Int? = null
//)
//
//data class FreeSpinsRiskPublic(
//    @JsonProperty("Activity")
//    val activity: Activity? = null,
//
//    @JsonProperty("bnk")
//    val bank: Int? = null
//)
//
//data class SpinTypeCtrl(
//    @JsonProperty("lstST")
//    val listST: List<Int>? = null
//)
//
//data class FreeSpinsRiskPrivate(
//    @JsonProperty("trigger")
//    val trigger: Int? = null,
//
//    @JsonProperty("seed")
//    val seed: Int? = null,
//
//    @JsonProperty("amtRN")
//    val amountRN: Int? = null
//)
//
//data class WildLayer(
//    @JsonProperty("wldLr")
//    val wildLayers: List<WildLayerItem>? = null
//)























//package com.gmail.wizaripost.seedFinder.dto
//
//import com.fasterxml.jackson.annotation.JsonProperty
//
//// Новые классы для недостающих полей
//data class WildMutipliersFeature(
//    @JsonProperty("mlt")
//    val multipliers: List<Any>? = null
//)
//
//data class FreeSpinsPublic(
//    @JsonProperty("Activity")
//    val activity: Activity? = null,
//
//    @JsonProperty("amtAddSp")
//    val amountAddSpins: Int? = null,
//
//    @JsonProperty("amtTtlSp")
//    val amountTotalSpins: Int? = null,
//
//    @JsonProperty("amtRstSp")
//    val amountRestSpins: Int? = null,
//
//    @JsonProperty("currSp")
//    val currentSpin: Int? = null,
//
//    @JsonProperty("multTtl")
//    val multiplierTotal: Int? = null,
//
//    @JsonProperty("multSp")
//    val multiplierSpin: Int? = null,
//
//    @JsonProperty("multSpInc")
//    val multiplierSpinInc: Int? = null,
//
//    @JsonProperty("totPrz")
//    val totalPrize: Int? = null
//)
//
//// Дополняем PublicState с новыми полями
//data class PublicState(
//    @JsonProperty("dnm")
//    val dnm: Int? = null,
//
//    @JsonProperty("bet")
//    val bet: Int? = null,
//
//    @JsonProperty("rsltGC")
//    val resultGC: Long? = null,
//
//    @JsonProperty("actions")
//    val actions: List<String>? = null,
//
//    @JsonProperty("ModelCore")
//    val modelCore: ModelCore? = null,
//
//    @JsonProperty("BigSymbolFeature")
//    val bigSymbolFeature: BigSymbolFeature? = null,
//
//    @JsonProperty("BrilliantSpins")
//    val brilliantSpins: BrilliantSpins? = null,
//
//    @JsonProperty("dynMtrx")
//    val dynMatrix: DynMatrix? = null,
//
//    @JsonProperty("mtrxMS")
//    val matrixMS: Matrix? = null,
//
//    // Существующие дополнительные поля
//    @JsonProperty("mtrxBS")
//    val matrixBS: Matrix? = null,
//
//    @JsonProperty("controllerReelStrips")
//    val controllerReelStrips: ControllerReelStrips? = null,
//
//    @JsonProperty("GmtrPrz")
//    val gmtrPrz: GmtrPrz? = null,
//
//    @JsonProperty("ScatteredFeature")
//    val scatteredFeature: ScatteredFeature? = null,
//
//    // Новые поля из второго JSON
//    @JsonProperty("WildMutipliersFeature")
//    val wildMultipliersFeature: WildMutipliersFeature? = null,
//
//    @JsonProperty("FreeSpins")
//    val freeSpins: FreeSpinsPublic? = null
//)
//
//// Остальные классы остаются без изменений:
//data class GmtrPrz(
//    @JsonProperty("Prize")
//    val prize: Int? = null,
//
//    @JsonProperty("lstPrz")
//    val lstPrz: List<PrzItem>? = null
//)
//
//data class PrzItem(
//    @JsonProperty("id")
//    val id: Int? = null,
//
//    @JsonProperty("amnt")
//    val amount: Int? = null,
//
//    @JsonProperty("val")
//    val value: Int? = null,
//
//    @JsonProperty("idS")
//    val idS: Int? = null,
//
//    @JsonProperty("len")
//    val length: Int? = null,
//
//    @JsonProperty("noLn")
//    val noLn: Int? = null,
//
//    @JsonProperty("dir")
//    val direction: Int? = null,
//
//    @JsonProperty("mask")
//    val mask: List<List<Int>>? = null
//)
//
//data class ControllerReelStrips(
//    @JsonProperty("idS")
//    val idS: Int? = null
//)
//
//data class ScatteredFeature(
//    @JsonProperty("isA")
//    val isActive: Boolean? = null,
//
//    @JsonProperty("Prz")
//    val prize: Int? = null
//)
//
//data class PrivateModelCore(
//    @JsonProperty("seed")
//    val seed: Long? = null,
//
//    @JsonProperty("amtRN")
//    val amountRN: Int? = null,
//
//    @JsonProperty("lmtGCR")
//    val limitGCR: Int? = null,
//
//    @JsonProperty("amtGSC")
//    val amountGSC: Int? = null
//)
//
//data class FreeSpinsPrivate(
//    @JsonProperty("initOut")
//    val initOut: Int? = null,
//
//    @JsonProperty("subTpSp")
//    val subTpSp: Int? = null,
//
//    @JsonProperty("amtGenSp")
//    val amountGenSp: Int? = null,
//
//    @JsonProperty("amtMaxSp")
//    val amountMaxSp: Int? = null,
//
//    @JsonProperty("minPrz")
//    val minPrize: Int? = null,
//
//    @JsonProperty("perc")
//    val percentage: Double? = null,
//
//    @JsonProperty("corrSp")
//    val corrSp: Int? = null,
//
//    @JsonProperty("isLck")
//    val isLock: Boolean? = null
//)
//
////data class BrilliantSpins(
////    @JsonProperty("listFJps")
////    val listFJps: List<Any>? = null,
//
////    @JsonProperty("idAST")
////    val idAST: Int? = null,
////
////    @JsonProperty("actFtr")
////    val actFtr: Int? = null,
////
////    @JsonProperty("actPrz")
////    val actPrz: Int? = null,
////
////    @JsonProperty("wtSTTA")
////    val wtSTTA: Boolean? = null
////)
//
//data class BrilliantSpins(
//    @JsonProperty("idAST")
//    val idAST: Int? = null,
//
//    @JsonProperty("actFtr")
//    val actFtr: Int? = null,
//
//    @JsonProperty("actPrz")
//    val actPrz: Int? = null,
//
//    @JsonProperty("wtSTTA")
//    val wtSTTA: Boolean? = null,
//
//    @JsonProperty("listFJps")
//    val listFJps: List<Any>? = null,
//
//    @JsonProperty("Activity")
//    val activity: Activity? = null,
//
//    @JsonProperty("amtRstSp")
//    val amountResetSpins: Int? = null,
//
//    @JsonProperty("totPrz")
//    val totalPrize: Int? = null,
//
//    @JsonProperty("lstRdJps")
//    val listRoundJackpots: List<JackpotState>? = null,
//
//    @JsonProperty("lstLSpJps")
//    val listLastSpinJackpots: List<JackpotState>? = null,
//
//    @JsonProperty("bmbTrgs")
//    val bombTriggers: List<BombTrigger>? = null,
//
//    @JsonProperty("trnsf")
//    val transfers: List<Transfer>? = null,
//
//    @JsonProperty("cntnt")
//    val content: List<List<Int>>? = null,
//
//    @JsonProperty("mtrx")
//    val matrix: List<List<MatrixCell>>? = null,
//
//    @JsonProperty("hght")
//    val height: Int? = null
//)
//
//data class JackpotState(
//    @JsonProperty("id")
//    val id: Int? = null,
//
//    @JsonProperty("paid")
//    val paid: Int? = null
//)
//
//data class BombTrigger(
//    @JsonProperty("rl")
//    val row: Int? = null,
//
//    @JsonProperty("rw")
//    val column: Int? = null,
//
//    @JsonProperty("trgs")
//    val triggers: List<Trigger>? = null
//)
//
//data class Trigger(
//    @JsonProperty("rl")
//    val row: Int? = null,
//
//    @JsonProperty("rw")
//    val column: Int? = null
//)
//
//data class Transfer(
//    @JsonProperty("type")
//    val type: Int? = null,
//
//    @JsonProperty("mtrx")
//    val matrix: List<List<MatrixCell>>? = null
//)
//
//data class MatrixCell(
//    @JsonProperty("id")
//    val id: Int? = null,
//
//    @JsonProperty("grp")
//    val group: Int? = null,
//
//    @JsonProperty("val")
//    val value: Int? = null
//)
//
//data class PrivateJackpots(
//    @JsonProperty("trigger")
//    val trigger: List<Any>? = null
//)
//
//data class Jackpots(
//    @JsonProperty("contribute")
//    val contribute: List<String>? = null
//)
//
//// Обновляем PrivateState с правильными типами
//data class PrivateState(
//    @JsonProperty("ModelCore")
//    val modelCore: PrivateModelCore? = null,
//
//    @JsonProperty("isCompleted")
//    val isCompleted: Boolean? = null,
//
//    @JsonProperty("is_prime_state")
//    val isPrimeState: Boolean? = null,
//
//    @JsonProperty("FreeSpins")
//    val freeSpins: FreeSpinsPrivate? = null,
//
//    @JsonProperty("BrilliantSpins")
//    val brilliantSpins: BrilliantSpins? = null,
//
//    @JsonProperty("jackpots")
//    val jackpots: PrivateJackpots? = null
//)
//
//// Остальные ваши классы остаются без изменений:
//data class GameStateStage(
//    @JsonProperty("public")
//    val public: PublicState? = null,
//
//    @JsonProperty("private")
//    val `private`: PrivateState? = null,
//
//    @JsonProperty("isComplete")
//    val isComplete: Boolean? = null
//)
//
//data class Result(
//    @JsonProperty("gameState")
//    val gameState: GameStateStage? = null,
//
//    @JsonProperty("jackpots")
//    val jackpots: Jackpots? = null
//)
//
//data class GameStateResponse(
//    @JsonProperty("result")
//    val result: Result? = null
//)
//
//
//data class ModelCore(
//    @JsonProperty("dnmAcc")
//    val dnmAcc: Int? = null,
//
//    @JsonProperty("dnmPlr")
//    val dnmPlr: Int? = null,
//
//    @JsonProperty("amntLn")
//    val amountLine: Int? = null,
//
//    @JsonProperty("btLn")
//    val betLine: Int? = null,
//
//    @JsonProperty("btT")
//    val betTotal: Int? = null,
//
//    @JsonProperty("betTtl")
//    val betTotalAmount: Int? = null,
//
//    @JsonProperty("mltSR")
//    val multiplierSR: Int? = null,
//
//    @JsonProperty("rsltS")
//    val resultS: Int? = null,
//
//    @JsonProperty("rsltGC")
//    val resultGC: Int? = null,
//
//    @JsonProperty("idLST")
//    val idLST: Int? = null,
//
//    @JsonProperty("idNST")
//    val idNST: Int? = null,
//
//    @JsonProperty("isGCFd")
//    val isGCFd: Boolean? = null,
//
//    @JsonProperty("isGCRl")
//    val isGCRl: Boolean? = null,
//
//    @JsonProperty("seed")
//    val seed: Long? = null
//)
//
//data class Activity(
//    @JsonProperty("isA")
//    val isActive: Boolean? = null,
//
//    @JsonProperty("isAd")
//    val isActivated: Boolean? = null,
//
//    @JsonProperty("isEA")
//    val isExtraActive: Boolean? = null,
//
//    @JsonProperty("isD")
//    val isDisabled: Boolean? = null
//)
//
//
//data class BigSymbolFeature(
//    @JsonProperty("tlc")
//    val tlc: List<Tlc>? = null
//)
//
//data class Tlc(
//    @JsonProperty("rl")
//    val reel: Int? = null,
//
//    @JsonProperty("rw")
//    val row: Int? = null,
//
//    @JsonProperty("h")
//    val height: Int? = null
//)
//
//data class DynMatrix(
//    @JsonProperty("hght")
//    val height: List<Int>? = null
//)
//
//data class Matrix(
//    @JsonProperty("cntnt")
//    val content: List<List<Int>>? = null,
//
//    @JsonProperty("avbl")
//    val available: List<List<Int>>? = null
//)
