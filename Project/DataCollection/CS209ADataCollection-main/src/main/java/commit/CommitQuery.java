package commit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import task1.Query;
import user.User;
import user.UserQuery;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommitQuery{

    public List<Commit> query(String user, String repo){
        List<Commit> commits = new ArrayList<>();
        try{
            int perPage = 100, page = 1;
            while (true) {
                String url = String.format("https://api.github.com/repos/%s/%s/commits?per_page=%d&page=%d",
                        user, repo, perPage, page);
                PrintStream ps = System.out;
                System.setOut(new PrintStream(new ByteArrayOutputStream()));
                HttpResponse<String> response = Unirest.get(url)
                        .header("Authorization", Query.TOKEN)

                        .asString();
                System.setOut(ps);
                JSONArray ja = JSONArray.parseArray(response.getBody());
                for (Object o : ja) {
                    JSONObject jo = (JSONObject) o;
                    commits.add(Commit.parse(jo, user, repo));
                }

                if (ja.size() < 100) {
                    break;
                }
                page++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return commits;
    }

    public List<User> extractCommitters(List<Commit> commits){
        UserQuery uq = new UserQuery();
        return commits.stream().map(Commit::commitAuthorName)
                .map(s -> uq.querySingleUser(s,false))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        CommitQuery cq = new CommitQuery();
        List<Commit> commits = cq.query("redis", "redis-py");
        System.out.println(commits.size());
        Commit.insertDatabaseMore(commits);
    }

}
