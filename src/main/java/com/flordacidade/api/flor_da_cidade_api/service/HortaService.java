// Caminho do Arquivo: src/main/java/com/flordacidade/api/flor_da_cidade_api/service/HortaService.java

package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.model.TipoDeHorta;
import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.AreaClassificacaoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.AtividadesProdutivasRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.HortaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.TipoDeHortaRepository;
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
    private AreaClassificacaoRepository areaClassificacaoRepository;

    @Autowired
    private AtividadesProdutivasRepository atividadesProdutivasRepository;

    @Autowired
    private TipoDeHortaRepository tipoDeHortaRepository;

    @Autowired
    private ArquivoService fileStorageService;

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

    @Transactional
    public Horta salvar(Horta horta, MultipartFile imagem, Integer idUsuario, Integer idUnidadeEnsino,
            Integer idAreaClassificacao, Integer idAtividadesProdutivas, Integer idTipoDeHorta) {

        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + idUsuario));
        UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository.findById(idUnidadeEnsino)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Unidade de Ensino não encontrada com id: " + idUnidadeEnsino));
        AreaClassificacao area = areaClassificacaoRepository.findById(idAreaClassificacao)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Área de Classificação não encontrada com id: " + idAreaClassificacao));
        AtividadesProdutivas atividade = atividadesProdutivasRepository.findById(idAtividadesProdutivas)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Atividade Produtiva não encontrada com id: " + idAtividadesProdutivas));
        TipoDeHorta tipo = tipoDeHortaRepository.findById(idTipoDeHorta)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tipo de Horta não encontrado com id: " + idTipoDeHorta));

        horta.setUsuario(usuario);
        horta.setUnidadeDeEnsino(unidadeEnsino);
        horta.setAreaClassificacao(area);
        horta.setAtividadesProdutivas(atividade);
        horta.setTipoDeHorta(tipo);

        if (imagem != null && !imagem.isEmpty()) {
            String nomeArquivo = fileStorageService.storeHortaImage(imagem);
            horta.setImagemCaminho(nomeArquivo);
        } else {
            horta.setImagemCaminho(PLACEHOLDER_IMAGE_FILENAME);
        }

        horta.setStatusHorta(Horta.StatusHorta.PENDENTE);
        return hortaRepository.save(horta);
    }

    @Transactional
    public Horta atualizar(Integer id, Horta hortaAtualizadaInput, MultipartFile novaImagem, Integer idUsuario,
            Integer idUnidadeEnsino, Integer idAreaClassificacao, Integer idAtividadesProdutivas,
            Integer idTipoDeHorta) {
        Horta hortaExistente = hortaRepository.findByIdFetchingAllDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        String imagemAntiga = hortaExistente.getImagemCaminho();

        if (novaImagem != null && !novaImagem.isEmpty()) {
            if (imagemAntiga != null && !imagemAntiga.isBlank() && !PLACEHOLDER_IMAGE_FILENAME.equals(imagemAntiga)) {
                fileStorageService.deleteHortaImage(imagemAntiga);
            }
            String nomeNovaImagem = fileStorageService.storeHortaImage(novaImagem);
            hortaExistente.setImagemCaminho(nomeNovaImagem);
        }

        if (hortaAtualizadaInput.getNomeHorta() != null)
            hortaExistente.setNomeHorta(hortaAtualizadaInput.getNomeHorta());
        if (hortaAtualizadaInput.getFuncaoUniEnsino() != null)
            hortaExistente.setFuncaoUniEnsino(hortaAtualizadaInput.getFuncaoUniEnsino());
        if (hortaAtualizadaInput.getStatusHorta() != null)
            hortaExistente.setStatusHorta(hortaAtualizadaInput.getStatusHorta());
        if (hortaAtualizadaInput.getOcupacaoPrincipal() != null)
            hortaExistente.setOcupacaoPrincipal(hortaAtualizadaInput.getOcupacaoPrincipal());
        if (hortaAtualizadaInput.getEndereco() != null)
            hortaExistente.setEndereco(hortaAtualizadaInput.getEndereco());
        if (hortaAtualizadaInput.getEnderecoAlternativo() != null)
            hortaExistente.setEnderecoAlternativo(hortaAtualizadaInput.getEnderecoAlternativo());
        if (hortaAtualizadaInput.getTamanhoAreaProducao() != null)
            hortaExistente.setTamanhoAreaProducao(hortaAtualizadaInput.getTamanhoAreaProducao());
        if (hortaAtualizadaInput.getCaracteristicaGrupo() != null)
            hortaExistente.setCaracteristicaGrupo(hortaAtualizadaInput.getCaracteristicaGrupo());
        if (hortaAtualizadaInput.getQntPessoas() != null)
            hortaExistente.setQntPessoas(hortaAtualizadaInput.getQntPessoas());
        if (hortaAtualizadaInput.getAtividadeDescricao() != null)
            hortaExistente.setAtividadeDescricao(hortaAtualizadaInput.getAtividadeDescricao());
        if (hortaAtualizadaInput.getParceria() != null)
            hortaExistente.setParceria(hortaAtualizadaInput.getParceria());

        if (idUsuario != null) {
            UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuário de referência não encontrado com id: " + idUsuario));
            hortaExistente.setUsuario(usuario);
        }
        if (idUnidadeEnsino != null) {
            UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository.findById(idUnidadeEnsino)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Unidade de Ensino não encontrada com id: " + idUnidadeEnsino));
            hortaExistente.setUnidadeDeEnsino(unidadeEnsino);
        }
        if (idAreaClassificacao != null) {
            AreaClassificacao area = areaClassificacaoRepository.findById(idAreaClassificacao)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Área de Classificação não encontrada com id: " + idAreaClassificacao));
            hortaExistente.setAreaClassificacao(area);
        }
        if (idAtividadesProdutivas != null) {
            AtividadesProdutivas atividade = atividadesProdutivasRepository.findById(idAtividadesProdutivas)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Atividade Produtiva não encontrada com id: " + idAtividadesProdutivas));
            hortaExistente.setAtividadesProdutivas(atividade);
        }
        if (idTipoDeHorta != null) {
            TipoDeHorta tipo = tipoDeHortaRepository.findById(idTipoDeHorta)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Tipo de Horta não encontrado com id: " + idTipoDeHorta));
            hortaExistente.setTipoDeHorta(tipo);
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
        if (horta.getUsuario() != null && horta.getUsuario().getPessoa() != null &&
                horta.getUsuario().getPessoa().getNome() != null
                && !horta.getUsuario().getPessoa().getNome().trim().isEmpty()) {
            nomeUsuario = horta.getUsuario().getPessoa().getNome();
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
        if (horta.getUsuario() != null && horta.getUsuario().getPessoa() != null &&
                horta.getUsuario().getPessoa().getNome() != null
                && !horta.getUsuario().getPessoa().getNome().trim().isEmpty()) {
            nomeUsuario = horta.getUsuario().getPessoa().getNome();
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