package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.AutenticacaoService;
import med.voll.api.domain.usuario.DadosUsuario;
import med.voll.api.infra.security.DadosTokenJWT;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {    
    
    @Autowired
    private AutenticacaoService autenticacaoService;
    
    @Cacheable(value = "login", key = "#dados.login")
    @PostMapping
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosUsuario dados) {
        return ResponseEntity.ok(autenticacaoService.gerarTokenJWT(dados));
    }



}
