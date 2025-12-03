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
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*
import kotlin.math.min

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
//    val firstSeed: Long = 14_000_000L
//    val lastSeed: Long = 200_000_000L
//    val firstSeed: Long = 51965917L
    val lastSeed: Long = 200_000_000L
    val firstSeed: Long = 1L
//    val lastSeed: Long = 100L
    // Вызов метода с параллельной обработкой
    parallelSeedProcessingHybrid(
        seedRunner = seedRunner,
        mathClient = mathClient,
        objectMapper = objectMapper,
        gameId = "RumblingRun-variation-95",
        firstSeed = firstSeed,
        lastSeed = lastSeed,
        concurrency = 28 // Используйте все 28 ядер
    )
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
                        println("[${elapsedMin}мин] Прогресс: ${percent}% ($current/$total)")
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
                            seedRunner.run(gameId, seed.toULong(), configResponse)
                            batchSuccessCount++
                        } catch (e: Exception) {
                            // Тихая ошибка - просто логируем
                            if (index % 100 == 0) { // Логируем только каждый 100-й батч
                                println("Батч $index: ошибка на сиде $seed")
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
        else -> 1000
    }
    return maxOf(baseSize, concurrency * 10) // Но не меньше чем потоков * 10
}



//fun parallelSeedProcessingWithBatches(
//    seedRunner: SeedRunner,
//    mathClient: MathClient,
//    objectMapper: ObjectMapper,
//    gameId: String,
//    firstSeed: Long,
//    lastSeed: Long,
//    concurrency: Int = Runtime.getRuntime().availableProcessors(),
//    batchSize: Int = 1000 // Размер батча
//) {
//    val responseGetConfigString = mathClient.getConfig(gameId)
//    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
//
//    val total = lastSeed - firstSeed + 1
//    val processed = AtomicLong(0)
//
//    println("Начинаем обработку сидов от $firstSeed до $lastSeed")
//    println("Всего сидов: $total")
//    println("Количество потоков: $concurrency")
//    println("Размер батча: $batchSize")
//
//    val totalTime = measureTimeMillis {
//        runBlocking {
//            val dispatcher = Dispatchers.Default.limitedParallelism(concurrency)
//
//            // Запускаем корутину для прогресса
//            val progressJob = launch {
//                var lastPrinted = 0L
//                while (processed.get() < total) {
//                    delay(60000) // 1 минута
//                    val current = processed.get()
//                    if (current > lastPrinted) {
//                        val percent = (current * 100) / total
//                        println("\r[Прогресс: $percent%] Обработано: $current/$total")
//                        lastPrinted = current
//                    }
//                }
//            }
//
//            // Создаем диапазон сидов
//            val seedRange = firstSeed..lastSeed
//
//            // Разбиваем на батчи и обрабатываем
//            val batchJobs = mutableListOf<Job>()
//
//            seedRange.chunked(batchSize).forEachIndexed { batchIndex, batch ->
//                val job = launch(dispatcher) {
//                    var localProcessed: Long = 0
//                    var lastReportTime = System.currentTimeMillis()
//
//                    for (seed in batch) {
//                        try {
//                            seedRunner.run(gameId, seed.toULong(), configResponse)
//                        } catch (e: Exception) {
//                            println("Ошибка в батче $batchIndex, сид $seed: ${e.message}")
//                        } finally {
//                            localProcessed++
//
//                            // Периодически обновляем прогресс внутри батча
//                            val currentTime = System.currentTimeMillis()
//                            if (currentTime - lastReportTime > 10000) { // Каждые 10 секунд
//                                processed.addAndGet(localProcessed)
//                                localProcessed = 0
//                                lastReportTime = currentTime
//                            }
//                        }
//                    }
//
//                    // Финализируем оставшиеся
//                    if (localProcessed > 0) {
//                        processed.addAndGet(localProcessed)
//                    }
//
////                    if (batchIndex % 10 == 0) {
////                        println("Батч $batchIndex завершен (${batch.first()}..${batch.last()})")
////                    }
//                }
//                batchJobs.add(job)
//            }
//
//            // Ждем завершения всех батчей
//            batchJobs.joinAll()
//            progressJob.cancel()
//
//            println("\nВсе батчи завершены!")
//        }
//    }
//
//    println("\nОбщее время: ${totalTime / 1000} секунд")
//    println("Обработано сидов: ${processed.get()}/$total")
//}






/**
 * Метод для параллельной обработки сидов
 */
//fun parallelSeedProcessingWithCoroutines(
//    seedRunner: SeedRunner,
//    mathClient: MathClient,
//    objectMapper: ObjectMapper,
//    gameId: String,
//    firstSeed: Long,
//    lastSeed: Long,
//    concurrency: Int = Runtime.getRuntime().availableProcessors()
//) {
//    // Шаг 1: GetConfig
//    val responseGetConfigString = mathClient.getConfig(gameId)
//    val configResponse: ConfigResponse = objectMapper.readValue(responseGetConfigString)
//
//    val total = lastSeed - firstSeed + 1
//    val processed = AtomicLong(0)
//    val startTime = System.currentTimeMillis()
//
//    println("Начинаем обработку сидов от $firstSeed до $lastSeed")
//    println("Всего сидов: $total")
//    println("Количество параллельных задач: $concurrency")
//
//    // Семафор для ограничения количества одновременных задач
//    val semaphore = Semaphore(concurrency)
//
//    runBlocking {
//        val progressJob = launch {
//            while (processed.get() < total) {
//                delay(60000) //1 минута
//                val current = processed.get()
//                val percent = (current * 100) / total
//                println("\r[Прогресс: $percent%] Обработано: $current/$total")
//            }
//        }
//
//        // Создаем корутины для каждой задачи
//        val jobs = (firstSeed..lastSeed).map { seed ->
//            GlobalScope.launch {
//                semaphore.acquire()
//                try {
//                    seedRunner.run(gameId, seed.toULong(), configResponse)
//                } catch (e: Exception) {
//                    println("\nОшибка при обработке сида $seed: ${e.message}")
//                } finally {
//                    processed.incrementAndGet()
//                    semaphore.release()
//                }
//            }
//        }
//
//        // Ждем завершения всех корутин
//        jobs.joinAll()
//        progressJob.cancel()
//    }
//
//    printStatistics(startTime, total, processed.get())
//}
//
//
//fun printStatistics(startTime: Long, total: Long, processed: Long) {
//    val endTime = System.currentTimeMillis()
//    val elapsedTimeMillis = endTime - startTime
//    val elapsedSeconds = elapsedTimeMillis / 1000
//
//    val hours = elapsedTimeMillis / (1000 * 60 * 60)
//    val minutes = (elapsedTimeMillis % (1000 * 60 * 60)) / (1000 * 60)
//    val seconds = (elapsedTimeMillis % (1000 * 60)) / 1000
//
//    val successRate = if (total > 0) (processed * 100) / total else 0
//    val speed = if (elapsedSeconds > 0) processed / elapsedSeconds else 0
//
//    println("\n" + "=".repeat(60))
//    println("ИТОГИ ОБРАБОТКИ")
//    println("=".repeat(60))
//    println("Обработано сидов: $processed из $total")
//    println("Процент успеха: $successRate%")
//    println("Общее время: ${hours}ч ${minutes}мин ${seconds}сек")
//    println("Средняя скорость: ${speed} сидов/сек")
//    println("Время на один сид: ${elapsedTimeMillis.toDouble() / total} мс")
//    println("=".repeat(60))
//}
