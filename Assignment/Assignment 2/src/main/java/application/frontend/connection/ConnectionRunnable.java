package application.frontend.connection;

import application.frontend.Client;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.CloseFrame;

public class ConnectionRunnable implements Runnable{
    private final TicTacToeWebSocketClient wsc;
    private final String username;

    private Client client;

    private String message = "";
    private long lastActivationTime = System.currentTimeMillis();

    public ConnectionRunnable(TicTacToeWebSocketClient wsc, String username) {
        this.wsc = wsc;
        this.username = username;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void setMessage(String message) {

        if(message.contains("server is alive")){
            lastActivationTime = System.currentTimeMillis();
            return;
        }

        System.out.println(message);

        this.message = message;
        if(message.startsWith("Paired with ") || message.contains("0")
                || message.contains("1")
                || message.contains("2") || message.contains("对手拔线投降")){
            if(this.client != null){
                this.client.receiveMessage(message);
            }
        }
    }

    public boolean isLoginSuccess() {
        return this.message.equals("Login:Success!");
    }

    public boolean isRegisterSuccess(){
        return this.message.equals("Register:Success!");
    }

    public static void main(String[] args) throws Exception {
        TicTacToeWebSocketClient wsc = new TicTacToeWebSocketClient(8888);
        wsc.connect();

        Thread.sleep(100);
        Thread t = new Thread(new ConnectionRunnable(wsc, "QWE"));
        t.start();
    }

    public void sendMessage(String message){
        while (!wsc.getReadyState().equals(ReadyState.OPEN));
        wsc.send(message);
    }

    public boolean heartBeat(){
        try {
            if(wsc.isClosed()){
                client.getController().setTerminated(true);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("服务器受不了，直接投降！");
                    alert.show();
                });
            }

            sendMessage("HEARTBEAT:"+ username);
            Thread.sleep(500);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        wsc.connect();
        while (heartBeat());
    }
}
