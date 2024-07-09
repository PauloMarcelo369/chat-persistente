package util;

// import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;





import models.Message;

import java.util.ArrayList;


public  class JsonTratament{

    private static final Gson gson = new Gson();

    public static <U> String toJson(U object){
        return gson.toJson(object);
    }

    public static <U> U fromJson(String json, Class<U> genericClass){
        return gson.fromJson(json, genericClass);
    }

    public static String resultSetToJson(ResultSet resultSet, Operation operation) throws SQLException{
        JsonArray jsonArray = new JsonArray();
        while (resultSet.next()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject = operation.toJsonObject(resultSet);
            jsonArray.add(jsonObject);
        }
        return JsonTratament.toJson(jsonArray);
    }

    public static ArrayList<String> jsonArrayToList(String arrayjson){
        ArrayList<String> nameList = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(arrayjson).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray){
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("username")){
                String username = jsonObject.get("username").getAsString();
                nameList.add(username);
            }
        }
        return nameList;
    }

    public static ArrayList<Message> jsonListToArrayList(String arrayjson){
        ArrayList<Message> messages = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(arrayjson).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String sender = jsonObject.get("sender").getAsString();
            String receiver = jsonObject.get("receiver").getAsString();
            String content = jsonObject.get("content").getAsString();
            LocalDateTime dateTime = LocalDateTime.parse(jsonObject.get("timestamp").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            messages.add(new Message(sender, receiver, content, dateTime));
        }
        return messages;
    }
}
