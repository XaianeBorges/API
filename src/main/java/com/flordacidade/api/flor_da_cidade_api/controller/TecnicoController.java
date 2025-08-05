package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.NewPasswordRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.PasswordResetRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.TecnicoMapper;
import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.service.TecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tecnicos")
@RequiredArgsConstructor
@Tag(name = "Técnicos", description = "Gerenciamento de usuários técnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;
    private final TecnicoMapper tecnicoMapper;

    @Operation(summary = "Lista todos os técnicos (visão segura)")
    @ApiResponse(responseCode = "200", description = "Lista de técnicos retornada", content = @Content(schema = @Schema(implementation = TecnicoResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<TecnicoResponseDTO>> getAll() {
        List<TecnicoModel> tecnicos = tecnicoService.getAll();
        return ResponseEntity.ok(tecnicoMapper.toResponseDTOList(tecnicos));
    }

    @Operation(summary = "Busca técnico por ID")
    @ApiResponse(responseCode = "200", description = "Técnico encontrado", content = @Content(schema = @Schema(implementation = TecnicoResponseDTO.class)))
    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> getById(@PathVariable Integer id) {
        return tecnicoService.getById(id)
                .map(tecnico -> ResponseEntity.ok(tecnicoMapper.toResponseDTO(tecnico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo técnico")
    @PostMapping
    public ResponseEntity<TecnicoResponseDTO> create(@Valid @RequestBody TecnicoCreateDTO tecnicoDTO) {
        TecnicoModel createdTecnico = tecnicoService.create(tecnicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tecnicoMapper.toResponseDTO(createdTecnico));
    }

    @Operation(summary = "Atualiza um técnico")
    @ApiResponse(responseCode = "200", description = "Técnico atualizado", content = @Content(schema = @Schema(implementation = TecnicoResponseDTO.class)))
    @PutMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody TecnicoUpdateDTO tecnicoDTO) {

        try {
            TecnicoModel atualizado = tecnicoService.update(id, tecnicoDTO);
            return ResponseEntity.ok(tecnicoMapper.toResponseDTO(atualizado));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Deleta um tecnico")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            tecnicoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Solicita a redefinição de senha", description = "Inicia o fluxo de 'esqueci minha senha'. Um token será gerado e enviado para o e-mail do técnico.")
    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> esqueciSenha(@Valid @RequestBody PasswordResetRequestDTO request) {
        tecnicoService.solicitarRedefinicaoSenha(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Redefine a senha usando um token")
    @PostMapping("/redefinir-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@Valid @RequestBody NewPasswordRequestDTO request) {
        tecnicoService.redefinirSenha(request.getToken(), request.getNovaSenha());
        return ResponseEntity.ok(Map.of("mensagem", "Senha redefinida com sucesso."));
    }
}