package ru.atom.matchmaker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.other.GameRepository;
import ru.atom.other.GameSession;
import ru.atom.other.PlayerQueue;

@Controller
@RequestMapping("/game")
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerQueue playerQueue;

    @RequestMapping(
            path = "create",
            method = RequestMethod.GET)
    public ResponseEntity<String> create(@RequestParam("playerCount") int playerCount) {
        GameSession gameSession= new GameSession(playerCount);
        gameRepository.put(gameSession);
        log.info("Game session ID{} created", gameSession.getId());
        return new ResponseEntity<>(String.valueOf(gameSession.getId()),HttpStatus.OK);
    }

    @RequestMapping(
            path = "start",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> start(@RequestParam("gameId") long gameID ) {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            path = "connect",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> connect(@RequestParam("gameId") long gameID,
                                          @RequestParam("name") String name) {
        return ResponseEntity.ok().build();
    }

}