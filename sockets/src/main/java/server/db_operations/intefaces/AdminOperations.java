package server.db_operations.intefaces;

public interface AdminOperations extends FileOperationsInDB, UsersOperations {
    public boolean changeAdminPassword();

    public String getAdminPassword();

    public boolean addAdmin();

    public boolean changeAdmin();
}
