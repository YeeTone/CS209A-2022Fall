import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        System.out.println(date.getDay());
    }
}
