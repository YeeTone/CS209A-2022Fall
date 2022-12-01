package application.frontend;

import application.frontend.connection.ConnectionRunnable;
import application.frontend.connection.TicTacToeWebSocketClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.java_websocket.framing.CloseFrame;

import java.net.URL;
import java.util.Optional;

public class Client extends Application {

    private TicTacToeWebSocketClient wsc;
    private ConnectionRunnable connectionRunnable;
    private Thread connectionThread;

    private Controller controller;

    public Client(){

    }

    public Client(TicTacToeWebSocketClient wsc, ConnectionRunnable ct, Thread connectionThread) {
        this.wsc = wsc;
        this.connectionRunnable = ct;
        this.connectionThread = connectionThread;
        this.connectionRunnable.setClient(this);
    }

    public void receiveMessage(String message){
        if(controller != null){
            controller.receiveMessage(message);
        }
    }

    public ConnectionRunnable getConnectionRunnable() {
        return connectionRunnable;
    }

    public Thread getConnectionThread() {
        return connectionThread;
    }

    public TicTacToeWebSocketClient getWsc() {
        return wsc;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(new URL("file:///D:\\IdeaProjects\\CS209A-A22\\resources\\mainUI.fxml"));
            Pane root = fxmlLoader.load();
            Scene rootScene = new Scene(root);
            controller = fxmlLoader.getController();
            controller.setClient(this);

            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(rootScene);
            primaryStage.setResizable(false);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                exit();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        free();
        System.exit(0);
    }

    public void free(){

        if(wsc == null){
            return;
        }

        if(controller.getPairedUser() != null){
            wsc.close(CloseFrame.NORMAL, controller.getPairedUser()+":对手拔线投降");
        }else {
            wsc.close(CloseFrame.NORMAL);
        }
        connectionThread.interrupt();
    }

    public Controller getController() {
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
