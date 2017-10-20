package za.co.ioagentsmith.game.kalaha.controller;

import za.co.ioagentsmith.game.kalaha.model.PlayerNames;
import za.co.ioagentsmith.game.kalaha.service.GameService;
import za.co.ioagentsmith.game.kalaha.util.BoardEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import za.co.ioagentsmith.game.kalaha.model.Kalaha;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class GameController {

    private static final String KALAHA_GAME = "kalahaGame";
    private static final String ROW_LENGTH = "rowLength";
    private static final String TOTAL_PITS = "totalPits";
    private static final String MESSAGE = "message";

    @Resource(name = "gameService")
    private GameService gameService;

    Kalaha kalahaGame;

    @RequestMapping(value = "/startGame.do", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("players") PlayerNames players, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }

        kalahaGame = new Kalaha(players);

        model.addAttribute(KALAHA_GAME, kalahaGame);
        model.addAttribute(ROW_LENGTH, BoardEnum.PITS_PER_PLAYER.getValue());
        model.addAttribute(TOTAL_PITS, BoardEnum.TOTAL_PITS.getValue());

        return KALAHA_GAME;
    }

    @RequestMapping(value = "/makeMove.do", method = RequestMethod.GET)
    public String move(@RequestParam String pitId, Model model) {
        int clickedPitId = Integer.valueOf(pitId).intValue();
        kalahaGame = gameService.startTheGame(clickedPitId, kalahaGame);

        model.addAttribute(KALAHA_GAME, kalahaGame);
        model.addAttribute(ROW_LENGTH, BoardEnum.PITS_PER_PLAYER.getValue());
        model.addAttribute(TOTAL_PITS, BoardEnum.TOTAL_PITS.getValue());
        model.addAttribute(MESSAGE, kalahaGame.getMessage());

        return KALAHA_GAME;
    }

}