package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.service.HortaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hortas")
public class HortaController {

    @Autowired
    private HortaService hortaService;

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

    // Endpoint para listar solicitações pendentes formatadas para a tela específica
    @GetMapping("/solicitacoes/pendentes")
    public ResponseEntity<List<Map<String, Object>>> getPendingHortaRequests() {
        List<Map<String, Object>> requests = hortaService.getPendingHortaRequests();
        return ResponseEntity.ok(requests); // Retorna 200 OK com lista vazia se não houver pendentes
    }

    // Adicionar este endpoint em HortaController.java
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Map<String, Object>>> getHortasByStatus(@PathVariable String status) {
        try {
            Horta.StatusHorta statusEnum = Horta.StatusHorta.valueOf(status.toUpperCase());
            List<Map<String, Object>> hortas = hortaService.getHortasByStatusWithUserDetails(statusEnum);
            return ResponseEntity.ok(hortas);
        } catch (IllegalArgumentException e) {
            // Logar o erro e retornar um bad request se o status for inválido
            // Logger.error("Status inválido fornecido: " + status, e);
            return ResponseEntity.badRequest().body(null); // Ou uma mensagem de erro mais específica
        }
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Horta> criar(
            // Dados da Horta (campos texto)
            @RequestParam("nomeHorta") String nomeHorta,
            @RequestParam(value = "funcaoUniEnsino", required = false) String funcaoUniEnsino, // Exemplo de campo opcional
            @RequestParam(value = "ocupacaoPrincipal", required = false) String ocupacaoPrincipal,
            @RequestParam("endereco") String endereco,
            @RequestParam(value = "enderecoAlternativo", required = false) String enderecoAlternativo,
            @RequestParam("tamanhoAreaProducao") Float tamanhoAreaProducao,
            @RequestParam(value = "caracteristicaGrupo", required = false) String caracteristicaGrupo,
            @RequestParam("qntPessoas") Integer qntPessoas,
            @RequestParam("atividadeDescricao") String atividadeDescricao,
            @RequestParam(value = "parceria", required = false) String parceria,
            // IDs das entidades relacionadas
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("idUnidadeEnsino") Integer idUnidadeEnsino,
            @RequestParam("idAreaClassificacao") Integer idAreaClassificacao,
            @RequestParam("idAtividadesProdutivas") Integer idAtividadesProdutivas,
            @RequestParam("idTipoDeHorta") Integer idTipoDeHorta,
            // Arquivo de imagem
            @RequestParam("imagem") MultipartFile imagem) {

        Horta novaHorta = new Horta();
        novaHorta.setNomeHorta(nomeHorta);
        novaHorta.setFuncaoUniEnsino(funcaoUniEnsino);
        novaHorta.setOcupacaoPrincipal(ocupacaoPrincipal);
        novaHorta.setEndereco(endereco);
        novaHorta.setEnderecoAlternativo(enderecoAlternativo); // Adicionado
        novaHorta.setTamanhoAreaProducao(tamanhoAreaProducao);
        novaHorta.setCaracteristicaGrupo(caracteristicaGrupo);
        novaHorta.setQntPessoas(qntPessoas);
        novaHorta.setAtividadeDescricao(atividadeDescricao);
        novaHorta.setParceria(parceria);
        // O statusHorta e imagemCaminho serão definidos no service

        Horta hortaSalva = hortaService.salvar(novaHorta, imagem, idUsuario, idUnidadeEnsino, idAreaClassificacao,
                idAtividadesProdutivas, idTipoDeHorta);
        return ResponseEntity.status(HttpStatus.CREATED).body(hortaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horta> atualizar(@PathVariable Integer id, @RequestBody Horta horta) {
        // Nota: O @RequestBody Horta aqui espera um JSON com a estrutura da entidade Horta.
        // Se for para atualizar a imagem também, geralmente se faz um endpoint separado ou multipart/form-data.
        // O método de serviço atualizado lida com atualizações parciais dos campos e relações.
        Horta atualizada = hortaService.atualizar(id, horta);
        return ResponseEntity.ok(atualizada);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Horta> alterarStatus(@PathVariable Integer id, @RequestParam("status") Horta.StatusHorta status) {
        // Nota: @RequestParam espera o valor do status como um parâmetro de requisição.
        // Ex: /api/hortas/1/status?status=ATIVA
        // Spring converterá a String "ATIVA" para o enum Horta.StatusHorta.ATIVA
        Horta horta = hortaService.alterarStatus(id, status);
        return ResponseEntity.ok(horta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        hortaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}