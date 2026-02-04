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
////		println(formatter.)
////		println(formatter)
////		println()
//
//		println("gamble and win")
//		println(createSeed(441U, 1u))
//		println("X2 and win")
//		println(createSeed(441U, 4U))
//		println("X3 and win")
//		println(createSeed(441U, 9u))
//		println("X4 and win")
//		println(createSeed(441U, 9U))
//		println("X5 and win")
//		println(createSeed(441U, 3979020692U))
//
//		println("Склейка двух сидов:")
//		println(createSeed(846912496U, 401157894U))
//		//Получен респонс 1722960036109147120
//
//		//1 вар 1-2 3637461473294888710
//		//2 вар 2-1 1722960036109147120
//		//1722960036109147120
//		println("Test1:")
////		println(createSeed(1746058339315351552U, 4072472249U))
//		println(createSeed(406586730U, 0U))
//		println(createSeed2(406586730L, 0L))
//		println(createSeed(0U, 406586730U))
//		println(createSeed2(0L, 406586730L))
//
//		println("Test1:2")
////		println(createSeed(1746058339315351552U, 4072472249U))
//		println(createSeed(406586730U, 1U))
//		println(createSeed2(406586730L, 1L))
//		println(createSeed(1U, 406586730U))
//		println(createSeed2(1L, 406586730L))
//
//		val seed1 = createSeed2(221L, 422L)
//		println("seed1 $seed1")
//		println("seed1 ${splitSeed(seed1)}")
//
////        val seed2 = createSeed2(1L, 406586730L)
//		val seed2: ULong = 234234234111u
//		println("seed2 $seed2")
//		println("seed2 ${splitSeed(seed2)}")
//		println("seed11111 ${splitSeed2(17339433639131128754u)}")
//		val seed11111 = createSeed2(17339433635727540224U, 3403588530U)
//		println("seed11111 $seed11111")
//		println("seed11111 ${splitSeed2(seed11111)}")
//
//
//
//
////		val seed3 = createSeed2(33401960660992, 7777)
//		val seed3 = 17663306427090199526U
//		println("seed3 $seed3")
//		println("seed3 ${splitSeed2(seed3)}")
////		high:class kotlin.ULong, 33401960660992
////		low:class kotlin.ULong, 7777
////		Result: 33401960668769 (expected: 1746058339315351552)


//		println(splitSeed2(6668254285589538289U).toString())
//		println(splitSeed(6668254285589538289U).toString())
//		println(splitSeed(6668254285589538289U).toString())
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
//		println("createSeed2 " + createSeed2(1, 1).toString())
//		println("createSeed2 " + createSeed2(maxUInt, 3).toString())
//		-5835649841802651490
		val sseed2 = -8151592353418518496
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

