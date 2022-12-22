package release;

import com.alibaba.fastjson.JSONObject;
import database.DatabaseConnection;
import database.DateUtils;

import java.sql.Date;
import java.sql.PreparedStatement;

public record Release(Integer releaseId, Date publishedAt, String authorLogin,
                      String releaseName,
                      String repoOwner, String repoName) {

    public static Release parse(JSONObject jsonObject, String repoOwner, String repoName) {
        Integer releaseId = jsonObject.getInteger("id");
        Date publishedAt = DateUtils.TZStrToSQLDate(jsonObject.getString("published_at"));
        String authorLogin = jsonObject.getJSONObject("author").getString("login");
        String releaseName = jsonObject.getString("name");
        return new Release(releaseId, publishedAt, authorLogin, releaseName, repoOwner, repoName);
    }

    public void insertDatabase() {
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
                    "insert into git_release(release_id, published_at , author_login, release_name, repo_owner, repo_name)" +
                            "values (?,?,?,?,?,?)");

            ps.setInt(1, releaseId());
            ps.setDate(2, publishedAt());
            ps.setString(3, authorLogin());
            ps.setString(4, releaseName());
            ps.setString(5, repoOwner());
            ps.setString(6, repoName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
