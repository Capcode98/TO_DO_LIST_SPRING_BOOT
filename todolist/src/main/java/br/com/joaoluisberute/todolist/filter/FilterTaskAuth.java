package br.com.joaoluisberute.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.joaoluisberute.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if(servletPath.equals("/Task/CreateTask")){

            // Guarda na variavel authHeaderAuthorization o tipo de autorização e as informações da autorização em si na forma de BASE64 ("TypeAuth"+" "+"d5fs4654fvsdfzx342#$")
            var authHeaderAuthorization = request.getHeader("Authorization");

            // Guarda na variavel authorizationParsedToBase64Only 
            var authorizationParsedToBase64Only = authHeaderAuthorization.substring("Basic".length()).trim();

            var authorizationParsedToString = new String(Base64.getDecoder().decode(authorizationParsedToBase64Only));

            String[] arrayWithUserAndPassword = authorizationParsedToString.split(":");

            System.out.println(authorizationParsedToString);
            
            var username = arrayWithUserAndPassword[0];
            var password = arrayWithUserAndPassword[1];

            System.out.println("Username:");
            System.out.println(username.length());
            
            System.out.println("Password:");
            System.out.println(password);
            
                
            if(username.length() != 0 || password.length() != 0){

                //Guarda na variavel um todas as informações de um usuario caso seja encontrado pelo metodo "findByUsername" no DB
                var user = this.userRepository.findByUsername(username);

                var passwordCheck = BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());

                if(passwordCheck.verified){

                    request.setAttribute("idUser", user.getId());

                    filterChain.doFilter(request, response);

                }else{

                    response.sendError(401);

                }
            
            }else{

                response.sendError(401);
            }        

        }else{
                
            filterChain.doFilter(request, response);
        }
    }    
}
