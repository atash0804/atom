package ru.atom.matchmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;
import ru.atom.gameController.GameController;
import ru.atom.other.Leaderboard;
import ru.atom.other.Player;
import ru.atom.other.PlayerQueue;

import java.util.TimerTask;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    private static int NUMBER_OF_PLAYERS = 4;

    java.util.Timer checkPlayers = new java.util.Timer().schedule(
            new TimerTask() {
                public void run() {
                    if (PlayerQueue.getQueue().size() > 0) {
                        GameController.create()
                    }
                }
            },5000);


    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> join(@RequestParam("name") String name) {

        name = HtmlUtils.htmlEscape(name);
        if (!Leaderboard.getStat().containsKey(name)) {
            Leaderboard.getStat().put(name, 0);
        }

        PlayerQueue.getQueue().offer(new Player(name));


        return ResponseEntity.ok().build();
    }
}
