package za.co.ioagentsmith.game.kalaha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {

    private static final String INDEX = "index";

    @GetMapping("/")
    public String index() {
        return INDEX;
    }
}