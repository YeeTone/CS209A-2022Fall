package application.backend.player;

import org.java_websocket.WebSocket;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerLib {
    private static final int DEFAULT_TOTAL = 0;
    private static final int DEFAULT_WIN = 0;

    private static final List<Player> registeredPlayers = new CopyOnWriteArrayList<>();

    private static final Map<Player, WebSocket> onlinePlayers = new ConcurrentHashMap<>();
    private static final Map<WebSocket, Long> webSocketHeartBeatTime = new ConcurrentHashMap<>();

    private PlayerLib() {
        throw new RuntimeException();
    }

    static {
        loadAll();
    }

    private static void loadAll() {
        File[] files = new File(".").listFiles();
        assert files != null;
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".player")) {
                load(f);
            }

        }
    }

    private static void load(File f) {
        if (!f.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String username = br.readLine();
            String password = br.readLine();
            int total = Integer.parseInt(br.readLine());
            int win = Integer.parseInt(br.readLine());

            if(registeredPlayers.stream().noneMatch(p -> p.getUsername().equals(username))){
                registeredPlayers.add(new Player(username, password, total, win));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean exist(String username) {
        return registeredPlayers.stream()
                .anyMatch(p -> p.getUsername().equals(username));
    }

    public static boolean register(String username, String password) {
        if (exist(username)) {
            return false;
        }
        Player p = new Player(username, password, DEFAULT_TOTAL, DEFAULT_WIN);
        registeredPlayers.add(p);
        write(p);

        return true;
    }

    public static void write(Player p) {
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(p.getUsername() + ".player"))) {
            pw.println(p.getUsername());
            pw.println(p.getPassword());
            pw.println(p.getTotal());
            pw.println(p.getWin());
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean heartBeat(WebSocket webSocket){

        if(webSocketHeartBeatTime.containsKey(webSocket)){
            long lastTime = webSocketHeartBeatTime.get(webSocket);
            if(System.currentTimeMillis() - lastTime <= 60 * 1000){
                webSocketHeartBeatTime.put(webSocket, System.currentTimeMillis());
                return true;
            }
            return false;
        }

        webSocketHeartBeatTime.put(webSocket, System.currentTimeMillis());
        return true;
    }

    public static WebSocket getWebSocketConnection(String username){
        return onlinePlayers.get(selectPlayer(username));
    }

    public static Player getPlayer(WebSocket webSocket){
        return onlinePlayers.entrySet().stream().filter(e -> e.getValue().equals(webSocket))
                .findFirst().get().getKey();
    }

    public static boolean login(String username, String password, WebSocket webSocket) {
        loadAll();
        if(registeredPlayers.stream()
                .anyMatch(p -> p.getUsername().equals(username)
                        && p.getPassword().equals(password))){
            Player player = registeredPlayers.stream().filter(
                    p -> p.getUsername().equals(username)
                    && p.getPassword().equals(password)).findFirst().get();
            onlinePlayers.put(player, webSocket);
            return true;
        }

        return false;
    }

    public static Player selectPlayer(String username){
        load(new File(username + ".player"));
        return registeredPlayers.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst().orElse(Player.NULL);
    }
}
