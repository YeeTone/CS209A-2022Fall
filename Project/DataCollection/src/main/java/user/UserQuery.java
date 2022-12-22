package user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;

import static task1.Query.TOKEN;

public class UserQuery {

    public User querySingleUser(String userLogin, boolean isLogin){
        try{
            if(isLogin){
                String url = String.format("https://api.github.com/users/%s", userLogin);
                HttpResponse<String> response = Unirest.get(url)
                        .header("Authorization", TOKEN).asString();
                JSONObject jo = JSONObject.parseObject(response.getBody());
                return User.parse(jo);
            }else {
                String url = String.format("https://api.github.com/search/users?q=fullname:%s", userLogin);
                HttpResponse<String> response = Unirest.get(url)
                        .header("Authorization", TOKEN).asString();
                System.out.println(response.getBody());
                System.exit(0);
                JSONArray ja = JSONArray.parseArray(response.getBody());

                return User.parse(ja.getJSONObject(0));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) throws UnirestException, SQLException {
        UserQuery userQuery = new UserQuery();
        User u = userQuery.querySingleUser("redis", true);
        u.insertDatabase();
    }
}
