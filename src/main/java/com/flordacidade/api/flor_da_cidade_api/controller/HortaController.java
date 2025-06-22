package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.service.HortaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map; // Mantido da Esquerda

@RestController
@RequestMapping("/api/hortas")
public class HortaController {

    @Autowired
    private HortaService hortaService;

    // Versão da Esquerda (retorna ResponseEntity)
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

    // Endpoint mantido da Esquerda
    @GetMapping("/solicitacoes/pendentes")
    public ResponseEntity<List<Map<String, Object>>> getPendingHortaRequests() {
        List<Map<String, Object>> requests = hortaService.getPendingHortaRequests();
        return ResponseEntity.ok(requests);
    }

    // Endpoint mantido da Esquerda
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

    // Versão da Esquerda (com campos opcionais e enderecoAlternativo)
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
            @RequestParam("imagem") MultipartFile imagem) {

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

    // Versão da Direita (adaptada e completada) para multipart/form-data e compatibilidade com o service
    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Horta> atualizar(
            @PathVariable Integer id,
            // Dados da horta (tornando opcionais, já que é uma atualização/PATCH-like)
            @RequestParam(value = "nomeHorta", required = false) String nomeHorta,
            @RequestParam(value = "funcaoUniEnsino", required = false) String funcaoUniEnsino,
            @RequestParam(value = "ocupacaoPrincipal", required = false) String ocupacaoPrincipal,
            @RequestParam(value = "endereco", required = false) String endereco,
            @RequestParam(value = "enderecoAlternativo", required = false) String enderecoAlternativo,
            @RequestParam(value = "tamanhoAreaProducao", required = false) Float tamanhoAreaProducao,
            @RequestParam(value = "caracteristicaGrupo", required = false) String caracteristicaGrupo,
            @RequestParam(value = "qntPessoas", required = false) Integer qntPessoas,
            @RequestParam(value = "atividadeDescricao", required = false) String atividadeDescricao,
            @RequestParam(value = "parceria", required = false) String parceria,
            @RequestParam(value = "statusHorta", required = false) Horta.StatusHorta statusHorta, // Se permitir atualização de status aqui também
            // IDs das entidades relacionadas (opcionais na atualização)
            @RequestParam(value = "idUsuario", required = false) Integer idUsuario,
            @RequestParam(value = "idUnidadeEnsino", required = false) Integer idUnidadeEnsino,
            @RequestParam(value = "idAreaClassificacao", required = false) Integer idAreaClassificacao,
            @RequestParam(value = "idAtividadesProdutivas", required = false) Integer idAtividadesProdutivas,
            @RequestParam(value = "idTipoDeHorta", required = false) Integer idTipoDeHorta,
            // O arquivo de imagem é opcional na atualização
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {

        Horta dadosParciais = new Horta();
        // Seta apenas os campos que foram fornecidos (não nulos)
        if (nomeHorta != null) dadosParciais.setNomeHorta(nomeHorta);
        if (funcaoUniEnsino != null) dadosParciais.setFuncaoUniEnsino(funcaoUniEnsino);
        if (ocupacaoPrincipal != null) dadosParciais.setOcupacaoPrincipal(ocupacaoPrincipal);
        if (endereco != null) dadosParciais.setEndereco(endereco);
        if (enderecoAlternativo != null) dadosParciais.setEnderecoAlternativo(enderecoAlternativo);
        if (tamanhoAreaProducao != null) dadosParciais.setTamanhoAreaProducao(tamanhoAreaProducao);
        if (caracteristicaGrupo != null) dadosParciais.setCaracteristicaGrupo(caracteristicaGrupo);
        if (qntPessoas != null) dadosParciais.setQntPessoas(qntPessoas);
        if (atividadeDescricao != null) dadosParciais.setAtividadeDescricao(atividadeDescricao);
        if (parceria != null) dadosParciais.setParceria(parceria);
        if (statusHorta != null) dadosParciais.setStatusHorta(statusHorta); // Se o status for atualizável aqui

        // A imagem e os IDs são passados diretamente para o serviço
        Horta hortaAtualizada = hortaService.atualizar(id, dadosParciais, imagem, idUsuario, idUnidadeEnsino,
                idAreaClassificacao, idAtividadesProdutivas, idTipoDeHorta);

        return ResponseEntity.ok(hortaAtualizada);
    }

    // Versão da Esquerda (mais explícita)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Horta> alterarStatus(@PathVariable Integer id, @RequestParam("status") Horta.StatusHorta status) {
        Horta horta = hortaService.alterarStatus(id, status);
        return ResponseEntity.ok(horta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        hortaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}