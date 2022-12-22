package release;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import issue.Issue;
import issue.IssueQuery;
import task1.Query;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ReleaseQuery {
    public List<Release> query(String user, String repo){
        List<Release> releases = new ArrayList<>();
        try{
            int perPage = 100, page = 1;
            int cnt = 0;
            while (true) {
                String url = String.format("https://api.github.com/repos/%s/%s/releases?per_page=%d&page=%d",
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
                    releases.add(Release.parse(jo, user, repo));
                }
                cnt += ja.size();

                if (ja.size() < 100) {
                    break;
                }
                page++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return releases;
    }

    public static void main(String[] args) {
        ReleaseQuery rq = new ReleaseQuery();
        List<Release> releases = rq.query("redis", "redis-py");
        System.out.println(releases.size());
        releases.forEach(Release::insertDatabase);
        //
    }
}
