package com.gmail.wizaripost.seedFinder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.dto.GameResponse
import com.gmail.wizaripost.seedFinder.seedfinder.SeedRunner
import com.gmail.wizaripost.seedFinder.service.ArgsProcessor
import com.gmail.wizaripost.seedFinder.service.actions.NewGameService
import com.gmail.wizaripost.seedFinder.service.stages.ActionBuilder
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients


@EnableFeignClients
@SpringBootApplication
class SeedFinderApplication()

fun main(args: Array<String>) {
    val application = runApplication<SeedFinderApplication>(*args);

    val seedRunner = application.getBean<SeedRunner>()
    val argsProcessor = application.getBean<ArgsProcessor>()
    val newGameService= application.getBean<NewGameService>()
    val objectMapper = application.getBean<ObjectMapper>()
    val mathClient = application.getBean<MathClient>()

//    val argsArr : Array<String> = arrayOf("17179869625")
    val argsArr : Array<String> = arrayOf("623927852")
    val seed = argsProcessor.parseFirstArgToULong(argsArr)

    // Шаг 1: NewGame
    val gameId = "RumblingRun-variation-95"
    val responseNewGameString = newGameService.execute(gameId, seed)
    val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)



    // Шаг 2: GetConfig
    val responseGetConfigString = mathClient.getConfig(gameId)
    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
//        resultPostProcessor.process("GetConfig", configResponse)

    for (i in 0..10) {
        val responseNewGameString = newGameService.execute(gameId, seed)
        val newGameResponse: GameResponse = objectMapper.readValue(responseNewGameString)

        seedRunner.run(gameId, newGameResponse)
    }
//    seedRunner.run(args[0].toULong())
}