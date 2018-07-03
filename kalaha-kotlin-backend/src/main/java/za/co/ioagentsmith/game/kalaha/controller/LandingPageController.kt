package za.co.ioagentsmith.game.kalaha.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LandingPageController {

    @GetMapping("/")
    fun index(): String {
        return INDEX
    }

    companion object {

        internal val INDEX = "index"
    }
}