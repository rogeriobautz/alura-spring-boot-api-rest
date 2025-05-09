package med.voll.api.domain.consulta;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.medico.MedicoService;
import med.voll.api.domain.paciente.PacienteService;
import med.voll.api.domain.usuario.AutenticacaoService;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private AutenticacaoService autenticacaoService;

    public Consulta agendar(DadosAgendamentoConsulta dados) {

        validarDataConsulta(dados.dataHora());

        var paciente = pacienteService.getPaciente(dados);

        var medico = medicoService.getMedico(dados);

        var consulta = new Consulta(null, medico, paciente, dados.dataHora(), null);

        return consultaRepository.save(consulta);
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new IllegalArgumentException("Id da consulta informado não existe!");
        }

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());

        if (!autenticacaoService.usuarioLogadoEhAdmin()) {
            if (consulta.getDataHora().minusHours(24).isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Só é possível cancelar consultas com 24 horas de antecedência");
            }
        }

        consulta.cancelar(dados.motivo());
    }

    private void validarDataConsulta(LocalDateTime dataHora) {

        if (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Não é possível agendar consultas aos domingos");
        }
        if (dataHora.getHour() < 7 || dataHora.getHour() > 18) {
            throw new IllegalArgumentException("Horário inválido. O horário de agendamento deve ser entre 7h e 18h");
        }
        if (dataHora.getMinute() != 0 || dataHora.getSecond() != 0) {
            throw new IllegalArgumentException("Horário inválido. O agendamento deve ser feito em horários cheios");
        }
        if (LocalDateTime.now().isAfter(dataHora.minusMinutes(30))) {
            throw new IllegalArgumentException(
                    "Não é possível agendar consultas com menos de 30 minutos de antecedência");
        }
    }

}
