package application.backend.server;

import application.backend.handler.*;
import application.backend.player.Player;
import application.backend.player.PlayerLib;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


public class TicTacToeServer extends WebSocketServer {

    private static final Map<String, Handler> HANDLERS = new HashMap<String, Handler>(){
        {
            put("LOGIN", new LoginHandler());
            put("REGISTER", new RegisterHandler());
            put("RANDOMPAIR", new RandomPairHandler());
            put("HEARTBEAT", new HeartBeatHandler());
            put("PUTCHESS", new PutChessHandler());
        }
    };

    public static void main(String[] args) {
        int port = 8888;
        try{
            port = Integer.parseInt(args[0]);
        }catch (Exception ignored){
        }

        new TicTacToeServer(port).start();
    }

    public TicTacToeServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("SSS"+s);
        Player p = PlayerLib.selectPlayer(s.split(":")[0].substring(0,s.split(":")[0].length() - 1));

        System.out.println("ppp"+p);
        Player paired = p;
        System.out.println(paired);
        if(paired == null){
            return;
        }
        WebSocket connection = PlayerLib.getWebSocketConnection(paired.getUsername());
        System.out.println(connection);
        connection.send(s);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        String[]ss = s.split(":");
        HANDLERS.get(ss[0]).handle(webSocket, ss[1]);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}
