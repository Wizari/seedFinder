package com.gmail.wizaripost.seedFinder.service

import org.springframework.stereotype.Service

@Service
class ResultPostProcessorImpl : ResultPostProcessor {
    override fun process(key: String, payload: Any) {
        println("$key >>> $payload")
    }
}