package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.service.CursoService;
import com.flordacidade.api.flor_da_cidade_api.service.ExcelService;
import com.flordacidade.api.flor_da_cidade_api.service.InscricaoCursoService;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.InscricaoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.CursoMapper;
import com.flordacidade.api.flor_da_cidade_api.mapper.InscricaoCursoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Tag(name = "Cursos e Oficinas", description = "Endpoints para o gerenciamento de Cursos e Oficinas")
public class CursoController {

        private final CursoService cursoService;
        private final ExcelService excelExportService;
        private final CursoMapper cursoMapper;
        private final ObjectMapper objectMapper;
        private final InscricaoCursoService inscricaoCursoService;
        private final InscricaoCursoMapper inscricaoCursoMapper;

        @Operation(summary = "Obtém as opções de enums para formulários de cursos", description = "Retorna uma lista de valores possíveis para os tipos de atividade, públicos-alvo e turnos.")
        @ApiResponse(responseCode = "200", description = "Opções retornadas com sucesso")
        @GetMapping("/opcoes")
        public ResponseEntity<Map<String, List<String>>> getFormOptions() {
                Map<String, List<String>> options = Map.of(
                                "tiposAtividade",
                                Arrays.stream(CursoModel.TipoAtividade.values()).map(Enum::name)
                                                .collect(Collectors.toList()),
                                "publicosAlvo",
                                Arrays.stream(CursoModel.PublicoAlvo.values()).map(Enum::name)
                                                .collect(Collectors.toList()),
                                "turnos",
                                Arrays.stream(CursoModel.Turno.values()).map(Enum::name).collect(Collectors.toList()));
                return ResponseEntity.ok(options);
        }

        @Operation(summary = "Lista todos os cursos cadastrados")
        @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDTO.class)))
        @GetMapping
        public ResponseEntity<List<CursoResponseDTO>> listarTodos() {
                List<CursoModel> cursos = cursoService.listarTodos();
                return ResponseEntity.ok(cursoMapper.toResponseDTOList(cursos));
        }

        @Operation(summary = "Busca um curso pelo seu ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Curso encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content) })
        @GetMapping("/{id}")
        public ResponseEntity<CursoResponseDTO> buscarPorId(@PathVariable Integer id) {
                return cursoService.buscarPorId(id)
                                .map(curso -> ResponseEntity.ok(cursoMapper.toResponseDTO(curso)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Busca cursos por status")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos filtrada por status retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Status inválido fornecido")})
        @GetMapping("/status")
        public ResponseEntity<List<CursoResponseDTO>> listarPorStatus(@RequestParam("status") CursoModel.Status status) {
            List<CursoModel> cursos = cursoService.listarPorStatus(status);
            return ResponseEntity.ok(cursoMapper.toResponseDTOList(cursos));
        }

        @Operation(summary = "Lista todos os cursos arquivados")
        @ApiResponse(responseCode = "200", description = "Lista de cursos arquivados retornada com sucesso")
        @GetMapping("/arquivados")
        public ResponseEntity<List<CursoResponseDTO>> listarArquivados() {
            List<CursoModel> cursosArquivados = cursoService.listarArquivados();
            return ResponseEntity.ok(cursoMapper.toResponseDTOList(cursosArquivados));
        }

        @Operation(summary = "Lista todos os inscritos em um curso específico")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscrições retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
        })
        @GetMapping("/{id}/inscricoes")
        public ResponseEntity<List<InscricaoResponseDTO>> listarInscritosPorCurso(@PathVariable Integer id) {
            List<InscricaoCursoModel> inscricoes = inscricaoCursoService.listarPorCursoId(id);
            return ResponseEntity.ok(inscricaoCursoMapper.toResponseDTOList(inscricoes));
        }

        @Operation(summary = "Cria um novo curso ou oficina")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Curso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
        })
       @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<CursoResponseDTO> criar(
          @Parameter(description = "Dados do curso em JSON", schema = @Schema(type = "string", format = "binary")) @RequestPart("curso") String cursoJson, 
          @Parameter(description = "Arquivo de imagem para o banner (opcional)") @RequestPart(value = "banner", required = false) MultipartFile bannerFile) {
    
          try {

              CursoRequestDTO cursoDTO = objectMapper.readValue(cursoJson, CursoRequestDTO.class);

              CursoModel cursoSalvo = cursoService.salvar(cursoDTO, bannerFile);
              return ResponseEntity.status(HttpStatus.CREATED).body(cursoMapper.toResponseDTO(cursoSalvo));

          } catch (JsonProcessingException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato do JSON do curso é inválido.", e);
            }
        }

        @Operation(summary = "Atualiza um curso ou oficina existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content)
        })
        @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<CursoResponseDTO> atualizar(
          @Parameter(description = "ID do curso a ser atualizado") @PathVariable Integer id,
          @Parameter(description = "Dados do curso a serem atualizados, em JSON", schema = @Schema(type = "string", format = "binary")) @RequestPart("curso") String cursoUpdateJson, // Mude de CursoUpdateDTO para String
          @Parameter(description = "Novo arquivo de imagem para o banner (opcional)") @RequestPart(value = "banner", required = false) MultipartFile bannerFile) {
    
          try {
        
            CursoUpdateDTO cursoUpdateDTO = objectMapper.readValue(cursoUpdateJson, CursoUpdateDTO.class);

            CursoModel cursoAtualizado = cursoService.atualizar(id, cursoUpdateDTO, bannerFile);
            return ResponseEntity.ok(cursoMapper.toResponseDTO(cursoAtualizado));

          } catch (JsonProcessingException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato do JSON de atualização do curso é inválido.", e);
           }
        }

        @Operation(summary = "Exclui um curso ou oficina")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Curso excluído com sucesso", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content) })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Integer id) {
                cursoService.deletar(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Exporta os cursos ativos para um arquivo Excel", description = "Gera e baixa um arquivo .xlsx com o relatório de todos os cursos com status 'Ativo'.")
        @ApiResponse(responseCode = "200", description = "Arquivo Excel gerado com sucesso") // documentar
        @GetMapping("/download/excel")
        public ResponseEntity<InputStreamResource> exportarCursosAtivosParaExcel() throws IOException {

                List<CursoModel> todosOsCursos = cursoService.listarTodos();

                List<CursoModel> cursosAtivos = todosOsCursos.stream()
                                .filter(curso -> curso.getStatus() == CursoModel.Status.ATIVO)
                                .collect(Collectors.toList());

                List<CursoResponseDTO> cursosAtivosDTO = cursoMapper.toResponseDTOList(cursosAtivos);

                ByteArrayInputStream bais = excelExportService.exportarCursosParaExcel(cursosAtivosDTO);

                HttpHeaders headers = new HttpHeaders();
                String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String filename = "relatorio-cursos-ativos-" + timestamp + ".xlsx";

                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

                return ResponseEntity
                                .ok()
                                .headers(headers)
                                .contentType(
                                                MediaType.parseMediaType(
                                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                                .body(new InputStreamResource(bais));
        }
}
