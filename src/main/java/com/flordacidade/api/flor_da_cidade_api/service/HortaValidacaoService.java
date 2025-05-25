package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaValidacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HortaValidacaoService {

    private final HortaValidacaoRepository validacaoRepository;
    private final HortaRepository hortaRepository;

    public HortaValidacaoService(HortaValidacaoRepository validacaoRepository, HortaRepository hortaRepository) {
        this.validacaoRepository = validacaoRepository;
        this.hortaRepository = hortaRepository;
    }

    @Transactional
    public HortaValidacao salvarValidacao(HortaValidacao validacao) {
        Horta horta = validacao.getHorta();

        if (horta != null) {
            switch (validacao.getStatus()) {
                case VISITA_AGENDADA:
                    horta.setStatusHorta(StatusHorta.VISITA_AGENDADA);
                    break;
                case ATIVA:
                    horta.setStatusHorta(StatusHorta.ATIVA);
                    break;
                case INATIVA:
                    horta.setStatusHorta(StatusHorta.INATIVA);
                    break;
                default:
                    throw new IllegalArgumentException("Status inválido: " + validacao.getStatus());
            }
            hortaRepository.save(horta);
        }

        return validacaoRepository.save(validacao);
    }

    public List<HortaValidacao> listarTodas() {
        return validacaoRepository.findAll();
    }
}