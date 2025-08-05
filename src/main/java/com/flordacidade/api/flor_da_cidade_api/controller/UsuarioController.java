package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.service.UsuarioService;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.UsuarioMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Validated
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para o gerenciamento de usuários finais (público geral)")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @Operation(summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<UsuarioModel> usuarios = service.getAll();
        return ResponseEntity.ok(mapper.toResponseDTOList(usuarios));
    }

    @Operation(summary = "Busca um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Integer id) {
        return service.getById(id)
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

    @Operation(summary = "Atualiza um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos únicos já existentes", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) {
        UsuarioModel atualizado = service.atualizar(id, usuarioDTO);
        return ResponseEntity.ok(mapper.toResponseDTO(atualizado));
    }

    @Operation(summary = "Exclui um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
