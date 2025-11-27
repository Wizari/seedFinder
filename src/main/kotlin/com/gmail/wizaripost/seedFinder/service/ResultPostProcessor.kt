package com.gmail.wizaripost.seedFinder.service

interface ResultPostProcessor {

    fun process(key: String, payload: Any)

}