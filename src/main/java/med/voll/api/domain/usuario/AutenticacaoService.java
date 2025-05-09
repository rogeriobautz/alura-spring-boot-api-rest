package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;

@Service
public class AutenticacaoService{

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    public DadosTokenJWT gerarTokenJWT(DadosLoginUsuario dados) {        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = authManager.authenticate(authToken);
        return tokenService.gerarTokenJWT((Usuario) authentication.getPrincipal());
    }

    public Boolean usuarioLogadoEhAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(EnumPapelAutorizacao.ADMIN.getAuthority()));
    }

}
