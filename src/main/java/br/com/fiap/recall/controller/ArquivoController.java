package br.com.fiap.recall.controller;

import br.com.fiap.recall.dto.request.ArquivoRequest;
import br.com.fiap.recall.dto.response.ArquivoResponse;
import br.com.fiap.recall.service.ArquivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Arquivos", description = "Endpoints para upload, extração e gestão de arquivos")
public class ArquivoController {

    private final ArquivoService arquivoService;

    public ArquivoController(ArquivoService arquivoService) {
        this.arquivoService = arquivoService;
    }

    @GetMapping("/api/empresas/{empresaId}/arquivos")
    @Operation(summary = "Listar arquivos de uma empresa")
    public ResponseEntity<List<ArquivoResponse>> listarPorEmpresa(@PathVariable String empresaId) {
        return ResponseEntity.ok(arquivoService.listarPorEmpresa(empresaId));
    }

    @GetMapping("/api/arquivos/{id}")
    @Operation(summary = "Buscar arquivo por ID")
    public ResponseEntity<ArquivoResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(arquivoService.buscarPorId(id));
    }

    @PostMapping("/api/empresas/{empresaId}/arquivos")
    @Operation(summary = "Cadastrar arquivo vinculado a uma empresa (PDF, CSV ou XLSX)")
    public ResponseEntity<ArquivoResponse> cadastrar(@PathVariable String empresaId, @Valid @RequestBody ArquivoRequest request) {
        ArquivoResponse criado = arquivoService.cadastrar(empresaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/api/arquivos/{id}")
    @Operation(summary = "Atualizar URL ou dados do arquivo")
    public ResponseEntity<ArquivoResponse> atualizar(@PathVariable String id, @Valid @RequestBody ArquivoRequest request) {
        return ResponseEntity.ok(arquivoService.atualizar(id, request));
    }

    @DeleteMapping("/api/arquivos/{id}")
    @Operation(summary = "Excluir arquivo por ID")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        arquivoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
