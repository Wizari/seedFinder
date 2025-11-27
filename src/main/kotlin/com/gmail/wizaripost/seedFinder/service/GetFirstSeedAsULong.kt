package com.gmail.wizaripost.seedFinder.service

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class ArgsProcessor(){
    fun parseFirstArgToULong(args: Array<String>): ULong {
        if (args.isEmpty()) {
            throw IllegalArgumentException("No arguments provided")
        }

        return try {
            args[0].toULong()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("First argument '${args[0]}' is not a valid unsigned long number", e)
        }
    }
}