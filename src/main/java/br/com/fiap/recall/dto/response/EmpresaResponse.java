package br.com.fiap.recall.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record EmpresaResponse(
        String id,
        String nome,
        String industry,
        String summarization,
        String jsonIntel,
        LocalDateTime dataCriacao,
        List<ArquivoResponse> arquivos,
        List<InsightResponse> insights
) {}
