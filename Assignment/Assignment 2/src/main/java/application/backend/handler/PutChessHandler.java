package application.backend.handler;

import application.backend.player.PlayerLib;
import org.java_websocket.WebSocket;

public class PutChessHandler implements Handler{
    @Override
    public void handle(WebSocket webSocket, String message) {
        /* PUTCHESS:TOUSER-QWER
            TOUSER-11910104#\n
            0 1\n
            X
            (走了以后的棋盘，合法性检查在前端完成)
        * */


        String[] split = message.split("#");
        String toUser = split[0].split("-")[1];

        System.out.println(toUser);

        WebSocket opponent = PlayerLib.getWebSocketConnection(toUser);
        if(!opponent.isOpen()){
            return;
        }

        String afterChessboard = split[1].substring(1);

        int[] opponentPut = prevPut(afterChessboard);
        char winner = afterChessboard.split("\n")[1].charAt(0);

        opponent.send(afterChessboard);

    }

    private int[] prevPut(String afterChessboard){
        String line = afterChessboard.split("\n")[0];
        String[] xy = line.split(" ");
        return new int[]{Integer.parseInt(xy[0]), Integer.parseInt(xy[1])};

    }
}
