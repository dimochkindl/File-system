package server.db_operations.intefaces;

import users.User;

import java.util.Map;
import java.util.Vector;

public interface UsersOperations {

    public Vector getUsers();

    public Map getFilesWithDate();

    public Vector getUsersFiles();

    public User deleteUser();

    public boolean addUser();

}
