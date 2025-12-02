package com.gmail.wizaripost.seedFinder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.seedfinder.SeedRunner
import com.gmail.wizaripost.seedFinder.service.ArgsProcessor
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import java.util.concurrent.atomic.AtomicLong


@EnableFeignClients
@SpringBootApplication
class SeedFinderApplication

fun main(args: Array<String>) {
    val application = runApplication<SeedFinderApplication>(*args)

    val seedRunner = application.getBean<SeedRunner>()
    val argsProcessor = application.getBean<ArgsProcessor>()
    val objectMapper = application.getBean<ObjectMapper>()
    val mathClient = application.getBean<MathClient>()
//11946555
//    firstSeed = 13_000_000L,
//    lastSeed = 13_100_000L,
    // Вызов метода с параллельной обработкой
    parallelSeedProcessingWithCoroutines(
        seedRunner = seedRunner,
        mathClient = mathClient,
        objectMapper = objectMapper,
        gameId = "RumblingRun-variation-95",
        firstSeed = 0,
        lastSeed = 1_000_000,
        10

    )
}

/**
 * Метод для параллельной обработки сидов
 */
fun parallelSeedProcessingWithCoroutines(
    seedRunner: SeedRunner,
    mathClient: MathClient,
    objectMapper: ObjectMapper,
    gameId: String,
    firstSeed: Long,
    lastSeed: Long,
    concurrency: Int = Runtime.getRuntime().availableProcessors()
) {
    // Шаг 1: GetConfig
    val responseGetConfigString = mathClient.getConfig(gameId)
    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)

    val total = lastSeed - firstSeed + 1
    val processed = AtomicLong(0)
    val startTime = System.currentTimeMillis()

    println("Начинаем обработку сидов от $firstSeed до $lastSeed")
    println("Всего сидов: $total")
    println("Количество параллельных задач: $concurrency")

    // Семафор для ограничения количества одновременных задач
    val semaphore = Semaphore(concurrency)

    runBlocking {
        val progressJob = launch {
            while (processed.get() < total) {
                delay(60000)
                val current = processed.get()
                val percent = (current * 100) / total
                println("\r[Прогресс: $percent%] Обработано: $current/$total")
            }
        }

        // Создаем корутины для каждой задачи
        val jobs = (firstSeed..lastSeed).map { seed ->
            GlobalScope.launch {
                semaphore.acquire()
                try {
                    seedRunner.run(gameId, seed.toULong(), configResponse)
                } catch (e: Exception) {
                    println("\nОшибка при обработке сида $seed: ${e.message}")
                } finally {
                    processed.incrementAndGet()
                    semaphore.release()
                }
            }
        }

        // Ждем завершения всех корутин
        jobs.joinAll()
        progressJob.cancel()
    }

    printStatistics(startTime, total, processed.get())
}


fun printStatistics(startTime: Long, total: Long, processed: Long) {
    val endTime = System.currentTimeMillis()
    val elapsedTimeMillis = endTime - startTime
    val elapsedSeconds = elapsedTimeMillis / 1000

    val hours = elapsedTimeMillis / (1000 * 60 * 60)
    val minutes = (elapsedTimeMillis % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (elapsedTimeMillis % (1000 * 60)) / 1000

    val successRate = if (total > 0) (processed * 100) / total else 0
    val speed = if (elapsedSeconds > 0) processed / elapsedSeconds else 0

    println("\n" + "=".repeat(60))
    println("ИТОГИ ОБРАБОТКИ")
    println("=".repeat(60))
    println("Обработано сидов: $processed из $total")
    println("Процент успеха: $successRate%")
    println("Общее время: ${hours}ч ${minutes}мин ${seconds}сек")
    println("Средняя скорость: ${speed} сидов/сек")
    println("Время на один сид: ${elapsedTimeMillis.toDouble() / total} мс")
    println("=".repeat(60))
}
