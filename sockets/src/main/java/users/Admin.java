package users;

import users.user_operations.IAdminOperations;

import java.util.ArrayList;

public final class Admin extends User implements IAdminOperations {
    private final static String username = "admin";
    private final static String password = "00admin00";


    public Admin() {
        super(username, "", password);
    }


    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void addUser() {

    }

    @Override
    public void getUsersStatistics() {

    }
}