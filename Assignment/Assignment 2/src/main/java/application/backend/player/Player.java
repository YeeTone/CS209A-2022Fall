package application.backend.player;

import java.util.Objects;

public class Player {

    public static final Player NULL = new Player(null, null, 0, 0);

    private Player currentPaired;

    private final String username;
    private final String password;
    private int total;
    private int win;

    public Player(String username, String password, int total, int win) {
        this.username = username;
        this.password = password;
        this.total = total;
        this.win = win;
    }

    public Player getCurrentPaired() {
        return currentPaired;
    }

    public void setCurrentPaired(Player currentPaired) {
        this.currentPaired = currentPaired;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getTotal() {
        return total;
    }

    public int getWin() {
        return win;
    }

    public void win(){
        this.total +=1;
        this.win +=1;
        PlayerLib.write(this);
    }

    public void lose(){
        this.total += 1;
        PlayerLib.write(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return username.equals(player.username) && password.equals(player.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
