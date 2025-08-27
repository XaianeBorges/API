// Caminho do Arquivo: src/main/java/com/flordacidade/api/flor_da_cidade_api/service/HortaService.java

package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.HortaComUsuarioDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.mapper.HortaMapper;
import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.TipoDeHorta;
import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.AreaClassificacaoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.AtividadesProdutivasRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.TipoDeHortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UnidadeEnsinoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HortaService {

    private static final String PLACEHOLDER_IMAGE_FILENAME = "folhin.png";

    private final HortaRepository hortaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeEnsinoRepository unidadeEnsinoRepository;
    private final ArquivoService fileStorageService;
    private final HortaMapper hortaMapper;
    private final AreaClassificacaoRepository areaClassificacaoRepository;
    private final AtividadesProdutivasRepository atividadesProdutivasRepository;
    private final TipoDeHortaRepository tipoDeHortaRepository;

    @Transactional(readOnly = true)
    public List<Horta> listarAtivasParaMapa() {
        return hortaRepository.findByStatusHortaFetchingDetails(Horta.StatusHorta.ATIVA);
    }

    @Transactional(readOnly = true)
    public List<Horta> listarTodas() {
        return hortaRepository.findAllFetchingAllDetails();
    }

    @Transactional(readOnly = true)
    public Optional<Horta> buscarPorId(Integer id) {
        return hortaRepository.findById(id);
    }

    @Transactional
    public Horta salvar(HortaRequestDTO hortaDTO, MultipartFile imagem) {

        Horta novaHorta = hortaMapper.requestDtoToEntity(hortaDTO);

        UsuarioModel usuario = usuarioRepository.findById(hortaDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + hortaDTO.getIdUsuario()));

        UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository.findById(hortaDTO.getIdUnidadeEnsino())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade de Ensino não encontrada com ID: " + hortaDTO.getIdUnidadeEnsino()));

        AreaClassificacao areaClassificacao = areaClassificacaoRepository.findById(hortaDTO.getIdAreaClassificacao())
                .orElseThrow(() -> new ResourceNotFoundException("Área de Classificação não encontrada com ID: " + hortaDTO.getIdAreaClassificacao()));

        AtividadesProdutivas atividadesProdutivas = atividadesProdutivasRepository.findById(hortaDTO.getIdAtividadesProdutivas())
                .orElseThrow(() -> new ResourceNotFoundException("Atividades Produtivas não encontradas com ID: " + hortaDTO.getIdAtividadesProdutivas()));

        TipoDeHorta tipoDeHorta = tipoDeHortaRepository.findById(hortaDTO.getIdTipoDeHorta())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Horta não encontrado com ID: " + hortaDTO.getIdTipoDeHorta()));

        novaHorta.setUsuario(usuario);
        novaHorta.setUnidadeEnsino(unidadeEnsino);
        novaHorta.setAreaClassificacao(areaClassificacao);
        novaHorta.setAtividadesProdutivas(atividadesProdutivas);
        novaHorta.setTipoDeHorta(tipoDeHorta);

        if (imagem != null && !imagem.isEmpty()) {
            String nomeArquivo = fileStorageService.storeHortaImage(imagem);
            novaHorta.setImagemCaminho(nomeArquivo);
        } else {
            novaHorta.setImagemCaminho(PLACEHOLDER_IMAGE_FILENAME);
        }

        novaHorta.setStatusHorta(Horta.StatusHorta.PENDENTE);

        return hortaRepository.save(novaHorta);
    }

    @Transactional
    public Horta atualizar(Integer id, HortaUpdateDTO hortaUpdateDTO, MultipartFile novaImagem) {

        Horta hortaExistente = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com ID: " + id));

        String imagemAntiga = hortaExistente.getImagemCaminho();

        hortaMapper.updateEntityFromDto(hortaUpdateDTO, hortaExistente);

        if (hortaUpdateDTO.getIdUnidadeEnsino() != null) {
            UnidadeEnsino ue = unidadeEnsinoRepository.findById(hortaUpdateDTO.getIdUnidadeEnsino())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidade de Ensino não encontrada."));
            hortaExistente.setUnidadeEnsino(ue);
        }
        if (hortaUpdateDTO.getIdAreaClassificacao() != null) {
            AreaClassificacao ac = areaClassificacaoRepository.findById(hortaUpdateDTO.getIdAreaClassificacao())
                    .orElseThrow(() -> new ResourceNotFoundException("Área de Classificação não encontrada."));
            hortaExistente.setAreaClassificacao(ac);
        }
        if (hortaUpdateDTO.getIdAtividadesProdutivas() != null) {
            AtividadesProdutivas ap = atividadesProdutivasRepository.findById(hortaUpdateDTO.getIdAtividadesProdutivas())
                    .orElseThrow(() -> new ResourceNotFoundException("Atividades Produtivas não encontradas."));
            hortaExistente.setAtividadesProdutivas(ap);
        }
        if (hortaUpdateDTO.getIdTipoDeHorta() != null) {
            TipoDeHorta th = tipoDeHortaRepository.findById(hortaUpdateDTO.getIdTipoDeHorta())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Horta não encontrado."));
            hortaExistente.setTipoDeHorta(th);
        }

        if (novaImagem != null && !novaImagem.isEmpty()) {
            String nomeNovaImagem = fileStorageService.storeHortaImage(novaImagem);
            hortaExistente.setImagemCaminho(nomeNovaImagem);

            if (imagemAntiga != null && !imagemAntiga.isBlank() && !imagemAntiga.equals(PLACEHOLDER_IMAGE_FILENAME)) {
                fileStorageService.deleteHortaImage(imagemAntiga);
            }
        }

        return hortaRepository.save(hortaExistente);
    }

    @Transactional
    public void deletar(Integer id) {
        Horta hortaParaDeletar = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        String imagemParaDeletar = hortaParaDeletar.getImagemCaminho();
        hortaRepository.delete(hortaParaDeletar);

        if (imagemParaDeletar != null && !imagemParaDeletar.isBlank() && !imagemParaDeletar.equals(PLACEHOLDER_IMAGE_FILENAME)) {
            fileStorageService.deleteHortaImage(imagemParaDeletar);
        }
    }

    @Transactional
    public Horta alterarStatus(Integer id, Horta.StatusHorta status) {
        Horta horta = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));
        horta.setStatusHorta(status);
        return hortaRepository.save(horta);
    }

    @Transactional(readOnly = true)
    public List<HortaComUsuarioDTO> getPendingHortaRequests() {
        List<Horta> pendingHortas = hortaRepository.findByStatusHortaFetchingDetails(Horta.StatusHorta.PENDENTE);
        return hortaMapper.toHortaComUsuarioDTOList(pendingHortas);
    }

    @Transactional(readOnly = true)
    public List<HortaComUsuarioDTO> getHortasByStatusWithUserDetails(Horta.StatusHorta status) {
        List<Horta> hortas = hortaRepository.findByStatusHortaFetchingDetails(status);
        return hortaMapper.toHortaComUsuarioDTOList(hortas);
    }
}
