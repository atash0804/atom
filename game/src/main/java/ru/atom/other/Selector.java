package ru.atom.other;

import ru.atom.gamecontroller.GameController;

import java.util.Date;

public class Selector extends Thread {
    private static int NUMBER_OF_PLAYERS = 4;
    private static int MAX_PLAYERS = 10;

    @Override
    public void run() {
        try {
            while (true) {
                Date curTime = new Date();
                int players;

                if (PlayerQueue.getQueue().size() > MAX_PLAYERS) {
                    players = NUMBER_OF_PLAYERS;
                } else {
                    if (PlayerQueue.getQueue().peek().getConnectTime().getTime() - curTime.getTime() > 10000) {
                        players = Math.max(PlayerQueue.getQueue().size(), NUMBER_OF_PLAYERS);
                    } else {
                        sleep(200);
                        continue;
                    }
                }

                long id = GameController.create(players);
                Player chosenPlayer = PlayerQueue.getQueue().poll();
                int rank = chosenPlayer.getRank();
                chosenPlayer.gameId = id;
                chosenPlayer.interrupt();

                for (int i = 1; i < players; i++) {
                    chosenPlayer = PlayerQueue.getQueue().peek();
                    int min = Math.abs(rank - chosenPlayer.getRank());

                    for (Player j: PlayerQueue.getQueue()) {
                        int cur = Math.abs(rank - j.getRank());
                        if (cur == 0) {
                            chosenPlayer = j;
                            break;
                        }
                        if (cur < min) {
                            min = cur;
                            chosenPlayer = j;
                        }
                    }

                    PlayerQueue.getQueue().remove(chosenPlayer);
                    chosenPlayer.gameId = id;
                    chosenPlayer.interrupt();
                }
            }
        } catch (Exception e) {
            return;
        }
    }
}
