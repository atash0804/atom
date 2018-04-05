package ru.atom.matchmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;
import ru.atom.other.Leaderboard;
import ru.atom.other.Player;
import ru.atom.other.PlayerQueue;
import ru.atom.other.Selector;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    private Selector selector = new Selector();

    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> join(@RequestParam("name") String name) throws InterruptedException {
        if (!selector.isAlive()) {
            selector.start();
            selector.setDaemon(true);
        }

        name = HtmlUtils.htmlEscape(name);
        int rank = 0;
        if (!Leaderboard.getStat().containsKey(name)) {
            Leaderboard.getStat().put(name, 0);
        } else {
            rank = Leaderboard.getStat().get(name);
        }

        Player player = new Player(name, rank);

        player.start();
        PlayerQueue.getQueue().offer(player);
        player.join();
        String responseBody = Long.toString(player.gameId);
        return ResponseEntity.ok(responseBody);
    }
}
