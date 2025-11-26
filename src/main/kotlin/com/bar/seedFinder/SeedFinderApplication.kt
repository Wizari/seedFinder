package com.bar.seedFinder

import com.bar.seedFinder.seedfinder.SeedRunnerTwo
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients


@EnableFeignClients
@SpringBootApplication
class SeedFinderApplication

fun main(args: Array<String>) {
    val application = runApplication<SeedFinderApplication>(*args);

    val seedRunner = application.getBean<SeedRunnerTwo>()
//    seedRunner.sayHello()
    val argsArr : Array<String> = arrayOf("17179869625")

    seedRunner.run(argsArr)
}