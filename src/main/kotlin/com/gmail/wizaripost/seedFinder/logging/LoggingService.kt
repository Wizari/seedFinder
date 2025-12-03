package com.gmail.wizaripost.seedFinder.logging

import com.gmail.wizaripost.seedFinder.logging.SimpleFileLogger
import com.gmail.wizaripost.seedFinder.logging.Logger
import org.springframework.beans.factory.annotation.Autowired

abstract class LoggingService {

    @Autowired
    protected lateinit var loggerService: SimpleFileLogger

    protected val log: Logger by lazy {
        loggerService.getLogger(this::class.java)
    }

    // Вспомогательный метод для логирования seed
    protected fun logSeed(seed: Long?) {
        if (seed != null) {
            log.info("Seed: $seed")
        }
    }
}