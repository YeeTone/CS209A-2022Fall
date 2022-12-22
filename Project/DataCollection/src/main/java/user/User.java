package user;

import com.alibaba.fastjson.JSONObject;
import database.DatabaseConnection;
import task1.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public record User(
        String userLogin,
        String fullName,
        Integer userId,
        String email
) {

    public static final User NULL = new User(null, null, 0, null);

    public static User parse(JSONObject jsonObject) {
        try {
            String login = jsonObject.getString("login");
            String fullName = jsonObject.getString("name");
            Integer userId = jsonObject.getInteger("id");
            String email = jsonObject.getString("email");
            return new User(login, fullName, userId, email);
        } catch (Exception e) {
            return NULL;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userLogin, user.userLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLogin);
    }

    public void insertDatabase() throws SQLException {
        PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement("insert into git_user(user_login, full_name, user_id, email)" +
                "values (?,?,?,?)");

        ps.setString(1, userLogin());
        ps.setString(2, fullName());
        ps.setInt(3, userId());
        ps.setString(4, email());
        ps.executeUpdate();
    }
}
