package med.voll.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.ConsultaService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping()
    @Transactional
    @CacheEvict(value = "consultas", key = "'listar-consultas'")
    public ResponseEntity<?> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados,
            UriComponentsBuilder uriBuilder) {        

        System.out.println("DadosAgendamentoConsulta: " + dados);
        var dadosDetalhamentoConsulta = consultaService.agendar(dados);
        System.out.println("DadosDetalhamentoConsulta: " + dadosDetalhamentoConsulta);

        URI uri = uriBuilder.path("/consultas/{id}").buildAndExpand(dadosDetalhamentoConsulta.id()).toUri();
        return ResponseEntity.created(uri).body(dadosDetalhamentoConsulta);
    }

    @GetMapping
    @Transactional
    @Cacheable(value = "consultas", key = "'listar-consultas'")
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listar(
            @Nullable @PageableDefault(size = 10, sort = { "dataHora" }) Pageable paginacao) {
        Page<DadosDetalhamentoConsulta> page = consultaRepository.findAllByMotivoCancelamentoNull(paginacao)
                .map(DadosDetalhamentoConsulta::new);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "consultas", key = "'listar-consultas'"),
            @CacheEvict(value = "consultas", key = "#dados.idConsulta")
    })
    public ResponseEntity<?> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        consultaService.cancelar(dados);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    @Cacheable(value = "pacientes", key = "#id")
    public ResponseEntity<DadosDetalhamentoConsulta> detalhar(@PathVariable Long id) {

        Consulta consulta = consultaRepository.findById(id).get();

        return ResponseEntity.ok(new DadosDetalhamentoConsulta(consulta));
    }

}