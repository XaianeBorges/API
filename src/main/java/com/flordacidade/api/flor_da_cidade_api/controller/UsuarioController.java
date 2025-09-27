package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.service.UsuarioService;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.UsuarioMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Validated
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para o gerenciamento de usuários (público geral)")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @Operation(summary = "Lista todos os usuários (Acesso restrito a Admins)")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Lista de usuários retornada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
         @ApiResponse(responseCode = "403", description = "Acesso negado, só ADM tem perimssão", content = @Content) })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<UsuarioModel> usuarios = service.getAll();
        return ResponseEntity.ok(mapper.toResponseDTOList(usuarios));
    }

    @Operation(summary = "Busca um usuário pelo seu ID (Acesso restrito a Admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só ADM tem perimssão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(usuario -> ResponseEntity.ok(mapper.toResponseDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Busca um usuário pelo seu CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o CPF fornecido", content = @Content)
    })
    @GetMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> getByCpf(@Parameter(description = "CPF do usuário a ser buscado") @PathVariable String cpf) {
        return service.getByCpf(cpf)
                .map(usuario -> ResponseEntity.ok(mapper.toResponseDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos únicos já existentes", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioCreateDTO usuarioDTO) {
        UsuarioModel created = service.criar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponseDTO(created));
    }

    @Operation(summary = "Atualiza um usuário existente (Acesso restrito a Admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos únicos já existentes", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só ADM tem perimssão", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) {
        UsuarioModel atualizado = service.atualizar(id, usuarioDTO);
        return ResponseEntity.ok(mapper.toResponseDTO(atualizado));
    }

    @Operation(summary = "Exclui um usuário (Acesso restrito a Admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só ADM tem perimssão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
