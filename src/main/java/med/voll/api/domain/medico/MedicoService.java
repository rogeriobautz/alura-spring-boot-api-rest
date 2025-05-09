package med.voll.api.domain.medico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Medico getMedico(DadosAgendamentoConsulta dados) {

        if (dados.idMedico() != null) {

            var medico = medicoRepository.findByIdAndAtivoTrue(dados.idMedico());
            if (medico == null) {
                throw new IllegalArgumentException("Médico não encontrado");
            }

            var consultaMedico = consultaRepository.verificarMedicoLivreNaDataHora(dados.idMedico(), dados.dataHora());
            if (consultaMedico != null) {
                throw new IllegalArgumentException("Médico já tem consulta agendada data/hora");
            }
            return medico;
        }

        if (dados.especialidade() == null) {
            throw new IllegalArgumentException("Não é possível escolher um médico sem especialidade");
        }

        var medico = medicoRepository.escolherMedicoAleatorioLivreNaDataHora(dados.especialidade(), dados.dataHora());
        if (medico == null) {
            throw new IllegalArgumentException("Não há médicos disponíveis nessa data");
        }
        return medico;
    }

}
