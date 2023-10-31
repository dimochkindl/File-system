package users;

import server.db_operations.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public boolean registerUser(String email) {
        getUserID();
        userId++;
        Connector connector = new Connector();
        Connection connection = null;
        PreparedStatement userInsertStatement = null;
        PreparedStatement userInfoInsertStatement = null;

        try {
            connection = connector.getConnection();
            connection.setAutoCommit(false);

            String userInsertSQL = "INSERT INTO users (id, username, upload_files, download_files) VALUES(?, ?, ?, ?)";
            String userInfoInsertSQL = "INSERT INTO users_info (user_id, email, password) VALUES (?, ?, ?)";

            userInsertStatement = connection.prepareStatement(userInsertSQL);
            userInsertStatement.setInt(1, userId);
            userInsertStatement.setString(2, username);
            userInsertStatement.setInt(3, 0);
            userInsertStatement.setInt(4, 0);
            userInsertStatement.executeUpdate();

            userInfoInsertStatement = connection.prepareStatement(userInfoInsertSQL);
            userInfoInsertStatement.setInt(1, userId);
            userInfoInsertStatement.setString(2, email);
            userInfoInsertStatement.setString(3, password);
            userInfoInsertStatement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            e.printStackTrace();
            return false;
        } finally {
            try {
                if (userInfoInsertStatement != null) {
                    userInfoInsertStatement.close();
                }
                if (userInsertStatement != null) {
                    userInsertStatement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }


    private void getUserID() {
        Connector connector = new Connector();
        Connection connection = null;
        PreparedStatement userInsertStatement = null;
        PreparedStatement userInfoInsertStatement = null;

        connection = connector.getConnection();
        PreparedStatement statement = null;
        try {
            String query = "SELECT id FROM users;";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userId++;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getSQLState());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
