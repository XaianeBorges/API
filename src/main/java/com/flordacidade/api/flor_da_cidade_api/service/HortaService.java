// Caminho do Arquivo: src/main/java/com/flordacidade/api/flor_da_cidade_api/service/HortaService.java

package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.mapper.HortaMapper;
import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UnidadeEnsinoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HortaService {

    private static final String PLACEHOLDER_IMAGE_FILENAME = "folhin.png";

    @Autowired
    private HortaRepository hortaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UnidadeEnsinoRepository unidadeEnsinoRepository;

    @Autowired
    private ArquivoService fileStorageService;

    @Autowired
    private HortaMapper hortaMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // --- NOVO MÉTODO ADICIONADO PARA O MAPA PÚBLICO ---
    @Transactional(readOnly = true)
    public List<Horta> listarAtivasParaMapa() {
        // Reutiliza a query otimizada que já existe, buscando apenas as hortas ATIVAS.
        return hortaRepository.findByStatusHortaFetchingDetails(Horta.StatusHorta.ATIVA);
    }
    // --- FIM DA ADIÇÃO ---

    public List<Horta> listarTodas() {
        return hortaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Horta> buscarPorId(Integer id) {
        return hortaRepository.findByIdFetchingAllDetails(id);
    }

    // Metodo atualizado com DTO
    @Transactional
    public Horta salvar(HortaRequestDTO hortaDTO, MultipartFile imagem) {

        Horta novaHorta = hortaMapper.requestDtoToEntity(hortaDTO);

        UsuarioModel usuario = usuarioRepository.findById(hortaDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado..."));

        novaHorta.setUsuario(usuario);

        if (imagem != null && !imagem.isEmpty()) {
            String nomeArquivo = fileStorageService.storeHortaImage(imagem);
            novaHorta.setImagemCaminho(nomeArquivo);
        } else {
            novaHorta.setImagemCaminho("folhin.png"); // Imagem padrão
        }

        novaHorta.setStatusHorta(Horta.StatusHorta.PENDENTE);

        return hortaRepository.save(novaHorta);
    }

    // Metodo crriado/atualizado com DTO
    @Transactional
    public Horta atualizar(Integer id, HortaUpdateDTO hortaUpdateDTO, MultipartFile novaImagem) {
        Horta hortaExistente = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada..."));

        hortaMapper.updateEntityFromDto(hortaUpdateDTO, hortaExistente);

        if (hortaUpdateDTO.getIdUnidadeEnsino() != null) {
            UnidadeEnsino ue = unidadeEnsinoRepository.findById(hortaUpdateDTO.getIdUnidadeEnsino()).orElseThrow();
            hortaExistente.setUnidadeDeEnsino(ue);
        }
        if (novaImagem != null && !novaImagem.isEmpty()) {
        }
        return hortaRepository.save(hortaExistente);
    }

    @Transactional
    public void deletar(Integer id) {
        Horta hortaParaDeletar = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        String imagemParaDeletar = hortaParaDeletar.getImagemCaminho();
        hortaRepository.delete(hortaParaDeletar);

        if (imagemParaDeletar != null && !imagemParaDeletar.isBlank()
                && !PLACEHOLDER_IMAGE_FILENAME.equals(imagemParaDeletar)) {
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
    public List<Map<String, Object>> getPendingHortaRequests() {
        List<Horta> pendingHortas = hortaRepository.findByStatusHortaFetchingDetails(Horta.StatusHorta.PENDENTE);
        return pendingHortas.stream().map(this::mapHortaToPendingRequestDetails).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHortasByStatusWithUserDetails(Horta.StatusHorta status) {
        List<Horta> hortas = hortaRepository.findByStatusHortaFetchingDetails(status);
        return hortas.stream().map(this::mapHortaToUserDetails).collect(Collectors.toList());
    }

    private Map<String, Object> mapHortaToPendingRequestDetails(Horta horta) {
        String nomeUsuario = "Usuário Desconhecido";
        if (horta.getUsuario() != null && horta.getUsuario() != null &&
                horta.getUsuario().getNome() != null
                && !horta.getUsuario().getNome().trim().isEmpty()) {
            nomeUsuario = horta.getUsuario().getNome();
        }
        String enderecoHorta = horta.getEndereco() != null && !horta.getEndereco().trim().isEmpty()
                ? horta.getEndereco()
                : "Endereço não informado";
        String title = "Request - Horta de " + nomeUsuario + " - " + enderecoHorta;
        String requestDate = horta.getDataCriacao() != null ? horta.getDataCriacao().format(DATE_FORMATTER) : "N/A";
        String typeName = (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null)
                ? horta.getTipoDeHorta().getNome()
                : "Não especificado";

        return Map.of(
                "id", (Object) horta.getIdHorta(),
                "title", title,
                "date", requestDate,
                "type", typeName);
    }

    private Map<String, Object> mapHortaToUserDetails(Horta horta) {
        String nomeUsuario = "Usuário Desconhecido";
        if (horta.getUsuario() != null && horta.getUsuario() != null &&
                horta.getUsuario().getNome() != null
                && !horta.getUsuario().getNome().trim().isEmpty()) {
            nomeUsuario = horta.getUsuario().getNome();
        }
        String enderecoHorta = horta.getEndereco() != null && !horta.getEndereco().trim().isEmpty()
                ? horta.getEndereco()
                : "Endereço não informado";
        String nomeHortaDisplay = horta.getNomeHorta() != null && !horta.getNomeHorta().trim().isEmpty()
                ? horta.getNomeHorta()
                : "Horta Sem Nome";

        return Map.of(
                "id", (Object) horta.getIdHorta(),
                "nomeHorta", nomeHortaDisplay,
                "endereco", enderecoHorta,
                "proprietario", nomeUsuario,
                "status", horta.getStatusHorta().toString(),
                "tipo",
                (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null
                        ? horta.getTipoDeHorta().getNome()
                        : "Não especificado"));
    }
}