package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.InscricaoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.InscricaoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.InscricaoCursoMapper;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.service.InscricaoCursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos/inscricoes")
@RequiredArgsConstructor
@Tag(name = "Inscrições em Cursos", description = "Endpoints para gerenciar as inscrições de usuários nos cursos")
public class InscricaoCursoController {

    private final InscricaoCursoService service;
    private final InscricaoCursoMapper mapper;

    @Operation(summary = "Lista todas as inscrições realizadas")
    @ApiResponse(responseCode = "200", description = "Lista de inscrições retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<InscricaoResponseDTO>> listarTodos() {
        List<InscricaoCursoModel> inscricoes = service.listarTodos();
        return ResponseEntity.ok(mapper.toResponseDTOList(inscricoes));
    }

    @Operation(summary = "Busca uma inscrição pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição encontrada", content = @Content(schema = @Schema(implementation = InscricaoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inscrição não encontrada", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<InscricaoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(inscricao -> ResponseEntity.ok(mapper.toResponseDTO(inscricao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria uma nova inscrição para um usuário em um curso", description = "Realiza uma série de validações, como período de inscrição, vagas disponíveis e duplicidade, antes de confirmar a inscrição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscrição realizada com sucesso", content = @Content(schema = @Schema(implementation = InscricaoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada (ex: sem vagas, fora do prazo)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Curso ou Usuário não encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<InscricaoResponseDTO> criarInscricao(
            @Valid @RequestBody InscricaoRequestDTO requestDTO) {
        InscricaoCursoModel inscricaoSalva = service.salvarInscricao(requestDTO);
        return new ResponseEntity<>(mapper.toResponseDTO(inscricaoSalva), HttpStatus.CREATED);
    }

    @Operation(summary = "Cancela (exclui) uma inscrição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inscrição cancelada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Inscrição não encontrada para cancelamento", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
