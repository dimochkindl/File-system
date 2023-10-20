package users.user_operations;

import users.User;

import java.util.ArrayList;

public interface IAdminOperations {
    void deleteUser(User user);
    void addUser();
    void getUsersStatistics();
}
