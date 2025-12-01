package com.gmail.wizaripost.seedFinder


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.seedfinder.SeedRunner
import com.gmail.wizaripost.seedFinder.service.ArgsProcessor
import com.gmail.wizaripost.seedFinder.service.actions.NewGameService
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import kotlin.concurrent.thread


@EnableFeignClients
@SpringBootApplication
class SeedFinderApplication()

fun main(args: Array<String>) {
    val application = runApplication<SeedFinderApplication>(*args);

    val seedRunner = application.getBean<SeedRunner>()
    val argsProcessor = application.getBean<ArgsProcessor>()
    val newGameService = application.getBean<NewGameService>()
    val objectMapper = application.getBean<ObjectMapper>()
    val mathClient = application.getBean<MathClient>()

//    val argsArr : Array<String> = arrayOf("17179869625")
    val argsArr: Array<String> = arrayOf("623927852")
    val seed = argsProcessor.parseFirstArgToULong(argsArr)

//    // Шаг 1: NewGame
    val gameId = "RumblingRun-variation-95"

    // Шаг 2: GetConfig
    val responseGetConfigString = mathClient.getConfig(gameId)
    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
    val gameResponse: GameResponse = objectMapper.readValue(responseGetConfigString)
//        resultPostProcessor.process("GetConfig", configResponse)

//    val first: Long = 0
//    val last: Long = 10_000_000
//
//    for (i in first..last) {
////        ultraFastCounter(10000000)
////        val responseNewGameString = newGameService.execute(gameId, seed)
////        val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)
//
//        seedRunner.run(gameId, i.toULong(), gameResponse)
//    }

    val firstSeed: Long = 0
    val lastSeed: Long = 100_000

    val total = lastSeed - firstSeed + 1
    var currentI = firstSeed // Объявляем как var, чтобы можно было менять изнутри цикла

// Запускаем фоновую задачу для отображения прогресса
    val progressThread = thread(name = "ProgressReporter") {
        while (currentI <= lastSeed -1) {
            val percent = ((currentI - firstSeed) * 100) / (total - 1)
            print("\r[Progress: $percent%] ")
            println()
            Thread.sleep(10000) // Обновляем раз в 10 секунд
        }
    }

// Сам цикл
    for (i in firstSeed..lastSeed) {
        currentI = i // Обновляем общую переменную
        seedRunner.run(gameId, i.toULong(), gameResponse)
    }
}
