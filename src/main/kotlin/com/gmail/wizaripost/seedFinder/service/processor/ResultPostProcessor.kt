package com.gmail.wizaripost.seedFinder.service.processor

interface ResultPostProcessor {

    fun process(key: String, payload: Any)

}