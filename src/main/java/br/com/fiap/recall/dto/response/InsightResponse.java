package br.com.fiap.recall.dto.response;

import java.time.LocalDateTime;

public record InsightResponse(
        String id,
        String categoria,
        String criticidade,
        String titulo,
        String insight,
        String acaoSugerida,
        String taskJira,
        String empresaId,
        LocalDateTime dataCriacao
) {}
