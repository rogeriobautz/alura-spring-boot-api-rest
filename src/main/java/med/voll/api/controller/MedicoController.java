package med.voll.api.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    @Transactional
    @CacheEvict(value = "medicos", key = "'listar-medicos'")
    public ResponseEntity<DadosDetalhamentoMedico> cadastrar(@RequestBody @Valid DadosCadastroMedico dados,
            UriComponentsBuilder uriBuilder) {
        var medico = medicoRepository.save(new Medico(dados));
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping()
    @Transactional
    @Cacheable(value = "medicos", key = "'listar-medicos'")
    public ResponseEntity<Page<DadosListagemMedico>> listar(
            @Nullable @PageableDefault(size = 10, sort = { "nome", "especialidade" }) Pageable paginacao) {
        var page = medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "medicos", key = "'listar-medicos'"),
        @CacheEvict(value = "medicos", key = "#dados.id")
    })
    public ResponseEntity<DadosDetalhamentoMedico> atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){

        Medico medico = medicoRepository.findById(dados.id()).get();

        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "medicos", key = "'listar-medicos'"),
        @CacheEvict(value = "medicos", key = "#id")
    })
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Medico medico = medicoRepository.findById(id).get();

        medico.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    @Cacheable(value = "medicos", key = "#id")
    public ResponseEntity<DadosDetalhamentoMedico> detalhar(@PathVariable Long id) {

        Medico medico = medicoRepository.findById(id).get();

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}
