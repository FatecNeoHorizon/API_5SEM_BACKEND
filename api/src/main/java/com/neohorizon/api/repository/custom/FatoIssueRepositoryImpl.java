package com.neohorizon.api.repository.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.neohorizon.api.dto.IssueDTO.IssueAgregationDTO;
import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class FatoIssueRepositoryImpl implements FatoIssueRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProjectIssueCountDTO> findIssuesByPeriod(IssueAgregationDTO issueAgregationDTO){

            String selectFormat;
            String groupColumns;
            String orderColumns;

        switch (issueAgregationDTO.getPeriodo()) {
            case DIA -> {
                selectFormat = "TO_CHAR(MAKE_DATE(dp.periodo_ano, dp.periodo_mes, dp.periodo_dia), 'YYYY-MM-DD') AS dataAgrupada";
                groupColumns = "dp.periodo_ano, dp.periodo_mes, dp.periodo_dia";
                orderColumns = "dp.periodo_ano, dp.periodo_mes, dp.periodo_dia";
            }
            case SEMANA -> {
                selectFormat = "CONCAT(dp.periodo_ano, '-S', LPAD(dp.periodo_semana::text, 2, '0')) AS dataAgrupada";
                groupColumns = "dp.periodo_ano, dp.periodo_semana";
                orderColumns = "dp.periodo_ano, dp.periodo_semana";
            }
            case MES -> {
                selectFormat = "TO_CHAR(MAKE_DATE(dp.periodo_ano, dp.periodo_mes, 1), 'YYYY-MM') AS dataAgrupada";
                groupColumns = "dp.periodo_ano, dp.periodo_mes";
                orderColumns = "dp.periodo_ano, dp.periodo_mes";
            }
            case ANO -> {
                selectFormat = "dp.periodo_ano::text AS dataAgrupada";
                groupColumns = "dp.periodo_ano";
                orderColumns = "dp.periodo_ano";
            }
            default -> {
                throw new IllegalArgumentException("Tipo de agregação inválido: " + issueAgregationDTO.getPeriodo());
            }
        }

        String whereClause = """
            dp.periodo_ano > :anoInicio OR (
                dp.periodo_ano = :anoInicio AND (
                    dp.periodo_mes > :mesInicio OR (
                        dp.periodo_mes = :mesInicio AND dp.periodo_dia >= :diaInicio
                    )
                )
            )
            AND (
                dp.periodo_ano < :anoFim OR (
                    dp.periodo_ano = :anoFim AND (
                        dp.periodo_mes < :mesFim OR (
                            dp.periodo_mes = :mesFim AND dp.periodo_dia <= :diaFim
                        )
                    )
                )
            )
        """;

        String sqlQuery = String.format("""
            SELECT
                %s,                                 -- dataAgrupada formatada
                SUM(fi.issue_quantidade) AS totalIssues
            FROM
                fato_issue fi
            JOIN
                dim_periodo dp ON fi.periodo_id = dp.periodo_id
            WHERE
                %s                                  -- filtro de data
            GROUP BY
                %s                                  -- colunas de agrupamento
            ORDER BY
                %s                                  -- colunas de ordenação
            """, selectFormat, whereClause, groupColumns, orderColumns);

        Query query = entityManager.createNativeQuery(sqlQuery);
        
        query.setParameter("anoInicio", issueAgregationDTO.getAnoInicio());
        query.setParameter("mesInicio", issueAgregationDTO.getMesInicio());
        query.setParameter("diaInicio", issueAgregationDTO.getDiaInicio());
        query.setParameter("anoFim", issueAgregationDTO.getAnoFim());
        query.setParameter("mesFim", issueAgregationDTO.getMesFim());
        query.setParameter("diaFim", issueAgregationDTO.getDiaFim());

        List<?> rawResults = query.getResultList();

        List<ProjectIssueCountDTO> dtos = new ArrayList<>();
        for (Object result : rawResults) {
            Object[] row = (Object[]) result;
            String dataAgrupada = (String) row[0];
            long totalIssues = ((Number) row[1]).longValue(); 

            dtos.add(new ProjectIssueCountDTO(dataAgrupada, totalIssues));
        }

        return dtos;


    }

}
