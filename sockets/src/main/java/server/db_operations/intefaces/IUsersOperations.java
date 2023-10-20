package server.db_operations.intefaces;

import users.User;

import java.sql.Date;
import java.util.Map;
import java.util.Vector;

public interface IUsersOperations {

    public Vector getUsers();

    public Map getFilesWithDate();

    public Vector getUsersFiles();

    public User deleteUser();

    public boolean addUser();

}
