package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HortaService {

    @Autowired
    private HortaRepository hortaRepository;

    public List<Horta> listarTodas() {
        return hortaRepository.findAll();
    }

    public Optional<Horta> buscarPorId(Long id) {
        return hortaRepository.findById(id);
    }

    public Horta salvar(Horta horta) {
        return hortaRepository.save(horta);
    }

    public Horta atualizar(Long id, Horta hortaAtualizada) {
        return hortaRepository.findById(id).map(horta -> {
            horta.setFuncaoUniEnsino(hortaAtualizada.getFuncaoUniEnsino());
            horta.setStatusHorta(hortaAtualizada.getStatusHorta());
            horta.setOcupacaoPrincipal(hortaAtualizada.getOcupacaoPrincipal());
            horta.setEndereco(hortaAtualizada.getEndereco());
            horta.setEnderecoAlternativo(hortaAtualizada.getEnderecoAlternativo());
            horta.setUnidadeEnsino(hortaAtualizada.getUnidadeEnsino());
            horta.setTamanhoAreaProducao(hortaAtualizada.getTamanhoAreaProducao());
            horta.setAreaClassificacao(hortaAtualizada.getAreaClassificacao());
            horta.setAtividadesProdutivas(hortaAtualizada.getAtividadesProdutivas());
            horta.setCaracteristicaGrupo(hortaAtualizada.getCaracteristicaGrupo());
            horta.setQntPessoas(hortaAtualizada.getQntPessoas());
            horta.setAtividadeDescricao(hortaAtualizada.getAtividadeDescricao());
            horta.setImagemCaminho(hortaAtualizada.getImagemCaminho());
            horta.setUsuario(hortaAtualizada.getUsuario());
            horta.setTipoDeHorta(hortaAtualizada.getTipoDeHorta());
            horta.setParceria(hortaAtualizada.getParceria());
            return hortaRepository.save(horta);
        }).orElseThrow(() -> new RuntimeException("Horta não encontrada com id: " + id));
    }

    public void deletar(Long id) {
        hortaRepository.deleteById(id);
    }

    // Método específico para alterar status da horta
    public Horta alterarStatus(Long id, StatusHorta status) {
        return hortaRepository.findById(id).map(horta -> {
            horta.setStatusHorta(status);
            return hortaRepository.save(horta);
        }).orElseThrow(() -> new RuntimeException("Horta não encontrada com id: " + id));
    }

    public void alterarStatus(Integer idHorta, StatusHorta novoStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'alterarStatus'");
    }
}