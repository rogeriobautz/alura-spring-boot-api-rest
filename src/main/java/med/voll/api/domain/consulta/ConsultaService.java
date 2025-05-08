package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public Consulta agendar(DadosAgendamentoConsulta dados) {

        var paciente = pacienteRepository.findById(dados.idPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        var medico = escolherMedico(dados);

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);

        return consultaRepository.save(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.findById(dados.idMedico())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Médico com a id " + dados.idMedico() + "não encontrado"));
        }

        if (dados.especialidade() == null) {
            throw new IllegalArgumentException("Não é possível escolher um médico sem especialidade");
        }

        var medico = medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
        if (medico == null) {
            throw new IllegalArgumentException("Não há médicos disponíveis nessa data");
        }
        return medico;
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new IllegalArgumentException("Id da consulta informado não existe!");
        }
    
        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

}
