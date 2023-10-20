package server.db_operations.intefaces;

public interface IAdminOperations extends IFileOperationsInDB, IUsersOperations{
    public boolean changeAdminPassword();

    public String getAdminPassword();

    public boolean addAdmin();

    public boolean changeAdmin();
}
