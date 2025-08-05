package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.AreaClassificacaoDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.AreaClassificacaoMapper;
import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.service.AreaClassificacaoService;
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
@RequestMapping("/api/hortas/areas-classificacao")
@RequiredArgsConstructor
@Tag(name = "Auxiliar: Áreas de Classificação", description = "Endpoints para gerenciar as opções de área de classificação para hortas")
public class AreaClassificacaoController {

    private final AreaClassificacaoService service;
    private final AreaClassificacaoMapper mapper;

    @Operation(summary = "Lista todas as áreas de classificação disponíveis")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaClassificacaoDTO.class)))
    @GetMapping
    public ResponseEntity<List<AreaClassificacaoDTO>> listar() {
        List<AreaClassificacao> listaDeEntidades = service.listar();
        return ResponseEntity.ok(mapper.toDTOList(listaDeEntidades));
    }

    @Operation(summary = "Busca uma área de classificação pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área de classificação encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaClassificacaoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Área de classificação não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AreaClassificacaoDTO> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(entidade -> ResponseEntity.ok(mapper.toDTO(entidade)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria uma nova área de classificação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaClassificacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: nome em branco)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AreaClassificacaoDTO> criar(@Valid @RequestBody AreaClassificacaoDTO dto) {
        // O serviço agora deve ser ajustado para receber o DTO
        AreaClassificacao entidadeSalva = service.salvar(dto);
        // Retorna o DTO correspondente com o status HTTP 201 (Created)
        return new ResponseEntity<>(mapper.toDTO(entidadeSalva), HttpStatus.CREATED);
    }

    @Operation(summary = "Exclui uma área de classificação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Área de classificação não encontrada para exclusão", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        // Retorna o status HTTP 204 (No Content), que é a prática recomendada para
        // DELETE
        return ResponseEntity.noContent().build();
    }
}
