// package com.neohorizon.api.integration;

// import java.math.BigDecimal;

// import static org.hamcrest.Matchers.hasItem;
// import static org.hamcrest.Matchers.is;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
// import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
// import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
// import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
// import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
// import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;

// @SpringBootTest
// @AutoConfigureMockMvc(addFilters = false) 
// @ActiveProfiles("test")
// public class IntegrationDimensoesTest extends AbstractIntegrationTest {

//     // ---------- DIM ATIVIDADE ----------
//     @Test
//     @DisplayName("Deve criar e recuperar uma DimAtividade via /dim-atividade")
//     void dimAtividade_criarEListar() throws Exception {
//         DimAtividadeDTO dto = DimAtividadeDTO.builder()
//                 .nome("Implementação")
//                 .descricao("Implementar endpoint X")
//                 .atividade_jira_id("TASK-1")
//                 .ativo(true)
//                 .build();

//         String body = objectMapper.writeValueAsString(dto);

//         String response = mockMvc.perform(post("/dim-atividade")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(body))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").isNumber())
//                 .andReturn()
//                 .getResponse()
//                 .getContentAsString();

//         DimAtividadeDTO created =
//                 objectMapper.readValue(response, DimAtividadeDTO.class);

//         mockMvc.perform(get("/dim-atividade"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[*].id",
//                         hasItem(created.getId().intValue())));

//         mockMvc.perform(get("/dim-atividade/{id}", created.getId()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.nome").value("Implementação"));
//     }

//     // ---------- DIM DEV ----------
//     @Test
//     @DisplayName("Deve criar e recuperar uma DimDev via /dim-dev")
//     void dimDev_criarEListar() throws Exception {
//         DimDevDTO dto = new DimDevDTO();
//         dto.setNome("Alice");
//         dto.setCustoHora(BigDecimal.valueOf(100.0));

//         String body = objectMapper.writeValueAsString(dto);

//         String response = mockMvc.perform(post("/dim-dev")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(body))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").isNumber())
//                 .andReturn()
//                 .getResponse()
//                 .getContentAsString();

//         DimDevDTO created =
//                 objectMapper.readValue(response, DimDevDTO.class);

//         mockMvc.perform(get("/dim-dev/{id}", created.getId()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.nome").value("Alice"))
//                 .andExpect(jsonPath("$.custoHora").value(100.0));
//     }

//     // ---------- DIM PERIODO ----------
//     @Test
//     @DisplayName("Deve criar e filtrar DimPeriodo via /dim-periodo/filter")
//     void dimPeriodo_criarEFiltrar() throws Exception {
//         DimPeriodoDTO dto = new DimPeriodoDTO(null, 1, 1, 1, 2025);
//         String body = objectMapper.writeValueAsString(dto);

//         mockMvc.perform(post("/dim-periodo")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(body))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").isNumber());

//         mockMvc.perform(get("/dim-periodo/filter")
//                         .param("dia", "1")
//                         .param("semana", "1")
//                         .param("mes", "1")
//                         .param("ano", "2025"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].dia").value(1))
//                 .andExpect(jsonPath("$[0].mes").value(1))
//                 .andExpect(jsonPath("$[0].ano").value(2025));
//     }

//     // ---------- DIM PROJETO ----------
//     @Test
//     @DisplayName("Deve criar e recuperar DimProjeto via /dim-projeto")
//     void dimProjeto_criarEListar() throws Exception {
//         DimProjetoDTO dto =
//                 new DimProjetoDTO(null, "NeoHorizon", "NH", "NH-PRJ-1");
//         String body = objectMapper.writeValueAsString(dto);

//         String response = mockMvc.perform(post("/dim-projeto")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(body))
//                 .andExpect(status().isCreated())
//                 .andReturn()
//                 .getResponse()
//                 .getContentAsString();

//         DimProjetoDTO created =
//                 objectMapper.readValue(response, DimProjetoDTO.class);

//         mockMvc.perform(get("/dim-projeto/{id}", created.getId()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.nome").value("NeoHorizon"));
//     }

//     // ---------- DIM STATUS ----------
//     @Test
//     @DisplayName("Deve criar e recuperar DimStatus via /dim-status")
//     void dimStatus_criarEListar() throws Exception {
//         DimStatusDTO dto =
//                 new DimStatusDTO(null, "Em Progresso", "ST-1");
//         String body = objectMapper.writeValueAsString(dto);

//         String response = mockMvc.perform(post("/dim-status")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(body))
//                 .andExpect(status().isCreated())
//                 .andReturn()
//                 .getResponse()
//                 .getContentAsString();

//         DimStatusDTO created =
//                 objectMapper.readValue(response, DimStatusDTO.class);

//         mockMvc.perform(get("/dim-status/{id}", created.getId()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.nome").value("Em Progresso"));
//     }

//     // ---------- DIM TIPO ----------
//     @Test
//     @DisplayName("Deve criar DimTipo e buscar pelo nome via /dim-tipo/nome/{nome}")
//     void dimTipo_criarEBuscarPorNome() throws Exception {
//         DimTipoDTO dto =
//                 new DimTipoDTO(null, "Desenvolvimento",
//                         "Implementação de funcionalidades", "TP-1");
//         String body = objectMapper.writeValueAsString(dto);

//         mockMvc.perform(post("/dim-tipo")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(body))
//                 .andExpect(status().isCreated());

//         mockMvc.perform(get("/dim-tipo/nome/{nome}", "Desenvolvimento"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].nome", is("Desenvolvimento")));
//     }
// }
