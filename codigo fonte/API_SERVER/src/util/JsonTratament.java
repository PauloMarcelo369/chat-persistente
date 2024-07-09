package util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
}
