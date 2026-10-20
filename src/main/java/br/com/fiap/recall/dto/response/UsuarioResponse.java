package br.com.fiap.recall.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(
        String id,
        String nome,
        String email,
        String cargo,
        String area,
        String tipo,
        boolean podeCriarContexto,
        LocalDateTime dataCriacao
) {}
