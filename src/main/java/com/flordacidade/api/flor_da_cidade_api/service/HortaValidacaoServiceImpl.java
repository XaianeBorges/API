package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.stereotype.Service;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaValidacaoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.HortaValidacaoMapper;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HortaValidacaoServiceImpl implements HortaValidacaoService {

    private final HortaValidacaoRepository hortaValidacaoRepository;
    private final HortaRepository hortaRepository;
    private final TecnicoRepository tecnicoRepository;
    private final HortaValidacaoMapper mapper;

    @Override
    @Transactional
    public HortaValidacaoResponseDTO validarHorta(HortaValidacaoRequestDTO requestDTO) {
        Horta horta = hortaRepository.findById(requestDTO.getIdHorta())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Horta não encontrada com ID: " + requestDTO.getIdHorta()));

        TecnicoModel tecnico = tecnicoRepository.findById(requestDTO.getIdTecnico())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Técnico não encontrado com ID: " + requestDTO.getIdTecnico()));

        HortaValidacao novaValidacao = mapper.toEntity(requestDTO);
        novaValidacao.setHorta(horta);
        novaValidacao.setTecnico(tecnico);

        HortaValidacao validacaoSalva = hortaValidacaoRepository.save(novaValidacao);

        horta.setStatusHorta(Horta.StatusHorta.ATIVA);
        hortaRepository.save(horta);

        return mapper.toResponseDTO(validacaoSalva);
    }
}