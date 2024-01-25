package br.com.joaoluisberute.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.NonNull;



@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/CreateUser")
    public ResponseEntity<UserModel> CreatUser(@NonNull @RequestBody UserModel userModel) {

        /*criptografando a senha*/
        userModel.setPassword(BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray()));

        /*criando variaveis para fazer a ferificação de username e email se já constam no baco de dados*/
        var user_name = this.userRepository.findByUsername(userModel.getUsername());
        var user_email = this.userRepository.findByEmail(userModel.getEmail());

        /*verificando se username e email já existem no banco de dados*/
        if( user_name != null ) {

            /*retornando a response contendo o status "BAD_REQUEST" --> 400 sinalisando que algumas das informações que deveriam ser unicas já consta no banco de dados*/
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }else if( user_email != null ) {

            /*retornando a response contendo o status "BAD_REQUEST" --> 400 sinalisando que algumas das informações que deveriam ser unicas já consta no banco de dados*/
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        /*salvando o usuario no banco de dados ao mesmo tempo que crio uma variavel para armazena-lo para retorna-lo no corpo do response*/
        var userCreated = this.userRepository.save(userModel);
        
        /*retornando a response contendo o usuario criado e salvo no banco de dados alem do status "OK" --> 200 */
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }
}
