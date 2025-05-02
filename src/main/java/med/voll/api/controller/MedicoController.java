package med.voll.api.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.dto.DadosCadastroMedico;
import med.voll.api.entity.Medico;
import med.voll.api.repository.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping()
    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    @PostMapping
    @Transactional
    public Medico cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        return medicoRepository.save(new Medico(dados));
    }

    @DeleteMapping
    public Medico excluir(@RequestParam String crm) throws BadRequestException {
        var medico = medicoRepository.findByCrm(crm);
        if(medico == null) {
            throw new BadRequestException("Nenhum médico com o CRM: " + crm + " encontrado.");
        }
        medicoRepository.delete(medico);
        return medico;
    }

    @PutMapping
    public Medico atualizar(@RequestBody @Valid DadosCadastroMedico dados) throws BadRequestException {
        var medicoCadastrado = medicoRepository.findByCrm(dados.crm());
        if(medicoCadastrado == null) {
            throw new BadRequestException("Nenhum médico com o CRM: " + dados.crm() + " encontrado.");
        }
        BeanUtils.copyProperties(new Medico(dados), medicoCadastrado, "id");
        return medicoRepository.save(medicoCadastrado);
    }


}
