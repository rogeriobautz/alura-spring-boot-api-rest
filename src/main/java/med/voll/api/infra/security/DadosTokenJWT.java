package med.voll.api.infra.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosTokenJWT(

    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_at")
    String expiresAt

) { }
