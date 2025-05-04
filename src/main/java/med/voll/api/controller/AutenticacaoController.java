package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.dto.usuario.DadosUsuario;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid DadosUsuario dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        System.out.println("token: " + token);
        var auth = authManager.authenticate(token);
        System.out.println("auth: " + auth);
        return ResponseEntity.ok().build();
    }

}
