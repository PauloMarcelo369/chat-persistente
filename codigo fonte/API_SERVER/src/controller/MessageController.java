package controller;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonObject;
import util.JsonTratament;


import dao.MessageDao;
import entitys.MessageEntity;

public class MessageController {
    MessageDao messageDao;

    public MessageController(){
        messageDao = new MessageDao();
    }

    public void insertMsg(String json){
        try{
            messageDao.insertMsg(JsonTratament.fromJson(json, MessageEntity.class));
        }
        catch(SQLException e){
            System.out.println("Error ao tentar inserir nova mensagem: " + e.getMessage());
        }
    }

    public String getMessages(String json) {
        try {
            MessageEntity messageEntity = JsonTratament.fromJson(json, MessageEntity.class);
            String receiver = messageEntity.getReceiver();

            try (ResultSet resultSet = messageDao.getMessages(messageEntity.getSender(), messageEntity.getReceiver())) {
                if (resultSet.wasNull()) return null;
                return receiver + "::" + JsonTratament.resultSetToJson(resultSet, result -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("sender", result.getString("sender"));
                    jsonObject.addProperty("receiver", result.getString("receiver"));
                    jsonObject.addProperty("content", result.getString("content"));
                    LocalDateTime timestamp = result.getTimestamp("timestamp").toLocalDateTime();
                    String formattedTimestamp = timestamp.format(formatter);
                    jsonObject.addProperty("timestamp", formattedTimestamp);
                    return jsonObject;
                });
            }
        } catch (SQLException e) {
            System.out.println("Error ao tentar resgatar mensagens antigas: " + e.getMessage());
            return null;
        }
    }
}