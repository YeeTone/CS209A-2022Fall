package repo;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;

import static task1.Query.TOKEN;

public class RepoQuery {
    public Repo querySingleRepo(String repoOwner, String repoName) throws UnirestException {
        String url = String.format("https://api.github.com/repos/%s/%s", repoOwner, repoName);
        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", TOKEN).asString();
        JSONObject jo = JSONObject.parseObject(response.getBody());
        return Repo.parse(jo);
    }

    public static void main(String[] args) throws UnirestException, SQLException {
        Repo r = new RepoQuery().querySingleRepo("redis", "redis-py");
        r.insertDatabase();
    }
}
