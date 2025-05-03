// package med.voll.api.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import med.voll.api.dto.DadosCadastroMedico;
// import med.voll.api.dto.DadosEndereco;
// import med.voll.api.entity.Medico;
// import med.voll.api.enums.Especialidade;
// import med.voll.api.repository.MedicoRepository;

// @WebMvcTest(MedicoController.class)
// class MedicoControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockitoBean
//     private MedicoRepository medicoRepository;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Test
//     void testListar() throws Exception {
//         var medico = new Medico();
//         when(medicoRepository.findAll()).thenReturn(List.of(medico));

//         mockMvc.perform(get("/medicos"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$").isArray());
//     }

//     @Test
//     void testCadastrar() throws Exception {
//         var dadosEndereco = new DadosEndereco("Rua A", "Bairro B", "71300300", "Cidade C", "DF", "apto 101", "10");
//         var dados = new DadosCadastroMedico("Dr. John", "john@example.com", "123456789", "12345", Especialidade.CARDIOLOGIA, dadosEndereco);
//         var medico = new Medico(dados);
//         when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

//         mockMvc.perform(post("/medicos")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(dados)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.crm").value("12345"));
//     }

//     @Test
//     void testExcluir() throws Exception {
//         var medico = new Medico();
//         when(medicoRepository.findByCrm("12345")).thenReturn(medico);

//         mockMvc.perform(delete("/medicos")
//                 .param("crm", "12345"))
//                 .andExpect(status().isOk());

//         verify(medicoRepository).delete(medico);
//     }

//     @Test
//     void testExcluirNotFound() throws Exception {
//         when(medicoRepository.findByCrm("12345")).thenReturn(null);

//         mockMvc.perform(delete("/medicos")
//                 .param("crm", "12345"))
//                 .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testAtualizar() throws Exception {
//         var dadosEndereco = new DadosEndereco("Rua B", "Bairro B", "71300300", "Cidade C", "DF", "apto 101", "10");
//         var dados = new DadosCadastroMedico("Dr. John", "john@example.com", "123456789", "12345", Especialidade.CARDIOLOGIA, dadosEndereco);
//         var medico = new Medico();
//         when(medicoRepository.findByCrm("12345")).thenReturn(medico);
//         when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

//         mockMvc.perform(put("/medicos")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(dados)))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testAtualizarNotFound() throws Exception {
//         var dadosEndereco = new DadosEndereco("Rua B", "Bairro B", "71300300", "Cidade C", "DF", "apto 101", "10");
//         var dados = new DadosCadastroMedico("Dr. John", "john@example.com", "123456789", "12345", Especialidade.CARDIOLOGIA, dadosEndereco);

//         when(medicoRepository.findByCrm("12345")).thenReturn(null);

//         mockMvc.perform(put("/medicos")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(dados)))
//                 .andExpect(status().isBadRequest());
//     }
// }