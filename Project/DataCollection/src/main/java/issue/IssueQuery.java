package issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import commit.Commit;
import task1.Query;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class IssueQuery {

    public List<Issue> query(String user, String repo, String state){
        List<Issue> issues = new ArrayList<>();
        try{
            int perPage = 100, page = 1;
            int cnt = 0;
            while (true) {
                String url = String.format("https://api.github.com/repos/%s/%s/issues?per_page=%d&page=%d&state=%s",
                        user, repo, perPage, page, state);
                //PrintStream ps = System.out;
                //System.setOut(new PrintStream(new ByteArrayOutputStream()));
                HttpResponse<String> response = Unirest.get(url)
                        .header("Authorization", Query.TOKEN)

                        .asString();
                //System.setOut(ps);
                JSONArray ja = JSONArray.parseArray(response.getBody());
                for (Object o : ja) {
                    JSONObject jo = (JSONObject) o;
                    if(jo.containsKey("pull_request")){
                        continue;
                    }

                    issues.add(Issue.parse(jo, user, repo));
                }
                cnt += ja.size();
                System.out.println(cnt);

                if (ja.size() < 100) {
                    break;
                }
                page++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return issues;
    }

    public static void main(String[] args) {
        IssueQuery issueQuery = new IssueQuery();
        List<Issue> issues = issueQuery.query("redis", "redis-py", "all");
        System.out.println(issues.size());
        issues.forEach(Issue::insertDatabase);
    }
}
