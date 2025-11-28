package com.gmail.wizaripost.seedFinder.utils

fun animateWithSpinner(totalItems: Int) {
    val spinner = arrayOf('|', '/', '-', '\\')
    var spinnerIndex = 0

    repeat(totalItems + 1) { i ->
        print("\r")

        val percentage = i.toDouble() / totalItems * 100
        print("${spinner[spinnerIndex]} Обработано: $i/$totalItems (${"%.1f".format(percentage)}%)")

        spinnerIndex = (spinnerIndex + 1) % spinner.size
        Thread.sleep(100)
    }
    println()
}