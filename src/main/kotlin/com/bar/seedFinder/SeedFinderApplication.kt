package com.bar.seedFinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SeedFinderApplication

fun main(args: Array<String>) {
	runApplication<SeedFinderApplication>(*args)
	println("Hello World!")
	if (args.isNotEmpty()) {
		for (arg in args) {
			println(arg)
		}
	}
}
