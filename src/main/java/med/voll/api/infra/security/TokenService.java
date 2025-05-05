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
import com.auth0.jwt.interfaces.DecodedJWT;

import med.voll.api.domain.usuario.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Value("${api.security.offset:-03:00}")
    private String offset;

    public DadosTokenJWT gerarTokenHMAC256(Usuario usuario) {

        try {
            
            Algorithm algoritmo = Algorithm.HMAC256(tokenSecret);

            Instant expiracao = dataExpiracao();

            String tokenJWT =  JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);

            // UTC timezone
            // String dataExpiracaoFormatada = ZonedDateTime.ofInstant(expiracao, ZoneId.of("UTC"))
            //         .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

            String dataExpiracaoFormatada = ZonedDateTime.ofInstant(expiracao, ZoneId.of(offset)).toString();
            
            return new DadosTokenJWT(tokenJWT, dataExpiracaoFormatada);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token jwt", e);
        }

    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of(offset));
    }

    public void validarToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algoritmo = Algorithm.HMAC256("secret");
            var verifier = JWT.require(algoritmo)
                    .withIssuer("auth0")
                    .build();

            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
        }
    }

}
