package server.db_operations.intefaces;

import users.User;

import java.util.Map;
import java.util.Vector;

public interface UsersOperations {

    Vector getUsers();

    Map getFilesWithDate();

    Vector getUsersFiles();

    User deleteUser();

    boolean addUser();

}
