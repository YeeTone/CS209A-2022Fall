package application.backend.handler;

import application.backend.player.PlayerLib;
import org.java_websocket.WebSocket;

public class RegisterHandler implements Handler {
    @Override
    public void handle(WebSocket webSocket, String message) {
        /*
            USERNAME@PASSWORD
        *
        * */

        String[] ss = message.split(Handler.SPLITTER);
        if(PlayerLib.register(ss[0], ss[1])){
            webSocket.send("Register:Success!");
        }else {
            webSocket.send("Register:Failure!");
        }
    }
}
