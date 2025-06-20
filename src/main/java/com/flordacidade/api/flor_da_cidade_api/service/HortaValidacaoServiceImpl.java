package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.stereotype.Service;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaValidacaoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HortaValidacaoServiceImpl implements HortaValidacaoService {

    private final HortaValidacaoRepository validacaoRepository;
    private final HortaRepository hortaRepository;
    private final TecnicoRepository tecnicoRepository;

    @Override
    @Transactional
    public HortaValidacao validarHorta(HortaValidacao validacao) {
        Horta hortaInput = validacao.getHorta();

        final Horta horta = hortaRepository.findById(hortaInput.getIdHorta())
                .orElseThrow(() -> new RuntimeException("Horta não encontrada"));

        TecnicoModel tecnico = tecnicoRepository.findById(validacao.getTecnico().getIdTecnico())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        // Atualiza o status da horta se enviado
        if (hortaInput.getStatusHorta() != null) {
            horta.setStatusHorta(hortaInput.getStatusHorta());
            hortaRepository.save(horta);
        }

        validacao.setHorta(horta);
        validacao.setTecnico(tecnico);

        return validacaoRepository.save(validacao);

        public List<HortaValidacao>listarTodas() {
        return validacaoRepository.findAll();
        }

    }

}
