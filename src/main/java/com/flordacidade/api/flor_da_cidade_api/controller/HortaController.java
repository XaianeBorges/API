package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.exception.*;
import com.flordacidade.api.flor_da_cidade_api.service.HortaService;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.HortaMapper;
import com.flordacidade.api.flor_da_cidade_api.service.PdfService;
import com.flordacidade.api.flor_da_cidade_api.service.ExcelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hortas")
@Tag(name = "Hortas", description = "Endpoints para o gerenciamento completo de hortas")
public class HortaController {

    @Autowired
    private HortaService hortaService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private HortaMapper hortaMapper;

    @Operation(summary = "Lista todas as hortas ativas para o mapa público")
    @ApiResponse(responseCode = "200", description = "Hortas ativas encontradas", content = @Content(mediaType = "application/json"))
    @GetMapping("/public/ativas")
    public ResponseEntity<List<HortaResponseDTO>> getPublicActiveHortas() {
        List<HortaResponseDTO> hortasAtivas = hortaService.listarAtivasParaMapa();
        return ResponseEntity.ok(hortasAtivas);
    }

    @Operation(summary = "Lista todas as hortas cadastradas (visão administrativa)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as hortas", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só tecnicos e ADM tem perimssão", content = @Content) })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<HortaResponseDTO>> listarTodas() {
        List<HortaResponseDTO> hortas = hortaService.listarTodas();
        return ResponseEntity.ok(hortas);
    }

    @Operation(summary = "Busca uma horta pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horta encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Horta não encontrada", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<HortaResponseDTO> buscarPorId(
            @Parameter(description = "ID da horta a ser buscada") @PathVariable Integer id) {
        return hortaService.buscarPorId(id)
                .map(hortaDTO -> ResponseEntity.ok(hortaDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista todas as solicitações de hortas com o status de PENDENTE (visão administrativa)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as hortas com o status PENDENTE", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só tecnicos e ADM tem perimssão", content = @Content) })
    @GetMapping("/solicitacoes/pendentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<Map<String, Object>>> getPendingHortaRequests() {
        List<Map<String, Object>> requests = hortaService.getPendingHortaRequests();
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Lista hortas de acordo com o status selecionado(visão administrativa )")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de hortas com o status X", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Status não enconttrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só tecnicos e ADM tem perimssão", content = @Content) })
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<Map<String, Object>>> getHortasByStatus(@PathVariable String status) {
        try {
            Horta.StatusHorta statusEnum = Horta.StatusHorta.valueOf(status.toUpperCase());
            List<Map<String, Object>> hortas = hortaService.getHortasByStatusWithUserDetails(statusEnum);
            return ResponseEntity.ok(hortas);
        } catch (IllegalArgumentException e) {
            System.err.println("Status inválido fornecido para /api/hortas/status: " + status + " - " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Cria uma nova horta (solicitação)", description = "Cria uma nova horta com status PENDENTE. Requer dados da horta e opcionalmente uma imagem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horta criada com sucesso", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HortaResponseDTO> criar(
            @Parameter(description = "Dados da horta a ser criada, em formato JSON.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HortaRequestDTO.class))) @RequestPart("horta") @Valid HortaRequestDTO hortaDTO,
            @Parameter(description = "Arquivo de imagem da horta (opcional)") @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        HortaResponseDTO hortaSalva = hortaService.salvar(hortaDTO, imagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(hortaSalva);
    }

    @Operation(summary = "Atualiza uma horta existente", description = "Atualiza os dados de uma horta e/ou sua imagem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horta atualizada com sucesso", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Horta não encontrada", content = @Content)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HortaResponseDTO> atualizar(
            @Parameter(description = "ID da horta a ser atualizada") @PathVariable Integer id,
            @Parameter(description = "Dados da horta a serem atualizados, em formato JSON.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HortaUpdateDTO.class))) @RequestPart("horta") @Valid HortaUpdateDTO hortaUpdateDTO,
            @Parameter(description = "Novo arquivo de imagem (opcional)") @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        HortaResponseDTO hortaAtualizada = hortaService.atualizar(id, hortaUpdateDTO, imagem);
        return ResponseEntity.ok(hortaAtualizada);
    }

    @Operation(summary = "Exclui uma horta pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horta excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só tecnicos e ADM tem perimssão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Horta não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da horta a ser excluída") @PathVariable Integer id) {
        hortaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Altera o status de uma horta (ex: PENDENTE para ATIVA)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Horta não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Status inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado, só tecnicos e ADM tem perimssão", content = @Content)
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<HortaResponseDTO> alterarStatus(
            @Parameter(description = "ID da horta que terá o status alterado") @PathVariable Integer id,
            @Parameter(description = "O novo status para a horta", schema = @Schema(implementation = Horta.StatusHorta.class)) @RequestParam("status") Horta.StatusHorta status) {

        HortaResponseDTO horta = hortaService.alterarStatus(id, status);
        return ResponseEntity.ok(horta);
    }

    @Operation(summary = "Exporta os dados de todas as hortas para um arquivo Excel", description = "Gera e baixa um arquivo .xlsx com o relatório de todas as hortas.")
    @ApiResponse(responseCode = "200", description = "Arquivo Excel gerado", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    @GetMapping("/relatorios/download/excel") // rota alterada para evitar conflito com /{id}
    public ResponseEntity<InputStreamResource> exportarHortasParaExcel() throws IOException {

        List<HortaResponseDTO> hortasDTO = hortaService.listarTodas();

        ByteArrayInputStream bais = excelService.exportarHortasParaExcel(hortasDTO);

        HttpHeaders headers = new HttpHeaders();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String filename = "relatorio_hortas_" + timestamp + ".xlsx";

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(bais));
    }

    @Operation(summary = "Exporta os dados de uma horta especifica para um arquivo em PDF", description = "Gera e baixa um arquivo .pdf com os dados da horta selecionada.")
    @ApiResponse(responseCode = "200", description = "Arquivo PDF gerado", content = @Content(mediaType = "application/pdf"))
    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> gerarRelatorioPdf(@PathVariable Integer id) throws IOException {
        HortaResponseDTO hortaDTO = hortaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com ID: " + id));

        ByteArrayInputStream bis = pdfService.gerarPdfHorta(hortaDTO);

        HttpHeaders headers = new HttpHeaders();
        String filename = "relatorio_horta_" + hortaDTO.getIdHorta() + ".pdf";

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}