// Caminho do Arquivo: src/main/java/com/flordacidade/api/flor_da_cidade_api/controller/HortaController.java

package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.exception.*;
import com.flordacidade.api.flor_da_cidade_api.service.HortaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.flordacidade.api.flor_da_cidade_api.service.PdfService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.flordacidade.api.flor_da_cidade_api.service.ExcelService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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

    @GetMapping("/public/ativas")
    public ResponseEntity<List<Horta>> getPublicActiveHortas() {
        List<Horta> hortasAtivas = hortaService.listarAtivasParaMapa();
        return ResponseEntity.ok(hortasAtivas);
    }

    @GetMapping
    public ResponseEntity<List<Horta>> listarTodas() {
        List<Horta> hortas = hortaService.listarTodas();
        return ResponseEntity.ok(hortas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horta> buscarPorId(@PathVariable Integer id) {
        return hortaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/solicitacoes/pendentes")
    public ResponseEntity<List<Map<String, Object>>> getPendingHortaRequests() {
        List<Map<String, Object>> requests = hortaService.getPendingHortaRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
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

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Horta> criar(
            @RequestParam("nomeHorta") String nomeHorta,
            @RequestParam(value = "funcaoUniEnsino", required = false) String funcaoUniEnsino,
            @RequestParam(value = "ocupacaoPrincipal", required = false) String ocupacaoPrincipal,
            @RequestParam("endereco") String endereco,
            @RequestParam(value = "enderecoAlternativo", required = false) String enderecoAlternativo,
            @RequestParam("tamanhoAreaProducao") Float tamanhoAreaProducao,
            @RequestParam(value = "caracteristicaGrupo", required = false) String caracteristicaGrupo,
            @RequestParam("qntPessoas") Integer qntPessoas,
            @RequestParam("atividadeDescricao") String atividadeDescricao,
            @RequestParam(value = "parceria", required = false) String parceria,
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("idUnidadeEnsino") Integer idUnidadeEnsino,
            @RequestParam("idAreaClassificacao") Integer idAreaClassificacao,
            @RequestParam("idAtividadesProdutivas") Integer idAtividadesProdutivas,
            @RequestParam("idTipoDeHorta") Integer idTipoDeHorta,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {

        Horta novaHorta = new Horta();
        novaHorta.setNomeHorta(nomeHorta);
        novaHorta.setFuncaoUniEnsino(funcaoUniEnsino);
        novaHorta.setOcupacaoPrincipal(ocupacaoPrincipal);
        novaHorta.setEndereco(endereco);
        novaHorta.setEnderecoAlternativo(enderecoAlternativo);
        novaHorta.setTamanhoAreaProducao(tamanhoAreaProducao);
        novaHorta.setCaracteristicaGrupo(caracteristicaGrupo);
        novaHorta.setQntPessoas(qntPessoas);
        novaHorta.setAtividadeDescricao(atividadeDescricao);
        novaHorta.setParceria(parceria);

        Horta hortaSalva = hortaService.salvar(novaHorta, imagem, idUsuario, idUnidadeEnsino, idAreaClassificacao,
                idAtividadesProdutivas, idTipoDeHorta);
        return ResponseEntity.status(HttpStatus.CREATED).body(hortaSalva);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Horta> atualizar(
            @PathVariable Integer id,
            @RequestParam("nomeHorta") String nomeHorta,
            @RequestParam(value = "funcaoUniEnsino", required = false) String funcaoUniEnsino,
            @RequestParam(value = "ocupacaoPrincipal", required = false) String ocupacaoPrincipal,
            @RequestParam(value = "endereco", required = false) String endereco,
            @RequestParam(value = "enderecoAlternativo", required = false) String enderecoAlternativo,
            @RequestParam(value = "tamanhoAreaProducao", required = false) Float tamanhoAreaProducao,
            @RequestParam(value = "caracteristicaGrupo", required = false) String caracteristicaGrupo,
            @RequestParam(value = "qntPessoas", required = false) Integer qntPessoas,
            @RequestParam(value = "atividadeDescricao", required = false) String atividadeDescricao,
            @RequestParam(value = "parceria", required = false) String parceria,
            @RequestParam(value = "statusHorta", required = false) Horta.StatusHorta statusHorta,
            @RequestParam(value = "idUsuario", required = false) Integer idUsuario,
            @RequestParam(value = "idUnidadeEnsino", required = false) Integer idUnidadeEnsino,
            @RequestParam(value = "idAreaClassificacao", required = false) Integer idAreaClassificacao,
            @RequestParam(value = "idAtividadesProdutivas", required = false) Integer idAtividadesProdutivas,
            @RequestParam(value = "idTipoDeHorta", required = false) Integer idTipoDeHorta,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {

        Horta dadosParaAtualizar = new Horta();
        dadosParaAtualizar.setNomeHorta(nomeHorta);
        if (funcaoUniEnsino != null)
            dadosParaAtualizar.setFuncaoUniEnsino(funcaoUniEnsino);
        if (ocupacaoPrincipal != null)
            dadosParaAtualizar.setOcupacaoPrincipal(ocupacaoPrincipal);
        if (endereco != null)
            dadosParaAtualizar.setEndereco(endereco);
        if (enderecoAlternativo != null)
            dadosParaAtualizar.setEnderecoAlternativo(enderecoAlternativo);
        if (tamanhoAreaProducao != null)
            dadosParaAtualizar.setTamanhoAreaProducao(tamanhoAreaProducao);
        if (caracteristicaGrupo != null)
            dadosParaAtualizar.setCaracteristicaGrupo(caracteristicaGrupo);
        if (qntPessoas != null)
            dadosParaAtualizar.setQntPessoas(qntPessoas);
        if (atividadeDescricao != null)
            dadosParaAtualizar.setAtividadeDescricao(atividadeDescricao);
        if (parceria != null)
            dadosParaAtualizar.setParceria(parceria);
        if (statusHorta != null)
            dadosParaAtualizar.setStatusHorta(statusHorta);

        Horta hortaAtualizada = hortaService.atualizar(id, dadosParaAtualizar, imagem, idUsuario, idUnidadeEnsino,
                idAreaClassificacao, idAtividadesProdutivas, idTipoDeHorta);

        return ResponseEntity.ok(hortaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        hortaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Horta> alterarStatus(@PathVariable Integer id,
            @RequestParam("status") Horta.StatusHorta status) {
        Horta horta = hortaService.alterarStatus(id, status);
        return ResponseEntity.ok(horta);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> exportarHortasParaExcel() throws IOException {
        List<Horta> hortas = hortaService.listarTodas();
        ByteArrayInputStream bais = excelService.exportarHortasParaExcel(hortas);

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
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(bais));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> gerarRelatorioPdf(@PathVariable Integer id) throws IOException {
        // 1. Busca a horta específica ou retorna 404 Not Found se não existir
        Horta horta = hortaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com ID: " + id));

        // 2. Chama o serviço para gerar o PDF em memória
        ByteArrayInputStream bis = pdfService.gerarPdfHorta(horta);

        // 3. Configura os cabeçalhos da resposta HTTP para o PDF
        HttpHeaders headers = new HttpHeaders();
        String filename = "relatorio_horta_" + horta.getIdHorta() + ".pdf";

        // "inline" tenta abrir no navegador, "attachment" força o download
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF) // Define o Content-Type para PDF
                .body(new InputStreamResource(bis));
    }
}