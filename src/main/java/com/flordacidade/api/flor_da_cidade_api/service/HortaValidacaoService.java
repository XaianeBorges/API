package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaValidacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HortaValidacaoService {

    private final HortaValidacaoRepository validacaoRepo;
    private final HortaService hortaService;

    public HortaValidacaoService(HortaValidacaoRepository validacaoRepo,
            HortaService hortaService) {
        this.validacaoRepo = validacaoRepo;
        this.hortaService = hortaService;
    }

    public List<HortaValidacao> listarTodas() {
        return validacaoRepo.findAll();
    }

    @Transactional
    public HortaValidacao validarHorta(HortaValidacao validacao, StatusHorta novoStatus) {
        // 1) salva o registro de validação
        HortaValidacao salva = validacaoRepo.save(validacao);
        // 2) atualiza status da Horta
        hortaService.alterarStatus(validacao.getHorta().getIdHorta(), novoStatus);
        return salva;
    }
}
