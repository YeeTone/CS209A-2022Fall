package application.backend.handler;

import application.backend.player.PlayerLib;
import org.java_websocket.WebSocket;

public class HeartBeatHandler implements Handler {
    @Override
    public void handle(WebSocket webSocket, String message) {
        /*
        * USERNAME
        * */

        if(PlayerLib.heartBeat(webSocket)){
            webSocket.send(message + ", server is alive!");
        }else {
            webSocket.send("Previous connection has been expired!");
            webSocket.close();
        }

    }
}
