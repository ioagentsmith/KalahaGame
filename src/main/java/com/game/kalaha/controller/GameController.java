package com.game.kalaha.controller;

import com.game.kalaha.model.PlayerNames;
import com.game.kalaha.service.GameService;
import com.game.kalaha.util.BoardEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.game.kalaha.model.Kalaha;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class GameController {

    @Resource(name = "gameService")
    private GameService gameService;

    Kalaha kalahaGame;

    @RequestMapping(value = "/startGame.do", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("players") PlayerNames players, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }

        kalahaGame = new Kalaha(players);

        model.addAttribute("kalahaGame", kalahaGame);
        model.addAttribute("rowLength", BoardEnum.PITS_PER_PLAYER.getValue());
        model.addAttribute("totalPits", BoardEnum.TOTAL_PITS.getValue());

        return "kalahaGame";
    }

    @RequestMapping(value = "/makeMove.do", method = RequestMethod.GET)
    public String move(@RequestParam String pitId, Model model) {
        int clickedPitId = Integer.valueOf(pitId).intValue();
        kalahaGame = gameService.startTheGame(clickedPitId, kalahaGame);

        model.addAttribute("kalahaGame", kalahaGame);
        model.addAttribute("rowLength", BoardEnum.PITS_PER_PLAYER.getValue());
        model.addAttribute("totalPits", BoardEnum.TOTAL_PITS.getValue());
        model.addAttribute("message", kalahaGame.getMessage());

        return "kalahaGame";
    }

}