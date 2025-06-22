package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.service.HortaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hortas")
public class HortaController {

    @Autowired
    private HortaService hortaService;

    @GetMapping
    public List<Horta> listarTodas() {
        return hortaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horta> buscarPorId(@PathVariable Integer id) {
        return hortaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Horta> criar(

            @RequestParam("nomeHorta") String nomeHorta,
            @RequestParam("funcaoUniEnsino") String funcaoUniEnsino,
            @RequestParam("ocupacaoPrincipal") String ocupacaoPrincipal,
            @RequestParam("endereco") String endereco,
            @RequestParam("tamanhoAreaProducao") Float tamanhoAreaProducao,
            @RequestParam("caracteristicaGrupo") String caracteristicaGrupo,
            @RequestParam("qntPessoas") Integer qntPessoas,
            @RequestParam("atividadeDescricao") String atividadeDescricao,
            @RequestParam("parceria") String parceria,
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
            // Dados da horta
            @RequestParam("nomeHorta") String nomeHorta,
            @RequestParam("funcaoUniEnsino") String funcaoUniEnsino,
            // Adicione todos os outros campos de Horta como @RequestParam
            // ...
            @RequestParam("tamanhoAreaProducao") Float tamanhoAreaProducao,
            // IDs das entidades relacionadas (tornando-os opcionais na atualização)
            @RequestParam(value = "idUsuario", required = false) Integer idUsuario,
            @RequestParam(value = "idUnidadeEnsino", required = false) Integer idUnidadeEnsino,
            @RequestParam(value = "idAreaClassificacao", required = false) Integer idAreaClassificacao,
            @RequestParam(value = "idAtividadesProdutivas", required = false) Integer idAtividadesProdutivas,
            @RequestParam(value = "idTipoDeHorta", required = false) Integer idTipoDeHorta,
            // O arquivo de imagem é opcional na atualização
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        // Monta um objeto Horta com os dados recebidos para passar ao serviço
        Horta dadosParciais = new Horta();
        dadosParciais.setNomeHorta(nomeHorta);
        dadosParciais.setFuncaoUniEnsino(funcaoUniEnsino);
        dadosParciais.setTamanhoAreaProducao(tamanhoAreaProducao);
        // ... set para todos os outros campos

        Horta hortaAtualizada = hortaService.atualizar(id, dadosParciais, imagem, idUsuario, idUnidadeEnsino,
                idAreaClassificacao, idAtividadesProdutivas, idTipoDeHorta);

        return ResponseEntity.ok(hortaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        hortaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Horta> alterarStatus(@PathVariable Integer id, @RequestParam Horta.StatusHorta status) {
        Horta horta = hortaService.alterarStatus(id, status);
        return ResponseEntity.ok(horta);
    }
}
