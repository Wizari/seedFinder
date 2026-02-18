package com.gmail.wizaripost.seedFinder

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.format.DateTimeFormatter
import java.util.Date

@SpringBootTest
class SeedFinderApplicationTests {

	@Test
	fun contextLoads() {
	}
	@Test
	fun converter() {
		println((Date(1761565972735).toLocaleString()))
		val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

		val seed = -5835649841802651490
		println("seed 522" + splitSeed(seed.toULong()).toString())
		val sseed1 = -220915674787390480
		println("seed search" + splitSeed(sseed1.toULong()).toString())
		println(splitSeed(18_446_744_073_709_551_615u).toString())
		println("1/1 " + createSeed2(1, 1).toString())
		println("1/2 " + createSeed2(1, 2).toString())
		println("2/1 " + createSeed2(2, 1).toString())
		println("3/1 " + createSeed2(3, 1).toString())
		println("3/2 " + createSeed2(3, 2).toString())
		println("3/3 " + createSeed2(3, 1).toString())
		println("7431 " + createSeed2(7431, 7431).toString())
		println("7828 " + createSeed2(7828, 7828).toString())
		println("8022 " + createSeed2(8022, 8022).toString())
		println("8189 " + createSeed2(8189, 8189).toString())
		println("9742 " + createSeed2(9742, 9742).toString())
		println("10077 " + createSeed2(10077, 10077).toString())
		println("4855 " + createSeed2(4855, 4855).toString())
		println("10077 " + createSeed2(10077, 10077).toString())
		println("10077 " + createSeed2(10077, 10077).toString())
		println("10077 " + createSeed2(10077, 10077).toString())
		println("737598185 " + createSeed2(737598185, 737598185).toString())
//		println("createSeed2 " + createSeed2(1, 1).toString())
//		println("createSeed2 " + createSeed2(maxUInt, 3).toString())
//		-5835649841802651490
//		val sseed2 = 3567247285
		val sseed2 = 140603466736952259
		println("seed search" + splitSeed(sseed2.toULong()).toString())
	}



	private fun createSeed(
		gameSeed: UInt,
		riskSeed: UInt,
	): ULong = gameSeed.toULong() + (riskSeed.toULong() shl 32)


	private fun createSeed2(highSeed: Long, lowSeed: Long): ULong{
		val high = (highSeed shl 32).toULong()
		val low = lowSeed.toULong() and 0xffffffffU
		val seed: ULong = high or low
		return seed
	}

	private fun createSeed2(highSeed: ULong, lowSeed: ULong): ULong{
		val high = (highSeed shl 32).toULong()
		val low = lowSeed.toULong() and 0xffffffffU
		val seed: ULong = high or low
		return seed
	}

	fun splitSeed(seed: ULong): Pair<Long, Long> {
		val low = (seed and 0xFFFFFFFFUL).toLong()  // младшие 32 бита
		val high = (seed shr 32).toLong()           // старшие 32 бита
		return Pair(high, low)
	}

	fun splitSeed2(seed: ULong): Pair<ULong, ULong> {
		val low = (seed and 0xFFFFFFFFUL)  // младшие 32 бита GAME
		val high = (seed shr 32)          // старшие 32 бита GAMBLE
		return Pair(high, low)
	}



}

