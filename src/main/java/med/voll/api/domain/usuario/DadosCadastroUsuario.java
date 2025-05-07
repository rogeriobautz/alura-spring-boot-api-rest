package med.voll.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
    @NotBlank
    @Size(max=100)
    String login,

    @NotBlank
    @Size(max=255)
    String senha,

    @NotBlank
    @Size(max=80)
    String nome,

    @NotBlank
    @Size(max=20)
    String autorizacao
) { }
