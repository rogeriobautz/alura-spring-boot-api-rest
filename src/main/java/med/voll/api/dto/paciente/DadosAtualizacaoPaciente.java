package med.voll.api.dto.paciente;

import jakarta.validation.constraints.NotNull;
import med.voll.api.dto.common.DadosEndereco;

public record DadosAtualizacaoPaciente(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
