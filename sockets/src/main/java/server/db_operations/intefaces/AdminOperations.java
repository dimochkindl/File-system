package server.db_operations.intefaces;

public interface AdminOperations extends FileOperationsInDB, UsersOperations {
    boolean changeAdminPassword();

    String getAdminPassword();

    boolean addAdmin();

    boolean changeAdmin();
}
