package com.neohorizon.api.integration;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;

public class IntegrationFatosTest extends AbstractIntegrationTest {

    private Long criarDimProjeto(String nome) throws Exception {
        DimProjetoDTO dto =
                new DimProjetoDTO(null, nome, "PROJ_" + nome.toUpperCase().replace(" ", "_"), "JIRA-" + nome);
        String body = objectMapper.writeValueAsString(dto);

        String response = mockMvc.perform(post("/dim-projeto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DimProjetoDTO created = objectMapper.readValue(response, DimProjetoDTO.class);
        return created.getId();
    }

    private Long criarDimPeriodo(LocalDate data) throws Exception {
        DimPeriodoDTO dto =
                new DimPeriodoDTO(null, data.getDayOfMonth(), data.getDayOfYear() / 7 + 1, data.getMonthValue(), data.getYear());
        String body = objectMapper.writeValueAsString(dto);

        String response = mockMvc.perform(post("/dim-periodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DimPeriodoDTO created = objectMapper.readValue(response, DimPeriodoDTO.class);
        return created.getId();
    }



    private Long criarDimDev(String nome) throws Exception {
        DimDevDTO dto = new DimDevDTO(null, nome, BigDecimal.valueOf(100.0));
        String body = objectMapper.writeValueAsString(dto);

        String response = mockMvc.perform(post("/dim-dev")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DimDevDTO created =
                objectMapper.readValue(response, DimDevDTO.class);
        return created.getId();
    }


    @Test
    @DisplayName("Deve criar FatoCustoHora e recuperar via /fato-custo-hora/filter")
    void fatoCustoHora_filtrarPorDimensoes() throws Exception {
        LocalDate data = LocalDate.of(2025, 3, 10);

        Long projetoA = criarDimProjeto("Projeto Custo A");
        Long projetoB = criarDimProjeto("Projeto Custo B");
        Long periodoId = criarDimPeriodo(data);
        Long devId = criarDimDev("Carol");

        String bodyA = """
                {
                  "dimProjeto": { "id": %d },
                  "dimPeriodo": { "id": %d },
                  "dimDev": { "id": %d },
                  "custo": 100.0,
                  "horasQuantidade": 10.0
                }
                """.formatted(projetoA, periodoId, devId);

        String bodyB = """
                {
                  "dimProjeto": { "id": %d },
                  "dimPeriodo": { "id": %d },
                  "dimDev": { "id": %d },
                  "custo": 200.0,
                  "horasQuantidade": 20.0
                }
                """.formatted(projetoB, periodoId, devId);

        mockMvc.perform(post("/fato-custo-hora")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyA))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/fato-custo-hora")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyB))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/fato-custo-hora/filter")
                        .param("projeto_id", projetoA.toString())
                        .param("periodo_id", periodoId.toString())
                        .param("dev_id", devId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                // na resposta atual, somente o id do projeto vem preenchido
                .andExpect(jsonPath("$[0].dimProjeto.id").value(projetoA.intValue()));
    }

    @Test
    @DisplayName("Deve calcular evolução de custo por mês em /fato-custo-hora/evolucao")
    void fatoCustoHora_evolucaoPorMes() throws Exception {
        LocalDate data = LocalDate.of(2025, 3, 10);

        Long projeto = criarDimProjeto("Projeto Evolução");
        Long periodoId = criarDimPeriodo(data);
        Long devId = criarDimDev("Dev Evolução");

        String body = """
                {
                  "dimProjeto": { "id": %d },
                  "dimPeriodo": { "id": %d },
                  "dimDev": { "id": %d },
                  "custo": 150.0,
                  "horasQuantidade": 15.0
                }
                """.formatted(projeto, periodoId, devId);

        mockMvc.perform(post("/fato-custo-hora")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/fato-custo-hora/evolucao")
                        .param("granularidade", "mes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].periodo", not(emptyOrNullString())))
                .andExpect(jsonPath("$[0].custo", greaterThanOrEqualTo(0)));
    }
}
