package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoResponseDTO;

public interface HortaValidacaoService {

    HortaValidacaoResponseDTO validarHorta(HortaValidacaoRequestDTO requestDTO);
}