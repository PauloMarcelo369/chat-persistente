package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConnectionToDataBase;
import java.sql.Connection;
import entitys.UserEntity;

public class UserDao {
    private final Connection connection = ConnectionToDataBase.getConnection();

    public void insert(UserEntity user) throws SQLException{
        String sql = "INSERT INTO Users (username) VALUES (?)";
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.execute();
        preparedStatement.close();            
        System.out.println("novo cliente cadastrado: " + user.getUserName());
        
    }
    
    public Boolean userExists(UserEntity user) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    public ResultSet getUsers() throws SQLException {
        String sql = "SELECT * FROM Users";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
