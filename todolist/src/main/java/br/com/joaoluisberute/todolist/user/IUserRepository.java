package br.com.joaoluisberute.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;



public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);
    UserModel findByUsername(String username);

}