package com.bar.seedFinder.client


import com.bar.seedFinder.dto.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    name = "MathClient",
    url = "http://localhost:8012"
    // Убрали configuration = [ContentTypeInterceptor::class]
)
interface MathClient {

    @PostMapping("/RumblingRun-variation-95/newGame")
    fun newGame(request: NewGameRequest): GameResponse // Убрали @HeaderMap

    @PostMapping("/RumblingRun-variation-95/getConfig")
    fun getConfig(): ConfigResponse

    @PostMapping("/RumblingRun-variation-95/execute")
    fun execute(request: SpinRequest): GameResponse

    @PostMapping("/RumblingRun-variation-95/execute")
    fun execute(request: FreeSpinRequest): GameResponse
}

//import com.bar.seedFinder.dto.*
//import org.springframework.cloud.openfeign.FeignClient
//import org.springframework.http.MediaType
//import org.springframework.web.bind.annotation.*
//
//@FeignClient(name = "MathClient", url = "http://localhost:8012")
//interface MathClient {
//
//    @PostMapping(
//        value = ["/RumblingRun-variation-95/newGame"],
//        consumes = [MediaType.APPLICATION_JSON_VALUE], // Или application/vnd.api+json если строго нужно
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    fun newGame(@RequestBody request: NewGameRequest): GameResponse
//
//    @PostMapping(
//        value = ["/RumblingRun-variation-95/getConfig"],
//        consumes = [MediaType.APPLICATION_JSON_VALUE],
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    fun getConfig(): ConfigResponse
//
//    @PostMapping(
//        value = ["/RumblingRun-variation-95/execute"],
//        consumes = [MediaType.APPLICATION_JSON_VALUE],
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    fun execute(@RequestBody request: ExecuteRequest): GameResponse
//}