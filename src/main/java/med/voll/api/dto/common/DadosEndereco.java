package med.voll.api.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosEndereco(

    @NotBlank
    String logradouro,

    @NotBlank
    String bairro,

    @NotBlank
    @Pattern(regexp = "\\d{8}")
    String cep,

    @NotBlank
    String cidade,

    @NotBlank
    @Size(min = 2, max = 2)
    String uf,

    String complemento,

    String numero

) {}
