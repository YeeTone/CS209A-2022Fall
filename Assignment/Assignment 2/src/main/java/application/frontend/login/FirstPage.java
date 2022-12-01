package application.frontend.login;

import application.frontend.Client;
import application.frontend.connection.ConnectionRunnable;
import application.frontend.connection.TicTacToeWebSocketClient;
import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This is a non-sense javadoc
 *
* */
public class FirstPage extends Application {
    private Label username;
    private Label password;
    private TextField inputUsername;
    private PasswordField inputPassword;
    private Button login;
    private Button clear;
    private Button register;
    private Stage primaryStage;

    private GridPane pane;

    private boolean connected = false;

    private TicTacToeWebSocketClient wsc;
    private ConnectionRunnable ct;
    private Thread connectionThread;

    @Override
    public void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      buildElements();
      buildGridPanel();
      buildActionListener();
      buildScene(primaryStage);

        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    private void buildConnection(String username, int port) {
      if (connected) {
        return;
      }

      try {
        wsc = new TicTacToeWebSocketClient(port);
        ct = new ConnectionRunnable(wsc, username);
        wsc.setHb(ct);
        connectionThread = new Thread(ct);

        connectionThread.start();
            connected = true;
        } catch (Exception ignored) {

        }
    }

    private void buildElements() {
        username = new Label("Name:");
        username.setFont(new Font(20));
        username.setTooltip(new Tooltip("Please input your username"));

        password = new Label("Password:");
        password.setFont(new Font(20));
        password.setTooltip(new Tooltip("Please input your password"));

        inputUsername = new TextField();
        inputPassword = new PasswordField();
        login = new Button("Login");
        clear = new Button("Clear");
        register = new Button("Register");
    }

    private void buildGridPanel() {
        pane = new GridPane();
        pane.setStyle("-fx-background-color: #39c5bb");
        pane.add(username, 0, 0);
        pane.add(password, 0, 1);
        pane.add(inputUsername, 1, 0);
        pane.add(inputPassword, 1, 1);
        pane.add(login, 0, 2);
        pane.add(clear, 1, 2);
        pane.add(register, 2, 2);

        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10); //设置水平间距
        pane.setVgap(17); //设置垂直间距
        GridPane.setMargin(clear, new Insets(0, 0, 0, 60));
    }

    private void buildActionListener() {
        Objects.requireNonNull(clear).setOnAction(e -> clear());

        Objects.requireNonNull(login).setOnAction(e -> {
            Alert alert;
            if (loginVerify(true)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Login Success!");
                switchLogin();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Login Failure!");
            }
            alert.show();

        });

        Objects.requireNonNull(register).setOnAction(e -> {
            Alert alert;
            if (registerVerify(true)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Register Success!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Register Failure!");
            }
            alert.show();
        });

    }

    private void clear() {
        inputUsername.setText("");
        inputPassword.setText("");
    }

    private boolean loginVerify(boolean recursive1) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        buildConnection(username, 8888);
        ct.sendMessage("LOGIN:" + username + "@" + password);

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return recursive1? loginVerify(false): ct.isLoginSuccess();
    }

    private void switchLogin() {
        this.primaryStage.close();
        new Client(wsc, ct, connectionThread).start(new Stage());

    }

    private boolean registerVerify(boolean recursive1){
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        buildConnection(username, 8888);
        ct.sendMessage("REGISTER:" + username + "@" + password);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return ct.isRegisterSuccess();
    }

    private void buildScene(Stage primaryStage) {
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Java FX - 登录页面 ");
        primaryStage.setWidth(500);
        primaryStage.setHeight(300);
        primaryStage.setResizable(false); //登录窗口的大小不允许改变
        primaryStage.show();
    }

}
