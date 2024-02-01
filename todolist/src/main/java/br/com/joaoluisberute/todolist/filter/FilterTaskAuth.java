package br.com.joaoluisberute.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.joaoluisberute.todolist.user.IUserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    // Pede para o Spring Boot instanciar e auto-gerenciar o ciclo de vida dessa método 
    @Autowired
    // Indica um metodo que será utulizado na autenticação de usuario
    private IUserRepository userRepository;

    // Pede para o Spring Boot rastrear o metodo-pai que está sendo herdado e sobre-escrito no metodo-filho para se caso for mudado alguma infomação de assinatura do metodo-filho de forma que impossibilite essa herança do metodo-pai seja lançado um erro impedindo o bild
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Guarda na variavel "servletPath" a rota no qual o request esta sendo enviado
        var servletPath = request.getServletPath();

        // Faz a verificação da rota utilizada e se for a esperada faz a autenticação 
        if(servletPath.startsWith("/Task/")){

            // Guarda na variavel "authHeaderAuthorization" o tipo de autorização e as informações da autorização em si na forma de BASE64 ("TypeAuth"+" "+"d5fs4654fvsdfzx342#$")
            var authHeaderAuthorization = request.getHeader("Authorization");

            // Guarda na variavel "authorizationParsedToBase64Only" apenas as informações de autenticação em forma de BASE64
            var authorizationParsedToBase64Only = authHeaderAuthorization.substring("Basic".length()).trim();

            // Guarda na variavel "authorizationParsedToString" as informações de autenticação em forma de String ("user:password")
            var authorizationParsedToString = new String(Base64.getDecoder().decode(authorizationParsedToBase64Only));

            // Guarda na variavel "arrayWithUserAndPassword" um array com as informações de autenticação separadas ("user","password")
            String[] arrayWithUserAndPassword = authorizationParsedToString.split(":");

            // Guarda na variavel "username" o usuario que esta tentando se autenticar
            var username = arrayWithUserAndPassword[0];
            // Guarda na variavel "password" a senha que esta tentando se autenticar
            var password = arrayWithUserAndPassword[1];
                
            // Verifica se o usuario e a senha não estão vazios
            if(username.length() != 0 || password.length() != 0){

                // Guarda na variavel "user" todas as informações de um usuario caso seja encontrado pelo metodo "findByUsername" informado no "request" dentro do DB
                var user = this.userRepository.findByUsername(username);

                // Guarda na variavel "passwordCheck" o resultado da verificação de senha passado na atenticação pelo "request" transformado em um CharArray com o password do "user" criptografado no DB  
                var passwordCheck = BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());

                // Verifica se houve um match da verificação de usuario e senha
                if(passwordCheck.verified){

                    // Adiciona no registro da task o "idUser" que nada mais é que o id do usuario que esta criando a task para ficar registrado ser o dono dela
                    request.setAttribute("idUser", user.getId());

                    // Passa os argumetos "request" e "response" para a próxima camada da aplicação
                    filterChain.doFilter(request, response);
                }
                // Caso o usuario não seja autenticado é enviado um erro no "response"
                else{

                    // Envio de erro via "response" por não ser autenticado o usuario 
                    response.sendError(401);
                }
            }
            // Caso o usuario ou a senha estejam vazias, uma mensagem de erro é enviada no "response"
            else{

                // Envio de erro via "response" por não ser autenticado o usuario 
                response.sendError(401);
            }        

        }
        // Caso não necessite de verificação passa os argumetos "request" e "response" para a próxima camada da aplicação
        else{
                
            // Passa os argumetos "request" e "response" para a próxima camada da aplicação sem realizar nenhuma validação
            filterChain.doFilter(request, response);
        }
    }    
}
