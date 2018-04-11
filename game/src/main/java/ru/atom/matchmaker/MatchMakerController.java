package ru.atom.matchmaker;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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

import java.io.IOException;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";
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
    //TODO
    //сюда надо передать число игроков в игре, обратно возвращается gameID, далее в игру с этим ID должны добавиться игроки
    //возможно(и скорее всего) это надо делать в методе join
    private long create() throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "playerCount=" + 4))
                .url(PROTOCOL + HOST + PORT + "/game/create")
                .build();
        Response response = client.newCall(request).execute();
        return  Long.parseLong(response.body().string());
    }

    private void connect(long gameId,String name) throws IOException{
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType,
                        "gameId=" + gameId + "&name=" + name))
                .url(PROTOCOL + HOST + PORT + "/game/connect")
                .build();
        client.newCall(request).execute();
    }

    private void start(long gameId) throws IOException{
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "gameId=" + gameId))
                .url(PROTOCOL + HOST + PORT + "/game/start")
                .build();
        client.newCall(request).execute();
    }
}
