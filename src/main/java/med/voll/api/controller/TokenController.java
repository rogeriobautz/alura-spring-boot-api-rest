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
import med.voll.api.domain.usuario.DadosLoginUsuario;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.domain.usuario.UsuarioRepository;
import med.voll.api.infra.security.DadosTokenJWT;

@RestController
@RequestMapping()
public class TokenController {    
    
    @Autowired
    private AutenticacaoService autenticacaoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Cacheable(value = "login", key = "#dados.login")
    @PostMapping("/token")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosLoginUsuario dados) {
        var usuario = (Usuario) usuarioRepository.findByLogin(dados.login());
        if(usuario != null && !usuario.getAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }
        return ResponseEntity.ok(autenticacaoService.gerarTokenJWT(dados));
    }    

}
