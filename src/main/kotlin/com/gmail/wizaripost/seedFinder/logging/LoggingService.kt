package com.gmail.wizaripost.seedFinder.logging

import org.springframework.beans.factory.annotation.Autowired

abstract class LoggingService {

    @Autowired
    protected lateinit var loggerService: FileLogger

    protected val log: Logger by lazy {
        loggerService.getLogger(this::class.java)
    }

    // Вспомогательный метод для логирования seed
    protected fun logSeed(message: String) {
        log.info(message)
    }
}