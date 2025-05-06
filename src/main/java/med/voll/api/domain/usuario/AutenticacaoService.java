package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;

@Service
public class AutenticacaoService{

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    public DadosTokenJWT gerarTokenJWT(DadosUsuario dados) {        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = authManager.authenticate(authToken);
        return tokenService.gerarTokenJWT((Usuario) authentication.getPrincipal());
    }

}
