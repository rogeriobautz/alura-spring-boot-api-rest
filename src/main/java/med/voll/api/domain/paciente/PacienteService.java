package med.voll.api.domain.paciente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@Service
public class PacienteService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente getPaciente(DadosAgendamentoConsulta dados) {
        var paciente = pacienteRepository.findByIdAtivoTrue(dados.idPaciente());
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado");
        }
        var consultaPaciente = consultaRepository.verificarConsultaPacienteMesmoDia(dados.idPaciente(),
                dados.dataHora());
        if (consultaPaciente != null) {
            throw new IllegalArgumentException("Paciente já tem consulta agendada neste dia");
        }
        return paciente;
    }

}
