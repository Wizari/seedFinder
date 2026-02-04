package com.gmail.wizaripost.seedFinder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gmail.wizaripost.seedFinder.client.MathClient
import com.gmail.wizaripost.seedFinder.dto.ConfigResponse
import com.gmail.wizaripost.seedFinder.seedfinder.SeedRunner
import com.gmail.wizaripost.seedFinder.service.ArgsProcessor
import kotlinx.coroutines.*
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.measureTimeMillis

@EnableFeignClients
@SpringBootApplication
class SeedFinderApplication

fun main(args: Array<String>) {
    val application = runApplication<SeedFinderApplication>(*args)

    val seedRunner = application.getBean<SeedRunner>()
    val argsProcessor = application.getBean<ArgsProcessor>()
    val objectMapper = application.getBean<ObjectMapper>()
    val mathClient = application.getBean<MathClient>()
    val gameId = "RumblingRun-variation-95"
//    val gameId = "Merlin-variation-0"
//11946555
    val firstSeed: Long = 1L
//   val firstSeed: Long = 2749L
//   val firstSeed: Long = 2315976L
//   val firstSeed: Long = 7100347L
//   val firstSeed: Long = 10094347L
//   val firstSeed: Long = 16227847L
//   val firstSeed: Long = 74_959_847L
//   val firstSeed: Long = 145849347L
//   val firstSeed: Long = 174593347L
//   val firstSeed: Long = 476_141_847L //SesFindZeroWin LAST
//    val firstSeed: Long = 250730L
//    val firstSeed: Long = 2796924L
//    val firstSeed: Long = 10465119L
//    val firstSeed: Long = 9610485
//    val firstSeed: Long = 1_000_000L
//    val firstSeed: Long = 100_000_000L

    val lastSeed: Long = 100_000_000L
//    val lastSeed: Long = 700_000_000L

//    val firstSeed: Long = 2500L
//    val lastSeed: Long = 100L
//    val lastSeed: Long = 3500L
//
    // Вызов метода с параллельной обработкой


    parallelSeedProcessingHybrid(
        seedRunner = seedRunner,
        mathClient = mathClient,
        objectMapper = objectMapper,
        gameId = gameId,
        firstSeed = firstSeed,
        lastSeed = lastSeed,
//        concurrency = 28 // Используйте все 28 ядер
        concurrency = 10 // Используйте все 28 ядер
    )


//    simpleFor(
//        seedRunner = seedRunner,
//        mathClient = mathClient,
//        objectMapper = objectMapper,
//        gameId = gameId,
//        firstSeed = firstSeed,
//        lastSeed = lastSeed,
//    )

}


fun parallelSeedProcessingHybrid(
    seedRunner: SeedRunner,
    mathClient: MathClient,
    objectMapper: ObjectMapper,
    gameId: String,
    firstSeed: Long,
    lastSeed: Long,
    concurrency: Int = Runtime.getRuntime().availableProcessors()
) {
    val responseGetConfigString = mathClient.getConfig(gameId)
    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)

    val total = lastSeed - firstSeed + 1
    val processed = AtomicLong(0)

    // Автоматический расчет размера батча на основе количества сидов
    val optimalBatchSize = calculateOptimalBatchSize(total, concurrency)

    println("=== Гибридная обработка ===")
    println("Диапазон: $firstSeed..$lastSeed")
    println("Всего сидов: $total")
    println("Потоков: $concurrency")
    println("Размер батча: $optimalBatchSize")
    println("Количество батчей: ${(total + optimalBatchSize - 1) / optimalBatchSize}")

    // Объявляем totalTime здесь, чтобы она была доступна в progressJob
    var totalTimeMillis: Long = 0

    val executionTime = measureTimeMillis {
        runBlocking {
            val dispatcher = Dispatchers.Default.limitedParallelism(concurrency)

            // Сохраняем время начала для progressJob
            val executionStartTime = System.currentTimeMillis()

            // Мониторинг прогресса
            val progressJob = launch {
                var lastUpdate = 0L
//                val updateInterval = 600000L // 10 минут
                val updateInterval = 600000L // 10 минут

                while (processed.get() < total) {
                    delay(updateInterval)
                    val current = processed.get()
                    if (current > lastUpdate) {
                        val percent = String.format("%.1f", current * 100.0 / total)
                        val elapsedMin = (System.currentTimeMillis() - executionStartTime) / 60000
                        println("[${elapsedMin}мин] ${percent}% ($current/$total) " +
                                "Init seed: $firstSeed; " +
                                "cur seed: ${current+firstSeed}")
                        lastUpdate = current
                    }
                }
            }

            // Создаем и выполняем батчи
            val seedRange = firstSeed..lastSeed
            val batches = seedRange.chunked(optimalBatchSize)

            val batchJobs = batches.mapIndexed { index, batch ->
                async(dispatcher) {
                    val batchStartTime = System.currentTimeMillis()
                    var batchSuccessCount: Long = 0 // Переименовал successCount -> batchSuccessCount

                    for (seed in batch) {
                        try {
                            seedRunner.run(gameId, createSeed(seed, seed), configResponse)
                            batchSuccessCount++
                        } catch (e: Exception) {
                            // Тихая ошибка - просто логируем
                            if (index % 100 == 0) { // Логируем только каждый 100-й батч
                                println("Батч $index: ошибка $e на сиде $seed")
                            }
                        }
                    }

                    // Обновляем глобальный счетчик
                    processed.addAndGet(batchSuccessCount)

                    val batchTime = System.currentTimeMillis() - batchStartTime
                    if (batchTime > 10000) { // Логируем только медленные батчи
                        println("Батч $index (${batch.size} сидов) выполнен за ${batchTime}мс")
                    }

                    batchSuccessCount // Возвращаем количество успешных
                }
            }

            // Ждем и собираем результаты
            val results = batchJobs.awaitAll()
            val totalSuccess = results.sum()

            progressJob.cancel()

            totalTimeMillis = System.currentTimeMillis() - executionStartTime

            println("\n=== РЕЗУЛЬТАТЫ ===")
            println("Успешно обработано: $totalSuccess/$total")
            println("Процент успеха: ${String.format("%.2f", totalSuccess * 100.0 / total)}%")
        }
    }

    println("\n=== ИТОГИ ===")
    println("Общее время выполнения: ${executionTime / 1000} секунд")
    println("Средняя скорость: ${String.format("%.1f", total / (executionTime / 1000.0))} сидов/сек")
    println("Время на сид: ${String.format("%.3f", executionTime / total.toDouble())} мс")
}

fun calculateOptimalBatchSize(totalSeeds: Long, concurrency: Int): Int {
    // Эмпирическая формула для расчета размера батча
    val baseSize = when {
        totalSeeds < 1000 -> 10 // Мало сидов - маленькие батчи
        totalSeeds < 10000 -> 50
        totalSeeds < 100000 -> 100
        totalSeeds < 1000000 -> 500
        else -> 500
//        else -> 1000
    }
    return maxOf(baseSize, concurrency * 10) // Но не меньше чем потоков * 10
}

private fun createSeed(highSeed: Long, lowSeed: Long): ULong{
    val high = (highSeed shl 32).toULong()
    val low = lowSeed.toULong() and 0xffffffffU
    val seed: ULong = high or low
    return seed
}

fun simpleFor(
    seedRunner: SeedRunner,
    mathClient: MathClient,
    objectMapper: ObjectMapper,
    gameId: String,
    firstSeed: Long,
    lastSeed: Long,
) {
    val responseGetConfigString = mathClient.getConfig(gameId)
    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
    for (i in firstSeed..lastSeed) {
//        seedRunner.run(gameId, i.toULong(), configResponse)
        seedRunner.run(gameId, createSeed(i, i), configResponse)
    }
}


