package repo;

import com.alibaba.fastjson.JSONObject;
import database.DatabaseConnection;
import user.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public record Repo(Integer repoId, String repoOwner, String repoName) {
    public static Repo parse(JSONObject jsonObject) {
        Integer repoId = jsonObject.getInteger("id");
        String repoOwner = User.parse(jsonObject.getJSONObject("owner")).userLogin();
        String repoName = jsonObject.getString("name");
        return new Repo(repoId, repoOwner, repoName);
    }

    public void insertDatabase() throws SQLException {
        PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
                "insert into git_repo(repo_id, repo_owner, repo_name)" +
                        "values (?,?,?)");

        ps.setInt(1, repoId());
        ps.setString(2, repoOwner());
        ps.setString(3, repoName());
        ps.executeUpdate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repo repo = (Repo) o;
        return Objects.equals(repoId, repo.repoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repoId);
    }
}

