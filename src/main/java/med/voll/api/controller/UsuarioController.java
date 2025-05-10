package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosCadastroUsuario;
import med.voll.api.domain.usuario.DadosUsuarioCadastrado;
import med.voll.api.domain.usuario.EnumPapelAutorizacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.domain.usuario.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {    
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    @GetMapping()
    @Transactional
    @Cacheable(value = "usuarios", key = "'listar-usuarios'")
    public ResponseEntity<Page<DadosUsuarioCadastrado>> listar(
            @Nullable @PageableDefault(size = 10, sort = { "autorizacao"}) Pageable paginacao) {

        var page = usuarioRepository.findAllByAtivoTrue(paginacao).map(DadosUsuarioCadastrado::new);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/cadastrar")
    @CacheEvict(value = "usuarios", key = "'listar-usuarios'")
    public ResponseEntity<DadosUsuarioCadastrado> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {

        var userdetails = usuarioRepository.findByLogin(dados.login());        
        if (userdetails != null) {
            throw new RuntimeException("Login já cadastrado");
        }
        Usuario usuario = usuarioRepository.save(new Usuario(dados, passwordEncoder.encode(dados.senha())));
        return ResponseEntity.ok(new DadosUsuarioCadastrado(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'listar-usuarios'"),
        @CacheEvict(value = "usuarios", key = "#id")
    })
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id).get();

        if(usuario.getAutorizacao().equals(EnumPapelAutorizacao.ADMIN)) {
            var quantidadeAdmins = usuarioRepository
                    .findAll()
                    .stream()
                    .filter(u -> u.getAtivo() && u.getAutorizacao().equals(EnumPapelAutorizacao.ADMIN))
                    .count();
            if(quantidadeAdmins <= 1) {
                throw new RuntimeException("Não é possível excluir o último administrador");
            }
        }
        
        usuario.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    @Cacheable(value = "usuarios", key = "#id")
    public ResponseEntity<DadosUsuarioCadastrado> detalhar(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id).get();

        return ResponseEntity.ok(new DadosUsuarioCadastrado(usuario));
    }

}
