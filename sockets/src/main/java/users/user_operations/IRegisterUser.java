package users.user_operations;

public interface IRegisterUser {
    public default void register(String username, String email, String password){
    }
}
