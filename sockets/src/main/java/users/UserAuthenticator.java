package users;

import server.db_operations.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthenticator {
    private final String username;
    private final String password;

    private int userId;

    public UserAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean checkUser() {
        Connector connector = new Connector();
        userId = -1;
        String foundPassword = null;
        String sqlQuery = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Can't connect to db to find user_id");
        }

        if (userId == -1) {
            return false;
        }

        sqlQuery = "SELECT password FROM users_info WHERE user_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                foundPassword = resultSet.getString("password");
            }
        } catch (SQLException ex) {
            System.out.println("Can't connect to db to find password");
        }

        if (foundPassword == null) {
            return false;
        }

        return foundPassword.equals(password);
    }

}
