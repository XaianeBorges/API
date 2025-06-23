package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.ResourceNotFoundException;
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
    private FileStorageService fileStorageService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Horta> listarTodas() {
        return hortaRepository.findAll();
    }

    public Optional<Horta> buscarPorId(Integer id) {
        return hortaRepository.findById(id);
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
            // Se nenhuma imagem for fornecida, usa o placeholder
            horta.setImagemCaminho(PLACEHOLDER_IMAGE_FILENAME);
        }

        horta.setStatusHorta(Horta.StatusHorta.PENDENTE);
        return hortaRepository.save(horta);
    }

    @Transactional
    public Horta atualizar(Integer id, Horta hortaAtualizada, MultipartFile novaImagem, Integer idUsuario,
                           Integer idUnidadeEnsino, Integer idAreaClassificacao, Integer idAtividadesProdutivas,
                           Integer idTipoDeHorta) {
        Horta hortaExistente = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        String imagemAntiga = hortaExistente.getImagemCaminho();

        if (novaImagem != null && !novaImagem.isEmpty()) {
            // Se a imagem antiga não era o placeholder e existia, deleta-a
            if (imagemAntiga != null && !imagemAntiga.isBlank() && !PLACEHOLDER_IMAGE_FILENAME.equals(imagemAntiga)) {
                fileStorageService.deleteHortaImage(imagemAntiga);
            }
            String nomeNovaImagem = fileStorageService.storeHortaImage(novaImagem);
            hortaExistente.setImagemCaminho(nomeNovaImagem);
        }
        // Se 'novaImagem' for nula e você quiser permitir que o usuário remova a imagem atual
        // (voltando para o placeholder), você precisaria de um sinal explícito do frontend.
        // Exemplo: se hortaAtualizada.getImagemCaminho() for uma string especial como "REMOVER_IMAGEM"
        // ou se um parâmetro booleano for enviado.
        // Por agora, se novaImagem for nula, a imagem existente (seja ela qual for) é mantida.

        // ... (atualização dos outros campos da horta) ...
        if (hortaAtualizada.getNomeHorta() != null)
            hortaExistente.setNomeHorta(hortaAtualizada.getNomeHorta());
        // ... (copie todos os outros setters daqui para baixo como estavam antes)
        if (hortaAtualizada.getFuncaoUniEnsino() != null)
            hortaExistente.setFuncaoUniEnsino(hortaAtualizada.getFuncaoUniEnsino());
        if (hortaAtualizada.getStatusHorta() != null)
            hortaExistente.setStatusHorta(hortaAtualizada.getStatusHorta());
        if (hortaAtualizada.getOcupacaoPrincipal() != null)
            hortaExistente.setOcupacaoPrincipal(hortaAtualizada.getOcupacaoPrincipal());
        if (hortaAtualizada.getEndereco() != null)
            hortaExistente.setEndereco(hortaAtualizada.getEndereco());
        if (hortaAtualizada.getEnderecoAlternativo() != null)
            hortaExistente.setEnderecoAlternativo(hortaAtualizada.getEnderecoAlternativo());
        if (hortaAtualizada.getTamanhoAreaProducao() != null)
            hortaExistente.setTamanhoAreaProducao(hortaAtualizada.getTamanhoAreaProducao());
        if (hortaAtualizada.getCaracteristicaGrupo() != null)
            hortaExistente.setCaracteristicaGrupo(hortaAtualizada.getCaracteristicaGrupo());
        if (hortaAtualizada.getQntPessoas() != null)
            hortaExistente.setQntPessoas(hortaAtualizada.getQntPessoas());
        if (hortaAtualizada.getAtividadeDescricao() != null)
            hortaExistente.setAtividadeDescricao(hortaAtualizada.getAtividadeDescricao());
        if ((novaImagem == null || novaImagem.isEmpty()) && hortaAtualizada.getImagemCaminho() != null) {
            // Esta lógica parece redundante se a imagem só muda se novaImagem for fornecida.
            // Se o objetivo é permitir que hortaAtualizada.getImagemCaminho() defina o nome
            // diretamente (ex: para resetar para placeholder via DTO), então deve ser mantido,
            // mas cuidado para não sobrescrever uma imagem recém-salva se novaImagem foi processada.
            // A lógica atual de imagem já cobre o upload de novaImagem.
            // Se hortaAtualizada.getImagemCaminho() for "folhin.png" e novaImagem for nula,
            // e a imagem existente não for "folhin.png", você pode querer deletar a antiga.
            // Vou simplificar: a imagem só muda se `novaImagem` for fornecida.
        }
        if (hortaAtualizada.getParceria() != null)
            hortaExistente.setParceria(hortaAtualizada.getParceria());

        // Atualização dos relacionamentos (como estava antes)
        if (idUsuario != null) {
            UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário de referência não encontrado com id: " + idUsuario));
            hortaExistente.setUsuario(usuario);
        } else if (hortaAtualizada.getUsuario() != null && hortaAtualizada.getUsuario().getIdUsuario() != null) {
            UsuarioModel usuario = usuarioRepository.findById(hortaAtualizada.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário de referência não encontrado com id: "
                            + hortaAtualizada.getUsuario().getIdUsuario()));
            hortaExistente.setUsuario(usuario);
        }
        // ... (copie a lógica de atualização para UnidadeEnsino, AreaClassificacao, AtividadesProdutivas, TipoDeHorta)
        if (idUnidadeEnsino != null) {
            UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository.findById(idUnidadeEnsino)
                    .orElseThrow(() -> new ResourceNotFoundException("Unidade de Ensino de referência não encontrada com id: " + idUnidadeEnsino));
            hortaExistente.setUnidadeDeEnsino(unidadeEnsino);
        } else if (hortaAtualizada.getUnidadeDeEnsino() != null
                && hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino() != null) {
            // ... (código original)
        } // ... e assim por diante para os outros relacionamentos


        return hortaRepository.save(hortaExistente);
    }

    @Transactional
    public void deletar(Integer id) {
        Horta hortaParaDeletar = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        String imagemParaDeletar = hortaParaDeletar.getImagemCaminho();

        hortaRepository.delete(hortaParaDeletar);

        // Deleta a imagem física APENAS se não for o placeholder e se existir
        if (imagemParaDeletar != null && !imagemParaDeletar.isBlank() && !PLACEHOLDER_IMAGE_FILENAME.equals(imagemParaDeletar)) {
            fileStorageService.deleteHortaImage(imagemParaDeletar);
        }
    }

    // ... (getPendingHortaRequests, getHortasByStatusWithUserDetails, alterarStatus como estavam antes) ...
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
                horta.getUsuario().getPessoa().getNome() != null && !horta.getUsuario().getPessoa().getNome().trim().isEmpty()) {
            nomeUsuario = horta.getUsuario().getPessoa().getNome();
        }
        String enderecoHorta = horta.getEndereco() != null && !horta.getEndereco().trim().isEmpty() ? horta.getEndereco() : "Endereço não informado";
        String title = "Request - Horta de " + nomeUsuario + " - " + enderecoHorta;
        String requestDate = horta.getDataCriacao() != null ? horta.getDataCriacao().format(DATE_FORMATTER) : "N/A";
        String typeName = (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null) ? horta.getTipoDeHorta().getNome() : "Não especificado";

        return Map.of(
                "id", (Object) horta.getIdHorta(),
                "title", title,
                "date", requestDate,
                "type", typeName);
    }

    private Map<String, Object> mapHortaToUserDetails(Horta horta) {
        String nomeUsuario = "Usuário Desconhecido";
        if (horta.getUsuario() != null && horta.getUsuario().getPessoa() != null &&
                horta.getUsuario().getPessoa().getNome() != null && !horta.getUsuario().getPessoa().getNome().trim().isEmpty()) {
            nomeUsuario = horta.getUsuario().getPessoa().getNome();
        }
        String enderecoHorta = horta.getEndereco() != null && !horta.getEndereco().trim().isEmpty() ? horta.getEndereco() : "Endereço não informado";
        String nomeHortaDisplay = horta.getNomeHorta() != null && !horta.getNomeHorta().trim().isEmpty() ? horta.getNomeHorta() : "Horta Sem Nome";

        return Map.of(
                "id", (Object) horta.getIdHorta(),
                "nomeHorta", nomeHortaDisplay,
                "endereco", enderecoHorta,
                "proprietario", nomeUsuario,
                "status", horta.getStatusHorta().toString(),
                "tipo", (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null ? horta.getTipoDeHorta().getNome() : "Não especificado")
        );
    }
}