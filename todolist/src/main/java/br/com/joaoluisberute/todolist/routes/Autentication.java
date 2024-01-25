package br.com.joaoluisberute.todolist.routes;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/")
public class Autentication {

    @GetMapping("/")
    public String InitialPage() {
        return "Initial Page!";
    }
}
