package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entitys.MessageEntity;
import util.ConnectionToDataBase;


public class MessageDao {
    private Connection connection = ConnectionToDataBase.getConnection();

    public void insertMsg(MessageEntity message) throws SQLException{
        String sql = "INSERT INTO messages (sender, receiver, content, timestamp) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, message.getSender());
        preparedStatement.setString(2, message.getReceiver());
        preparedStatement.setString(3, message.getContent());
        preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(message.getTimeStamp()));
        preparedStatement.execute();
        preparedStatement.close();
    }

    public ResultSet getMessages(String sender, String receiver) throws SQLException{
        String sql = "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, sender);
        preparedStatement.setString(2, receiver);
        preparedStatement.setString(3, receiver);
        preparedStatement.setString(4, sender);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
