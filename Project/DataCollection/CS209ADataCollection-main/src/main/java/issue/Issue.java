package issue;

import com.alibaba.fastjson.JSONObject;
import database.DatabaseConnection;
import database.DateUtils;

import java.sql.PreparedStatement;
import java.text.ParseException;
import java.sql.Date;

public record Issue(Integer issueId, Date createdAt, String title,
                    Date closedAt, String creatorLogin, String state,
                    String repoOwner, String repoName) {

    public static Issue parse(JSONObject jsonObject, String repoOwner, String repoName) throws ParseException {
        Integer issueId = jsonObject.getInteger("id");
        Date createdAt = DateUtils.TZStrToSQLDate(jsonObject.getString("created_at"));
        String title = jsonObject.getString("title");
        Date closedAt = DateUtils.TZStrToSQLDate(jsonObject.getString("closed_at"));
        String creatorLogin = jsonObject.getJSONObject("user").getString("login");
        String state = jsonObject.getString("state");
        return new Issue(issueId, createdAt, title, closedAt, creatorLogin, state, repoOwner, repoName);
    }

    public void insertDatabase() {
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
                    "insert into git_issue(issue_id, create_at, title, closed_at, creator_login, state, repo_owner, repo_name)" +
                            "values (?,?,?,?,?,?,?,?)");

            ps.setInt(1, issueId());
            ps.setDate(2, createdAt());
            ps.setString(3, title());
            ps.setDate(4, closedAt());
            ps.setString(5, creatorLogin());
            ps.setString(6, state());
            ps.setString(7, repoOwner());
            ps.setString(8, repoName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
