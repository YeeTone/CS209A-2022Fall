package lab10;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.stream.Collectors;


public class Practice {

    public static void main(String[] args) throws UnirestException {
        String url = "https://pokeapi.co/api/v2/pokemon/1/";
        HttpResponse<String> httpResponse = Unirest.get(url).asString();
        JSONObject jo = JSONObject.parseObject(httpResponse.getBody());
        System.out.printf("Name: %s%n",jo.getString("name"));
        System.out.printf("Height: %s%n", jo.getString("height"));
        System.out.printf("Weight: %s%n", jo.getString("weight"));
        System.out.print("Abilities: ");
        JSONArray ja = JSONArray.parseArray(jo.getString("abilities"));
        System.out.println(ja.stream().map(e -> (JSONObject)JSONObject.parse(e.toString()))
                .map(f -> f.get("ability"))
                        .map(g -> ((JSONObject) JSONObject.parse(g.toString())).getString("name"))
                .collect(Collectors.toList()));
    }
}
