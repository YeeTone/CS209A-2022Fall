package application.backend.handler;

import org.java_websocket.WebSocket;

public interface Handler {

    String SPLITTER = "@";

    void handle(WebSocket webSocket, String message);
}
