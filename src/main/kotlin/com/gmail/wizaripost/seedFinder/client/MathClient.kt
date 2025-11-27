package com.gmail.wizaripost.seedFinder.client


import com.gmail.wizaripost.seedFinder.dto.CloseRequest
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

    @PostMapping("/{gameName}/execute")
    fun executeClose(
        @PathVariable gameName: String,
        request: CloseRequest
    ): String
}