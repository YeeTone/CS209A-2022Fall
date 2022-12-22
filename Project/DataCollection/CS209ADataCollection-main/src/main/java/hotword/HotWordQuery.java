package hotword;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import task1.Query;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HotWordQuery {
    public List<HotWord> query(String user, String repo, String state, int startPage, int needPage) {
        List<HotWord> hotWords = new ArrayList<>(100_0000);
        try {
            int perPage = 100, page = startPage;
            int cnt = 0;
            while (true) {
                String url = String.format("https://api.github.com/repos/%s/%s/issues?per_page=%d&page=%d&state=%s",
                        user, repo, perPage, page, state);
                PrintStream ps = System.out;
                System.setOut(new PrintStream(new ByteArrayOutputStream()));
                HttpResponse<String> response = Unirest.get(url)
                        .header("Authorization", Query.TOKEN)

                        .asString();
                System.setOut(ps);
                JSONArray ja = JSONArray.parseArray(response.getBody());
                //System.out.println(ja);
                for (Object o : ja) {
                    JSONObject jo = (JSONObject) o;
                    if (jo.containsKey("pull_request")) {
                        continue;
                    }

                    List<HotWord> titleHotWords = HotWord.parseTitle(jo.getString("title"), user, repo);
                    List<HotWord> bodyHotWords = HotWord.parseBody(jo.getString("body"), user, repo);
                    List<HotWord> commentsHotWords = HotWord.parseComments(queryComments(getCommentsURL(jo)), user, repo);

                    hotWords.addAll(titleHotWords);
                    hotWords.addAll(bodyHotWords);
                    hotWords.addAll(commentsHotWords);
                }
                cnt += ja.size();
                needPage -= 1;
                System.out.println(cnt);

                if (ja.size() < 100 || needPage <= 0) {
                    break;
                }
                page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotWords;
    }

    private List<HotWord> getAllWords(JSONObject jo) {
        int commentNumber = getCommentsNumber(jo);
        String commentURL = getCommentsURL(jo);
        return new ArrayList<>();
    }

    private int getCommentsNumber(JSONObject jo) {
        return jo.getIntValue("comments");
    }

    private String getCommentsURL(JSONObject jo) {
        return jo.getString("comments_url");
    }

    private JSONArray queryComments(String commentURL) throws UnirestException {
        PrintStream ps = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        HttpResponse<String> response = Unirest.get(commentURL)
                .header("Authorization", Query.TOKEN)

                .asString();
        System.setOut(ps);
        return JSONArray.parseArray(response.getBody());
    }

    public static void main(String[] args) {
        HotWordQuery hotWordQuery = new HotWordQuery();
        List<HotWord> hotWords = hotWordQuery.query("redis", "redis-py",
                "all",1, 1000);
        // 138-2
        System.out.println(hotWords.size());
        HotWord.insertDatabaseMore(hotWords);
        //hotWords.forEach(Issue::insertDatabase);
    }
}
