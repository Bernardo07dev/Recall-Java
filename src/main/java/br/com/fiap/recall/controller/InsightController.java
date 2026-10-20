package br.com.fiap.recall.controller;

import br.com.fiap.recall.dto.request.InsightRequest;
import br.com.fiap.recall.dto.response.InsightResponse;
import br.com.fiap.recall.service.InsightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Insights", description = "Endpoints para geração e gestão de inteligência de vendas e CS")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping("/api/empresas/{empresaId}/insights")
    @Operation(summary = "Listar insights de uma empresa")
    public ResponseEntity<List<InsightResponse>> listarPorEmpresa(@PathVariable String empresaId) {
        return ResponseEntity.ok(insightService.listarPorEmpresa(empresaId));
    }

    @GetMapping("/api/insights/{id}")
    @Operation(summary = "Buscar insight por ID")
    public ResponseEntity<InsightResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(insightService.buscarPorId(id));
    }

    @PostMapping("/api/empresas/{empresaId}/insights")
    @Operation(summary = "Criar novo insight para a empresa e gerar task Jira")
    public ResponseEntity<InsightResponse> cadastrar(@PathVariable String empresaId, @Valid @RequestBody InsightRequest request) {
        InsightResponse criado = insightService.cadastrar(empresaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/api/insights/{id}")
    @Operation(summary = "Atualizar insight por ID")
    public ResponseEntity<InsightResponse> atualizar(@PathVariable String id, @Valid @RequestBody InsightRequest request) {
        return ResponseEntity.ok(insightService.atualizar(id, request));
    }

    @DeleteMapping("/api/insights/{id}")
    @Operation(summary = "Excluir insight por ID")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        insightService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
