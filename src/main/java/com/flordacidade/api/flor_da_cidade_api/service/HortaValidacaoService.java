package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import java.util.List;

public interface HortaValidacaoService {

    // A assinatura agora corresponde à implementação e ao que o controller espera
    HortaValidacao validarHorta(HortaValidacao validacao, StatusHorta novoStatus);

    List<HortaValidacao> listarTodas();
}