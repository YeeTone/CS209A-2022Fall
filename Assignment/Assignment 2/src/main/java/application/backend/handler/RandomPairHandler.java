package application.backend.handler;

import application.backend.player.Player;
import application.backend.player.PlayerLib;
import org.java_websocket.WebSocket;

public class RandomPairHandler implements Handler {

    private Player toBePairedPlayer;
    private WebSocket toBePairedWebSocket;

    @Override
    public void handle(WebSocket webSocket, String message) {
        /*
        * USERNAME
        * */

        String[] ss = message.split(SPLITTER);
        Player newComer = PlayerLib.selectPlayer(ss[0]);
        if (toBePairedPlayer == null) {
            toBePairedPlayer = newComer;
            toBePairedWebSocket = webSocket;
            webSocket.send("Waiting for pairing...");
        } else {
            webSocket.send("Paired with " + toBePairedPlayer.getUsername()+ "O");
            toBePairedWebSocket.send("Paired with " + newComer.getUsername()+"X");
            newComer.setCurrentPaired(toBePairedPlayer);
            toBePairedPlayer.setCurrentPaired(newComer);

            toBePairedPlayer = null;
            toBePairedWebSocket = null;

        }
    }
}
