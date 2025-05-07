package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.api.domain.usuario.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Value("${api.security.offset:-03:00}")
    private String offset;

    private static final String ISSUER = "API Voll.med"; 

    public DadosTokenJWT gerarTokenJWT(Usuario usuario) {

        try {

            Algorithm algoritmo = Algorithm.HMAC256(tokenSecret);

            Instant expiracao = dataExpiracao();

            String tokenJWT =  JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);

            String dataExpiracaoFormatada = ZonedDateTime.ofInstant(expiracao, ZoneId.of(offset)).toString();
            
            return new DadosTokenJWT(tokenJWT, dataExpiracaoFormatada);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token jwt", e);
        }

    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of(offset));
    }

    public String getSubject(String tokenJWT) {
        try {

            Algorithm algoritmo = Algorithm.HMAC256(tokenSecret);

            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception) {

            throw new RuntimeException("Token inválido ou expirado", exception);

        }
    }

}
