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
            String nomeArquivo = fileStorageService.storeFile(imagem);
            horta.setImagemCaminho(nomeArquivo);
        } else {
            throw new RuntimeException("A imagem da horta é obrigatória.");
        }
        horta.setStatusHorta(Horta.StatusHorta.PENDENTE);
        return hortaRepository.save(horta);
    }

    @Transactional
    public Horta atualizar(Integer id, Horta hortaAtualizada) {
        Horta hortaExistente = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        if (hortaAtualizada.getNomeHorta() != null)
            hortaExistente.setNomeHorta(hortaAtualizada.getNomeHorta());
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
        if (hortaAtualizada.getParceria() != null)
            hortaExistente.setParceria(hortaAtualizada.getParceria());

        if (hortaAtualizada.getUsuario() != null && hortaAtualizada.getUsuario().getIdUsuario() != null) {
            UsuarioModel usuario = usuarioRepository.findById(hortaAtualizada.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário de referência não encontrado com id: "
                            + hortaAtualizada.getUsuario().getIdUsuario()));
            hortaExistente.setUsuario(usuario);
        }
        if (hortaAtualizada.getUnidadeDeEnsino() != null
                && hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino() != null) {
            UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository
                    .findById(hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Unidade de Ensino de referência não encontrada com id: "
                                    + hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino()));
            hortaExistente.setUnidadeDeEnsino(unidadeEnsino);
        }
        if (hortaAtualizada.getAreaClassificacao() != null
                && hortaAtualizada.getAreaClassificacao().getIdAreaClassificacao() != null) {
            AreaClassificacao area = areaClassificacaoRepository
                    .findById(hortaAtualizada.getAreaClassificacao().getIdAreaClassificacao())
                    .orElseThrow(() -> new ResourceNotFoundException("Área de Classificação não encontrada com id: "
                            + hortaAtualizada.getAreaClassificacao().getIdAreaClassificacao()));
            hortaExistente.setAreaClassificacao(area);
        }
        if (hortaAtualizada.getAtividadesProdutivas() != null
                && hortaAtualizada.getAtividadesProdutivas().getIdAtividadesProdutivas() != null) {
            AtividadesProdutivas atividade = atividadesProdutivasRepository
                    .findById(hortaAtualizada.getAtividadesProdutivas().getIdAtividadesProdutivas())
                    .orElseThrow(() -> new ResourceNotFoundException("Atividade Produtiva não encontrada com id: "
                            + hortaAtualizada.getAtividadesProdutivas().getIdAtividadesProdutivas()));
            hortaExistente.setAtividadesProdutivas(atividade);
        }
        if (hortaAtualizada.getTipoDeHorta() != null && hortaAtualizada.getTipoDeHorta().getIdTipoDeHorta() != null) {
            TipoDeHorta tipo = tipoDeHortaRepository.findById(hortaAtualizada.getTipoDeHorta().getIdTipoDeHorta())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Horta não encontrada com id: "
                            + hortaAtualizada.getTipoDeHorta().getIdTipoDeHorta()));
            hortaExistente.setTipoDeHorta(tipo);
        }
        return hortaRepository.save(hortaExistente);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!hortaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horta não encontrada com id: " + id);
        }
        hortaRepository.deleteById(id);
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
        List<Horta> pendingHortas = hortaRepository.findByStatusHortaFetchingDetails(Horta.StatusHorta.PENDENTE); // Nome correto do método

        return pendingHortas.stream().map(horta -> {
            String nomeUsuario = "Usuário Desconhecido";
            if (horta.getUsuario() != null &&
                    horta.getUsuario().getPessoa() != null &&
                    horta.getUsuario().getPessoa().getNome() != null &&
                    !horta.getUsuario().getPessoa().getNome().trim().isEmpty()) {
                nomeUsuario = horta.getUsuario().getPessoa().getNome();
            }

            String enderecoHorta = "Endereço não informado";
            if (horta.getEndereco() != null && !horta.getEndereco().trim().isEmpty()) {
                enderecoHorta = horta.getEndereco();
            }

            String title = "Request - Horta de " + nomeUsuario + " - " + enderecoHorta;

            String requestDate = "N/A";
            if (horta.getDataCriacao() != null) {
                requestDate = horta.getDataCriacao().format(DATE_FORMATTER);
            }

            String typeName = "Não especificado";
            if (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null) {
                typeName = horta.getTipoDeHorta().getNome();
            }

            return Map.of(
                    "id", (Object) horta.getIdHorta(),
                    "title", title,
                    "date", requestDate,
                    "type", typeName
            );
        }).collect(Collectors.toList());
    }

    // Método para buscar hortas por status e retornar detalhes para o frontend
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHortasByStatusWithUserDetails(Horta.StatusHorta status) {
        // CORREÇÃO AQUI: Usar o nome do método que existe no HortaRepository
        List<Horta> hortas = hortaRepository.findByStatusHortaFetchingDetails(status); // <<< MUDANÇA AQUI

        return hortas.stream().map(horta -> {
            String nomeUsuario = "Usuário Desconhecido";
            if (horta.getUsuario() != null && horta.getUsuario().getPessoa() != null &&
                    horta.getUsuario().getPessoa().getNome() != null && !horta.getUsuario().getPessoa().getNome().trim().isEmpty()) {
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
                    "tipo", (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null ? horta.getTipoDeHorta().getNome() : "Não especificado")
            );
        }).collect(Collectors.toList());
    }
}