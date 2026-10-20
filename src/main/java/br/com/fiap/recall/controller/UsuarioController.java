package br.com.fiap.recall.controller;

import br.com.fiap.recall.dto.request.UsuarioRequest;
import br.com.fiap.recall.dto.response.UsuarioResponse;
import br.com.fiap.recall.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e controle de acesso")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário (ADMIN, MANAGER ou VIEWER)")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse criado = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário existente")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable String id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário por ID")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
