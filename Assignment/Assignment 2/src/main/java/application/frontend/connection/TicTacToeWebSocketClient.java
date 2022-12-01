package application.frontend.connection;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class TicTacToeWebSocketClient extends WebSocketClient {
    private ConnectionRunnable hb;

    public TicTacToeWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public TicTacToeWebSocketClient(int port) throws URISyntaxException {
        super(new URI("ws://localhost:"+port));
    }

    public void setHb(ConnectionRunnable hb) {
        this.hb = hb;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s){
        if(s.contains("投降")){
            System.out.println("111"+s);
        }
//\\        System.out.println(s);
        hb.setMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
