package commit;

import com.alibaba.fastjson.JSONObject;
import database.DatabaseConnection;
import database.DateUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.util.List;

public record Commit(String commitSHA, String commitAuthorName,
                     Date commitDate, String repoOwner, String repoName,
                     String dayTimeLabel, String weekTimeLabel) {
    public static Commit parse(JSONObject jsonObject, String repoOwner, String repoName) throws ParseException {
        String commitSHA = jsonObject.getString("sha");
        String commitAuthorName = jsonObject.getJSONObject("commit")
                .getJSONObject("author").getString("name");
        String dateString = jsonObject.getJSONObject("commit")
                .getJSONObject("author").getString("date");
        Date date = DateUtils.TZStrToSQLDate(dateString);
        String dayTimeLabel = daytimeLabel(date);
        String weekTimeLable = weekTimeLabel(date);
        return new Commit(commitSHA, commitAuthorName, date,
                repoOwner, repoName, dayTimeLabel, weekTimeLable);
    }

    private static String daytimeLabel(Date date) {
        long time = date.getTime();
        java.util.Date d = new java.util.Date(time);
        int h = d.getHours();
        if (h < 6) {
            return "midnight";
        } else if (h < 9) {
            return "morning";
        } else if (h < 12) {
            return "forenoon";
        } else if (h < 14) {
            return "noon";
        } else if (h < 17) {
            return "afternoon";
        } else if (h < 19) {
            return "evening";
        } else if (h < 23) {
            return "night";
        } else {
            return "midnight";
        }
    }

    private static String weekTimeLabel(Date date) {
        long time = date.getTime();
        java.util.Date d = new java.util.Date(time);
        int day = d.getDay();
        return switch (day) {
            case 0 -> "Sunday";
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> "";
        };
    }

    public static void insertDatabaseMore(List<Commit> commitList) {
        try {
            Connection connection = DatabaseConnection.getInstance();
            connection.setAutoCommit(false);

            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
                    "insert into git_commit(commit_sha, commit_author_name, commit_date, " +
                            "repo_owner, repo_name, daytime_label, weektime_label)" +
                            "values (?,?,?,?,?,?,?)");
            for (int i = 0; i < commitList.size(); i++) {
                ps.setString(1, commitList.get(i).commitSHA());
                ps.setString(2, commitList.get(i).commitAuthorName());
                ps.setDate(3, commitList.get(i).commitDate());
                ps.setString(4, commitList.get(i).repoOwner());
                ps.setString(5, commitList.get(i).repoName());
                ps.setString(6, commitList.get(i).dayTimeLabel());
                ps.setString(7, commitList.get(i).weekTimeLabel());
                ps.addBatch();
                if (i % 20000 == 0) {
                    ps.executeBatch();
                    connection.commit();
                }
            }
            ps.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void insertDatabase() {
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
                    "insert into git_commit(commit_sha, commit_author_name, commit_date)" +
                            "values (?,?,?)");

            ps.setString(1, commitSHA());
            ps.setString(2, commitAuthorName());
            ps.setDate(3, commitDate());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
