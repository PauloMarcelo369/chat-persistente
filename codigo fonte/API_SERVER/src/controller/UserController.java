package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.UserDao;
import entitys.UserEntity;
import com.google.gson.JsonObject;
import util.JsonTratament;


public class UserController {
    UserDao userDao;

    public UserController(){
        userDao = new UserDao();
    }

    public void insertUser(String json){
        System.out.println("Esse é o json que chegou: " + json);
        try {
            if (userExists(json)) return;
            
            userDao.insert(JsonTratament.fromJson(json, UserEntity.class));

        } catch (SQLException e) {
            System.out.println("Error ao tentar inserir usuario: " + e.getMessage());
        }
    }

    public Boolean userExists(String json){
        try {
            return userDao.userExists(JsonTratament.fromJson(json, UserEntity.class));
        } catch (SQLException e) {
            System.out.println("Error ao tentar verificar a existencia do usuario: " + e.getMessage());
            return false;
        }
    }

    public String getUsers(){
        try (ResultSet resultSet = userDao.getUsers()) {
            return JsonTratament.resultSetToJson(resultSet, 
                                result -> {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("username", result.getString("username"));
                                    return jsonObject;
                                });
        } catch (SQLException e) {
            System.out.println("Error ao tentar resgatar os usuarios: " + e.getMessage());
            return null;
        }
    }
}
