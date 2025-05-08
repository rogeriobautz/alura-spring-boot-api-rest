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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    @CacheEvict(value = "pacientes", key = "'listar-pacientes'")
    public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(@RequestBody @Valid DadosCadastroPaciente dados,
            UriComponentsBuilder uriBuilder) {
        Paciente paciente = pacienteRepository.save(new Paciente(dados));
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    @Transactional
    @Cacheable(value = "pacientes", key = "'listar-pacientes'")
    public ResponseEntity<Page<DadosListagemPaciente>> listar(
            @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
        Page<DadosListagemPaciente> page = pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "pacientes", key = "'listar-pacientes'"),
            @CacheEvict(value = "pacientes", key = "#dados.id")
    })
    public ResponseEntity<DadosListagemPaciente> atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {

        Paciente paciente = pacienteRepository.findById(dados.id()).get();

        paciente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosListagemPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "pacientes", key = "'listar-pacientes'"),
            @CacheEvict(value = "pacientes", key = "#id")
    })
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Paciente paciente = pacienteRepository.findById(id).get();

        paciente.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    @Cacheable(value = "pacientes", key = "#id")
    public ResponseEntity<DadosListagemPaciente> detalhar(@PathVariable Long id) {

        Paciente paciente = pacienteRepository.findById(id).get();

        return ResponseEntity.ok(new DadosListagemPaciente(paciente));
    }

}