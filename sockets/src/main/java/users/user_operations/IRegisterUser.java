package users.user_operations;

public interface IRegisterUser {
    default void register(String username, String email, String password){
    }
}
