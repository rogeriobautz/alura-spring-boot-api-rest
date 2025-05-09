package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findAllByMotivoCancelamentoNull(Pageable paginacao);

    @Query("""
            select 1 from Consulta c
            where c.medico.id = :idMedico
            and c.dataHora = :dataHora
    """)
    Integer verificarMedicoLivreNaDataHora(Long idMedico, LocalDateTime dataHora);

    @Query("""
        select 1 from Consulta c
        where c.paciente.id = :idPaciente
        and function('date', c.dataHora) = function('date', :dataHora)
    """)
    Integer verificarConsultaPacienteMesmoDia(Long idPaciente, LocalDateTime dataHora);

}
