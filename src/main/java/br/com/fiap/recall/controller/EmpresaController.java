package br.com.fiap.recall.controller;

import br.com.fiap.recall.dto.request.EmpresaRequest;
import br.com.fiap.recall.dto.response.EmpresaResponse;
import br.com.fiap.recall.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@Tag(name = "Empresas", description = "Endpoints para gerenciamento de empresas e contextos")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as empresas")
    public ResponseEntity<List<EmpresaResponse>> listarTodas() {
        return ResponseEntity.ok(empresaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa por ID detalhada com arquivos e insights")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(empresaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova empresa")
    public ResponseEntity<EmpresaResponse> cadastrar(@Valid @RequestBody EmpresaRequest request) {
        EmpresaResponse criada = empresaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma empresa existente")
    public ResponseEntity<EmpresaResponse> atualizar(@PathVariable String id, @Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.ok(empresaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir empresa por ID (em cascata)")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
