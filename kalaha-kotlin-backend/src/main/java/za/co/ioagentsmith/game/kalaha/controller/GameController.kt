package za.co.ioagentsmith.game.kalaha.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import za.co.ioagentsmith.game.kalaha.model.Kalaha
import za.co.ioagentsmith.game.kalaha.model.PlayerNames
import za.co.ioagentsmith.game.kalaha.service.GameService
import za.co.ioagentsmith.game.kalaha.util.BoardEnum

import javax.annotation.Resource
import javax.validation.Valid

@Controller
class GameController {

    @Resource(name = "gameService")
    internal val gameService: GameService? = null

    internal lateinit var kalahaGame: Kalaha

    @RequestMapping(value = "/startGame.do", method = arrayOf(RequestMethod.POST))
    fun submit(@Valid @ModelAttribute("players") players: PlayerNames, result: BindingResult, model: ModelMap): String {
        if (result.hasErrors()) {
            return ERROR_PATH
        }

        kalahaGame = Kalaha(players)

        model.addAttribute(KALAHA_GAME, kalahaGame)
        model.addAttribute(ROW_LENGTH, BoardEnum.PITS_PER_PLAYER.value)
        model.addAttribute(TOTAL_PITS, BoardEnum.TOTAL_PITS.value)

        return KALAHA_GAME
    }

    @RequestMapping(value = "/makeMove.do", method = arrayOf(RequestMethod.GET))
    fun move(@RequestParam pitId: String, model: Model): String {
        val clickedPitId = Integer.valueOf(pitId).toInt()
        kalahaGame = gameService!!.startTheGame(clickedPitId, kalahaGame)

        model.addAttribute(KALAHA_GAME, kalahaGame)
        model.addAttribute(ROW_LENGTH, BoardEnum.PITS_PER_PLAYER.value)
        model.addAttribute(TOTAL_PITS, BoardEnum.TOTAL_PITS.value)
        model.addAttribute(MESSAGE, kalahaGame.message!!)

        return KALAHA_GAME
    }

    companion object {

        internal val ERROR_PATH = "/error"
        internal val KALAHA_GAME = "kalahaGame"
        internal val ROW_LENGTH = "rowLength"
        internal val TOTAL_PITS = "totalPits"
        internal val MESSAGE = "message"
    }
}