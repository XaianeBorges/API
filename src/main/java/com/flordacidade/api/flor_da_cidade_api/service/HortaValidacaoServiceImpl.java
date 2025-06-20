package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.stereotype.Service;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaValidacaoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HortaValidacaoServiceImpl implements HortaValidacaoService {

    private final HortaValidacaoRepository validacaoRepository;
    private final HortaRepository hortaRepository;
    private final TecnicoRepository tecnicoRepository;

    @Override // Este @Override não dará mais erro
    @Transactional
    public HortaValidacao validarHorta(HortaValidacao validacao, StatusHorta novoStatus) {
        Horta horta = hortaRepository.findById(validacao.getHorta().getIdHorta())
                .orElseThrow(() -> new RuntimeException("Horta com ID " + validacao.getHorta().getIdHorta() + " não encontrada"));

        TecnicoModel tecnico = tecnicoRepository.findById(validacao.getTecnico().getIdTecnico())
                .orElseThrow(() -> new RuntimeException("Técnico com ID " + validacao.getTecnico().getIdTecnico() + " não encontrado"));

        horta.setStatusHorta(novoStatus);
        hortaRepository.save(horta);

        validacao.setHorta(horta);
        validacao.setTecnico(tecnico);

        return validacaoRepository.save(validacao);
    }

    @Override
    public List<HortaValidacao> listarTodas() {
        return validacaoRepository.findAll();
    }
}