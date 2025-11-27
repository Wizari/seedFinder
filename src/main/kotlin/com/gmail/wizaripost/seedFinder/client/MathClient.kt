package com.gmail.wizaripost.seedFinder.client


import com.gmail.wizaripost.seedFinder.dto.FreeSpinRequest
import com.gmail.wizaripost.seedFinder.dto.NewGameRequest
import com.gmail.wizaripost.seedFinder.dto.SpinRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    name = "MathClient",
    url = "http://localhost:8012"
)
interface MathClient {

    @PostMapping("/{gameName}/newGame")
    fun newGame(
        @PathVariable gameName: String,
        request: NewGameRequest
    ): String

    @PostMapping("/{gameName}/getConfig")
    fun getConfig(@PathVariable gameName: String): String

    @PostMapping("/{gameName}/execute")
    fun executeSpin(
        @PathVariable gameName: String,
        request: SpinRequest
    ): String

    @PostMapping("/{gameName}/execute")
    fun executeFreeSpin(
        @PathVariable gameName: String,
        request: FreeSpinRequest
    ): String
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