package ru.atom.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.models.Player;
import ru.atom.models.Session;
import ru.atom.repositories.PlayersRepository;
import ru.atom.repositories.SessionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/game")
public class GameServiceController {
    private static final Logger log = LoggerFactory.getLogger(GameServiceController.class);

    private AtomicInteger gameId = new AtomicInteger(0);

    private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;

    @Autowired
    public GameServiceController(PlayersRepository playersRepository, SessionRepository sessionRepository) {
        this.playersRepository = playersRepository;
        this.sessionRepository = sessionRepository;
    }

    @RequestMapping(
            path = "create",
            method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestParam("playerCount") int playerCount) {
        log.info("Creating game with playerCount = {}", playerCount);
        Session session = new Session(gameId.getAndIncrement(), playerCount);
        sessionRepository.add(session);
        return new ResponseEntity<>(String.valueOf(session.getId()), HttpStatus.OK);
    }

    @RequestMapping(
            path = "start",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> start(@RequestParam("gameId") int gameId) {
        log.info("Staring game with gameId {}", gameId);
        Session session = sessionRepository.get(gameId);
        if (session == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // game starts
        return new ResponseEntity<>(String.valueOf(gameId), HttpStatus.OK);
    }


    @RequestMapping(
            path = "connect",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> connect(@RequestParam("gameId") int gameId, @RequestParam("name") String name) {
        log.info("Connecting to gameId = {} with name = {}", gameId, name);
        Session session = sessionRepository.get(gameId);
        Player player = playersRepository.get(name);
        if (session == null) return new ResponseEntity<>("No such session", HttpStatus.BAD_REQUEST);
        if (player == null) return new ResponseEntity<>("No such player", HttpStatus.BAD_REQUEST);
        session.addPlayer(player);
        // Connecting user to game
        return new ResponseEntity<>(String.valueOf(session.getId()), HttpStatus.OK);
    }


    @GetMapping(path = "leaderboard", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, Long> getLeaderboard() {
        log.info("Getting leaderboard...");
        HashMap<String, Long> leaderboard = new HashMap<>();
        BlockingQueue<Player> players = playersRepository.getPlayers();
        for (Player player : players)
        {
            leaderboard.put(player.getUsername(), player.getRating());
        }
        return leaderboard;
    }
}
