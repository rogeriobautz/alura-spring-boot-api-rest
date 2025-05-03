package med.voll.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
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
import med.voll.api.dto.paciente.DadosAtualizacaoPaciente;
import med.voll.api.dto.paciente.DadosCadastroPaciente;
import med.voll.api.dto.paciente.DadosDetalhamentoPaciente;
import med.voll.api.dto.paciente.DadosListagemPaciente;
import med.voll.api.entity.Paciente;
import med.voll.api.repository.PacienteRepository;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
        Paciente paciente = repository.save(new Paciente(dados));
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        Page<DadosListagemPaciente> page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosListagemPaciente> atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {

        Paciente paciente = repository.findById(dados.id()).get();

        paciente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosListagemPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Paciente paciente = repository.findById(id).get();

        paciente.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosListagemPaciente> detalhar(@PathVariable Long id) {

        Paciente paciente = repository.findById(id).get();


                return ResponseEntity.ok(new DadosListagemPaciente(paciente));
    }


}