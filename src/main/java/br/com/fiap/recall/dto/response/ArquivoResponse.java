package br.com.fiap.recall.dto.response;

import java.time.LocalDateTime;

public record ArquivoResponse(
        String id,
        String url,
        String tipo,
        Integer paginasLinhas,
        String empresaId,
        String textoConvertido,
        LocalDateTime dataUpload
) {}
