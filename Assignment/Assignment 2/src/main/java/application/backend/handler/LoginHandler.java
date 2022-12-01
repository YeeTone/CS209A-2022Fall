package application.backend.handler;

import application.backend.player.Player;
import application.backend.player.PlayerLib;
import org.java_websocket.WebSocket;

public class LoginHandler implements Handler{

    @Override
    public void handle(WebSocket webSocket, String message) {
        /*
        * USERNAME@PASSWORD
        * */
        String[] ss = message.split("@");
        if(PlayerLib.login(ss[0], ss[1], webSocket)){
            webSocket.send("Login:Success!");
        }else {
            webSocket.send("Login:Failure!");
        }
    }
}
