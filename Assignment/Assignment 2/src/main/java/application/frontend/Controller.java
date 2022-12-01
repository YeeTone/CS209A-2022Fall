package application.frontend;

import application.backend.player.Player;
import application.backend.player.PlayerLib;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import javafx.application.Platform;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final char PLAY_1 = 'O';
    private static final char PLAY_2 = 'X';
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @FXML
    private Pane base_square;

    @FXML
    private Button pairButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button info;

    @FXML
    private Label pairedWith;

    @FXML
    private Rectangle game_panel;

    private boolean isParing = false;
    private boolean isTerminated = false;
    private String pairedUser;

    private boolean initialized = false;
    private char myTURN;
    private char currentTURN = 'X';

    private char[][] chessBoard = new char[3][3];
    private boolean[][] flag = new boolean[3][3];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game_panel.setOnMouseClicked(event -> {
            if(!initialized){
                myTURN = 'X';
                initialized = true;
            }

            if(myTURN != currentTURN){
                return;
            }

            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            refreshBoard(x, y, myTURN);
        });

        pairButton.setOnMouseClicked(e -> askForPair());

        exitButton.setOnMouseClicked(e -> client.exit());

        info.setOnMouseClicked(e -> showInfo());

    }

    private void showInfo(){
        String userInfo = client.getConnectionRunnable().getUsername();
        Player p = PlayerLib.selectPlayer(userInfo);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("name: %s, password: %s, total: %d, win: %d",
                    p.getUsername(), p.getPassword(), p.getTotal(), p.getWin()));
            alert.show();
        });
    }

    public void setTerminated(boolean terminated) {
        isTerminated = terminated;
    }

    private void switchTURN(){
        currentTURN = opponent(currentTURN);
    }

    private char opponent(char c){
        if(c == 'O'){
            return 'X';
        }else {
            return 'O';
        }
    }

    private void askForPair() {
        if(isParing){
            return;
        }

        isParing = true;
        this.client.getConnectionRunnable().sendMessage("RANDOMPAIR:" + client.getConnectionRunnable().getUsername());
    }

    public void receiveMessage(String message) {
        if (message.startsWith("Paired with ")) {
            setPaired(true, message);
        }else if(message.startsWith("0")|| message.startsWith("1")||message.startsWith("2")){
            putChessByOpponent(message);
        }else if(message.contains("对手拔线投降") && !isTerminated){
            isTerminated = true;
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(myTURN+" wins the game!");
                alert.show();
                String winnerName = this.client.getConnectionRunnable().getUsername();
                String loserName = this.pairedUser;
                PlayerLib.selectPlayer(winnerName).win();
                PlayerLib.selectPlayer(loserName).lose();
            });
        }
    }

    private void putChessByOpponent(String message){
        String[] ss = message.split("\n");
        String[] location = ss[0].split(" ");
        int x = Integer.parseInt(location[0]), y = Integer.parseInt(location[1]);
        refreshBoard(x, y, location[2].charAt(0));

        if(!initialized){
            myTURN = opponent(location[2].charAt(0));
            initialized = true;
        }
    }

    public void setPaired(boolean paired, String message) {
        this.isParing = paired;

        if (paired) {
            char last = message.charAt(message.length() - 1);
            myTURN = opponent(last);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(message);
                alert.show();
                pairedWith.setText(message);
            });
        }

        this.pairedUser = message.replace("Paired with ", "");

    }

    private void refreshBoard(int x, int y, char chess) {
        if (!isParing || isTerminated) {
            return;
        }

        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = chess;
            drawChess();

            switchTURN();
            if(chess == myTURN){
                sendToOpponent(x, y, chess);
            }

            if(WinnerChecker.winner(chessBoard) == 'F') {
                isTerminated = true;
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Draw!");
                    alert.show();
                    handleWinner();
                });
            } else if(WinnerChecker.winner(chessBoard) != 'N'){
                isTerminated = true;
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(WinnerChecker.winner(chessBoard)+" wins the game!");
                    alert.show();
                    handleWinner();
                });
            }
        }
    }

    private void handleWinner(){
        String winnerName = WinnerChecker.winner(chessBoard) == myTURN ?
                this.client.getConnectionRunnable().getUsername():
                this.pairedUser;
        String loserName = WinnerChecker.winner(chessBoard) != myTURN ?
                this.client.getConnectionRunnable().getUsername():
                this.pairedUser;
        if(WinnerChecker.winner(chessBoard) == 'F') {
            PlayerLib.selectPlayer(winnerName).lose();
        }else {
            PlayerLib.selectPlayer(winnerName).win();
        }

        PlayerLib.selectPlayer(loserName).lose();
    }

    private void sendToOpponent(int i, int j, char chess){
        String message = formMessage(i, j, chess);
        System.out.println("M:"+message);
        client.getConnectionRunnable().sendMessage(message);
    }

    public String getPairedUser() {
        return pairedUser;
    }

    private String formMessage(int i, int j, char chess){

        return "PUTCHESS:" +
                "TOUSER-" + pairedUser.substring(0, pairedUser.length() - 1) + "#\n" +
                i + " " + j +" "+chess+ "\n" +
                WinnerChecker.winner(chessBoard);
    }

    private void drawChess() {
        Platform.runLater(() -> {
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard[0].length; j++) {
                    if (flag[i][j]) {
                        // This square has been drawing, ignore.
                        continue;
                    }
                    switch (chessBoard[i][j]) {
                        case PLAY_1:
                            drawCircle(i, j);
                            break;
                        case PLAY_2:
                            drawLine(i, j);
                            break;
                        case EMPTY:
                            // do nothing
                            break;
                        default:
                            System.err.println("Invalid value!");
                    }
                }
            }
        });

    }

    private void drawCircle(int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine(int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }
}
