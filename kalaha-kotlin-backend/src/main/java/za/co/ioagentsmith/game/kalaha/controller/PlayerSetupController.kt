package za.co.ioagentsmith.game.kalaha.controller

import za.co.ioagentsmith.game.kalaha.model.PlayerNames
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class PlayerSetupController {

    @RequestMapping(value = ["/playerSetup.do"], method = [RequestMethod.GET])
    fun showForm(): ModelAndView {
        return ModelAndView("playerSetup", "playerNames", PlayerNames())
    }

}