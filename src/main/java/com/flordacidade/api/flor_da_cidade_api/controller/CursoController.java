package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.CursoModel.PublicoAlvo;
import com.flordacidade.api.flor_da_cidade_api.model.CursoModel.TipoAtividade;
import com.flordacidade.api.flor_da_cidade_api.model.CursoModel.Turno;
import com.flordacidade.api.flor_da_cidade_api.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public List<CursoModel> listarTodos() {
        return cursoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoModel> buscarPorId(@PathVariable Integer id) {
        return cursoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursoModel> criar(

            @RequestParam("tipoAtividade") TipoAtividade tipoAtividade,
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("local") String local,
            @RequestParam("instituicao") String instituicao,
            @RequestParam("publicoAlvo") PublicoAlvo publicoAlvo,
            @RequestParam("dataInicio") LocalDate dataInicio,
            @RequestParam("dataFim") LocalDate dataFim,
            @RequestParam("dataInscInicio") LocalDate dataInscInicio,
            @RequestParam("dataInscFim") LocalDate dataInscFim,
            @RequestParam("turno") Turno turno,
            @RequestParam("maxPessoas") int maxPessoas,
            @RequestParam("cargaHoraria") int cargaHoraria,
            @RequestParam(value = "fotoBanner", required = false) MultipartFile fotoBanner) {
        CursoModel novoCurso = new CursoModel();

        novoCurso.setTipoAtividade(tipoAtividade);
        novoCurso.setNome(nome);
        novoCurso.setDescricao(descricao);
        novoCurso.setLocal(local);
        novoCurso.setInstituicao(instituicao);
        novoCurso.setPublicoAlvo(publicoAlvo);
        novoCurso.setDataInicio(dataInicio);
        novoCurso.setDataFim(dataFim);
        novoCurso.setDataInscInicio(dataInscInicio);
        novoCurso.setDataInscFim(dataInscFim);
        novoCurso.setTurno(turno);
        novoCurso.setMaxPessoas(maxPessoas);
        novoCurso.setCargaHoraria(cargaHoraria);
        novoCurso.setAtivo(true);

        CursoModel cursoSalvo = cursoService.salvar(novoCurso, fotoBanner);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo); // Boa prática: retornar 201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoModel> atualizar(@PathVariable Integer id, @RequestBody CursoModel curso) {
        return ResponseEntity.ok(cursoService.atualizar(id, curso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
