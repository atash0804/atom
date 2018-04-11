package ru.atom.lecture08.websocket;

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

import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import javax.swing.Timer;
import java.util.TimerTask;

@Controller
@RequestMapping("chat")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private Map<String, Integer> msgCount = new ConcurrentHashMap<>();
    private Map<String, String> password = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name, @RequestParam("pass") String pass) {
        name = HtmlUtils.htmlEscape(name);
        pass = HtmlUtils.htmlEscape(pass);
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        if (password.containsKey(name) && !pass.equals(password.get(name))) {
            return ResponseEntity.badRequest().body("Incorrect password. Try again");
        }
        if (pass.length() < 1) {
            return ResponseEntity.badRequest().body("Too short pass. At least 1 character required :<");
        }
        if (pass.length() > 20) {
            return ResponseEntity.badRequest().body("Too long password, sorry :<");
        }
        usersOnline.put(name, name);
        if (!password.containsKey(name)) {
            password.put(name, pass);
        }
        msgCount.put(name, 0);
        String msg = "[" + name + "] logged in";
        messages.add(msg);
        try{
            FileWriter hFile = new FileWriter("hist.txt", true);
            hFile.append(msg);
            hFile.append("\n");
            hFile.close();
        }
        catch (IOException e) {
        }

        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat() {
        if (messages.isEmpty()) {
            try{
                FileReader hFile = new FileReader("hist.txt");
                Scanner scan = new Scanner(hFile);
                while (scan.hasNextLine()) {
                    messages.add(scan.nextLine());
                }
                hFile.close();
            }
            catch (IOException e) {
            }
        }

        return new ResponseEntity<>(messages.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")),
                HttpStatus.OK);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersOnline.keySet().stream().sorted().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }
    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestParam("name") String name) {
        name = HtmlUtils.htmlEscape(name);
        if (usersOnline.containsKey(name)) {
            usersOnline.remove(name);
            msgCount.remove(name);
            String msg = "[" + name + "] logged out";
            messages.add(msg);
            try{
                FileWriter hFile = new FileWriter("hist.txt", true);
                hFile.append(msg);
                hFile.append("\n");
                hFile.close();
            }
            catch (IOException e) {
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("You are not logged in anyway :(");
        }
    }

}
