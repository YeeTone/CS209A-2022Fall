package hotword;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import database.DatabaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

public record HotWord(String type, String word,
                      String repoOwner, String repoName) {
    public static final String STOPWORD_URL = "D:\\IdeaProjects\\CS209ADataCollection\\src\\main\\resources\\stopwords-en.txt";
    public static final Set<String> STOPWORDS = new HashSet<>();

    private static boolean prepared = false;

    public static void prepareStopWords() {
        if (prepared) {
            return;
        }

        File f = new File(STOPWORD_URL);
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String s;
            while ((s = br.readLine()) != null) {
                STOPWORDS.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepared = true;

    }

    public static List<HotWord> parseTitle(String title,
                                           String repoOwner, String repoName) {
        return Arrays.stream(title.split(" "))
                .map(e -> new HotWord("title", e.toLowerCase(), repoOwner, repoName))
                .filter(HotWord::isAllEnglishLetter)
                .filter(HotWord::notStopWord)
                .collect(Collectors.toList());
    }

    public static List<HotWord> parseBody(String body,
                                          String repoOwner, String repoName) {
        if (body == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(body.split(" "))
                .map(e -> new HotWord("Body", e.toLowerCase(), repoOwner, repoName))
                .filter(HotWord::isAllEnglishLetter)
                .filter(HotWord::notStopWord)
                .collect(Collectors.toList());
    }

    public static List<HotWord> parseComments(JSONArray commentsJSONArray,
                                              String repoOwner, String repoName) {
        List<HotWord> hotWords = new ArrayList<>();
        for (Object o : commentsJSONArray) {
            JSONObject jo = (JSONObject) o;
            String commentBody = jo.getString("body");
            hotWords.addAll(parseBody(commentBody, repoOwner, repoName));
        }

        return hotWords;
    }

    public boolean isAllEnglishLetter() {
        return word().matches("^[a-zA-Z]+$");
    }

    public boolean notStopWord() {
        prepareStopWords();
        return !STOPWORDS.contains(word());
    }

    public static void insertDatabaseMore(List<HotWord> hotWordList) {
        try {
            Connection connection = DatabaseConnection.getInstance();
            connection.setAutoCommit(false);

            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
                    "insert into git_hotword(type, word, repo_owner, repo_name) values (?,?,?,?)");
            for (int i = 0; i < hotWordList.size(); i++) {


                ps.setString(1, hotWordList.get(i).type());
                ps.setString(2, hotWordList.get(i).word());
                ps.setString(3, hotWordList.get(i).repoOwner());
                ps.setString(4, hotWordList.get(i).repoName());
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

}
