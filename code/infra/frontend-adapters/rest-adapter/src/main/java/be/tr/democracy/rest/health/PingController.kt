package be.tr.democracy.rest.health

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class PingController() {

    @GetMapping("/ping")
    fun ping() = "pong"

}