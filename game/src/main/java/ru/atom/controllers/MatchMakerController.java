package ru.atom.controllers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.atom.models.Player;
import ru.atom.models.Session;
import ru.atom.repositories.PlayersRepository;
import ru.atom.repositories.SessionRepository;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;


@Controller
@RequestMapping("/matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    private static final int PLAYERSCOUNT = 4;
    private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;

    @Autowired
    public MatchMakerController(PlayersRepository playersRepository, SessionRepository sessionRepository) {
        this.playersRepository = playersRepository;
        this.sessionRepository = sessionRepository;
    }

    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> join(@RequestParam("name") String name) throws IOException {
        log.info("Joining to game with name = {}", name);
        Player player = playersRepository.get(name);
        if (player == null) {
            log.info("Player not found. Creating!");
            player = new Player(name, 0);
        }
        playersRepository.add(player);
        BlockingQueue<Session> sessions = sessionRepository.getSessions();
        log.info("Tryin' to find valid session(with rating). It uses manhattan distance");
        double threshold = 0;
        do {
            for (Session session : sessions) {
                if (!session.isFull()) {
                    double rating = Math.abs(session.getAverageRating() - player.getRating());
                    if (rating <= threshold) {
                        log.info("Found valid session {}", session.getId());
                        session.addPlayer(player);
                        if (session.isFull()) {
                            log.info("Session is full. Start the game");
                            start(session.getId());
                            return new ResponseEntity<>(String.valueOf(session.getId()), HttpStatus.OK);
                        }
                    }
                }
            }
            threshold++;
        } while (threshold != 10);

        log.info("Couldn't find valid session, so we're creating one!");
        Session session = sessionRepository.get(create());
        session.addPlayer(player);
        return new ResponseEntity<>(String.valueOf(session.getId()), HttpStatus.OK);
    }

    //TODO
    //сюда надо передать число игроков в игре, обратно возвращается gameID, далее в игру с этим ID должны добавиться игроки
    //возможно(и скорее всего) это надо делать в методе join
    private int create() throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "playerCount=" + PLAYERSCOUNT))
                .url(PROTOCOL + HOST + PORT + "/game/create")
                .build();
        Response response = client.newCall(request).execute();
        return Integer.valueOf(response.body().string());
    }

    private void connect(int gameId, String name) throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType,
                        "gameId=" + gameId + "&name=" + name))
                .url(PROTOCOL + HOST + PORT + "/game/connect")
                .build();
        client.newCall(request).execute();
    }

    private void start(int gameId) throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "gameId=" + gameId))
                .url(PROTOCOL + HOST + PORT + "/game/start")
                .build();
        client.newCall(request).execute();
    }
}
