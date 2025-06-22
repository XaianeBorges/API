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

import java.time.format.DateTimeFormatter; // Mantido da Esquerda
import java.util.List;
import java.util.Map;                     // Mantido da Esquerda
import java.util.Optional;
import java.util.stream.Collectors;       // Mantido da Esquerda

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

    // Mantido da Esquerda
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

        // 1. Validação de IDs e busca das entidades relacionadas (Lógica da Direita/Igual)
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

        // 2. Associa as entidades encontradas ao objeto Horta
        horta.setUsuario(usuario);
        horta.setUnidadeDeEnsino(unidadeEnsino);
        horta.setAreaClassificacao(area);
        horta.setAtividadesProdutivas(atividade);
        horta.setTipoDeHorta(tipo);

        // 3. Salva o arquivo de imagem no disco
        if (imagem != null && !imagem.isEmpty()) {
            String nomeArquivo = fileStorageService.storeFile(imagem);
            horta.setImagemCaminho(nomeArquivo);
        } else {
            // Se a imagem for obrigatória, lance uma exceção aqui
            throw new RuntimeException("A imagem da horta é obrigatória.");
        }

        // 4. Define o status inicial como PENDENTE por padrão
        horta.setStatusHorta(Horta.StatusHorta.PENDENTE);

        // 5. Salva a entidade Horta completa no banco de dados
        return hortaRepository.save(horta);
    }

    // Usando a versão da DIREITA para 'atualizar' por ser mais completa (trata imagem e IDs)
    @Transactional
    public Horta atualizar(Integer id, Horta hortaAtualizada, MultipartFile novaImagem, Integer idUsuario,
                           Integer idUnidadeEnsino, Integer idAreaClassificacao, Integer idAtividadesProdutivas,
                           Integer idTipoDeHorta) {
        // 1. Busca a horta existente no banco. Se não encontrar, lança a exceção.
        Horta hortaExistente = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        // Guarda o nome da imagem antiga para possível exclusão
        String imagemAntiga = hortaExistente.getImagemCaminho();

        // 2. Lógica de atualização da imagem
        if (novaImagem != null && !novaImagem.isEmpty()) {
            // Se uma nova imagem foi enviada, deleta a antiga (se existir)
            if (imagemAntiga != null && !imagemAntiga.isBlank()) {
                fileStorageService.deleteFile(imagemAntiga);
            }
            // Salva a nova imagem e atualiza o caminho no objeto
            String nomeNovaImagem = fileStorageService.storeFile(novaImagem);
            hortaExistente.setImagemCaminho(nomeNovaImagem);
        }
        // Se nenhuma nova imagem for enviada, o campo `imagemCaminho` da hortaExistente
        // permanece intacto.

        // 2. Atualiza os campos simples (Strings, números, etc)
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
        // A lógica da DIREITA para atualizar entidades como UnidadeDeEnsino, AreaClassificacao etc.
        // baseia-se nos IDs fornecidos em 'hortaAtualizada.getEntidade().getIdEntidade()'.
        // Os parâmetros idUsuario, idUnidadeEnsino etc. na assinatura deste método da DIREITA
        // não são usados diretamente para buscar as entidades na lógica original da DIREITA.
        // A atualização dos campos de relacionamento abaixo refletem a lógica original da DIREITA.

        if (hortaAtualizada.getTamanhoAreaProducao() != null)
            hortaExistente.setTamanhoAreaProducao(hortaAtualizada.getTamanhoAreaProducao());
        if (hortaAtualizada.getCaracteristicaGrupo() != null)
            hortaExistente.setCaracteristicaGrupo(hortaAtualizada.getCaracteristicaGrupo());
        if (hortaAtualizada.getQntPessoas() != null)
            hortaExistente.setQntPessoas(hortaAtualizada.getQntPessoas());
        if (hortaAtualizada.getAtividadeDescricao() != null)
            hortaExistente.setAtividadeDescricao(hortaAtualizada.getAtividadeDescricao());
        // Se novaImagem não foi fornecida, mas hortaAtualizada tem um caminho, respeitar (embora incomum com MultipartFile)
        if (novaImagem == null || novaImagem.isEmpty()) {
            if (hortaAtualizada.getImagemCaminho() != null)
                hortaExistente.setImagemCaminho(hortaAtualizada.getImagemCaminho());
        }
        if (hortaAtualizada.getParceria() != null)
            hortaExistente.setParceria(hortaAtualizada.getParceria());

        // 3. Atualiza os relacionamentos buscando as entidades completas no banco (usando IDs de hortaAtualizada)
        // Se os parâmetros idUsuario, idUnidadeEnsino etc. devessem ter precedência, a lógica abaixo mudaria.
        // Mantendo a lógica original da versão da Direita:
        if (idUsuario != null) { // Prioriza o ID do parâmetro se fornecido
            UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário de referência não encontrado com id: " + idUsuario));
            hortaExistente.setUsuario(usuario);
        } else if (hortaAtualizada.getUsuario() != null && hortaAtualizada.getUsuario().getIdUsuario() != null) {
            UsuarioModel usuario = usuarioRepository.findById(hortaAtualizada.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário de referência não encontrado com id: "
                            + hortaAtualizada.getUsuario().getIdUsuario()));
            hortaExistente.setUsuario(usuario);
        }


        if (idUnidadeEnsino != null) { // Prioriza o ID do parâmetro se fornecido
            UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository.findById(idUnidadeEnsino)
                    .orElseThrow(() -> new ResourceNotFoundException("Unidade de Ensino de referência não encontrada com id: " + idUnidadeEnsino));
            hortaExistente.setUnidadeDeEnsino(unidadeEnsino);
        } else if (hortaAtualizada.getUnidadeDeEnsino() != null
                && hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino() != null) {
            UnidadeEnsino unidadeEnsino = unidadeEnsinoRepository
                    .findById(hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Unidade de Ensino de referência não encontrada com id: "
                                    + hortaAtualizada.getUnidadeDeEnsino().getIdUnidadeDeEnsino()));
            hortaExistente.setUnidadeDeEnsino(unidadeEnsino);
        }

        if (idAreaClassificacao != null) { // Prioriza o ID do parâmetro se fornecido
            AreaClassificacao area = areaClassificacaoRepository.findById(idAreaClassificacao)
                    .orElseThrow(() -> new ResourceNotFoundException("Área de Classificação não encontrada com id: " + idAreaClassificacao));
            hortaExistente.setAreaClassificacao(area);
        } else if (hortaAtualizada.getAreaClassificacao() != null
                && hortaAtualizada.getAreaClassificacao().getIdAreaClassificacao() != null) {
            AreaClassificacao area = areaClassificacaoRepository
                    .findById(hortaAtualizada.getAreaClassificacao().getIdAreaClassificacao())
                    .orElseThrow(() -> new ResourceNotFoundException("Área de Classificação não encontrada com id: "
                            + hortaAtualizada.getAreaClassificacao().getIdAreaClassificacao()));
            hortaExistente.setAreaClassificacao(area);
        }


        if (idAtividadesProdutivas != null) { // Prioriza o ID do parâmetro se fornecido
            AtividadesProdutivas atividade = atividadesProdutivasRepository.findById(idAtividadesProdutivas)
                    .orElseThrow(() -> new ResourceNotFoundException("Atividade Produtiva não encontrada com id: " + idAtividadesProdutivas));
            hortaExistente.setAtividadesProdutivas(atividade);
        } else if (hortaAtualizada.getAtividadesProdutivas() != null
                && hortaAtualizada.getAtividadesProdutivas().getIdAtividadesProdutivas() != null) {
            AtividadesProdutivas atividade = atividadesProdutivasRepository
                    .findById(hortaAtualizada.getAtividadesProdutivas().getIdAtividadesProdutivas())
                    .orElseThrow(() -> new ResourceNotFoundException("Atividade Produtiva não encontrada com id: "
                            + hortaAtualizada.getAtividadesProdutivas().getIdAtividadesProdutivas()));
            hortaExistente.setAtividadesProdutivas(atividade);
        }

        if (idTipoDeHorta != null) { // Prioriza o ID do parâmetro se fornecido
            TipoDeHorta tipo = tipoDeHortaRepository.findById(idTipoDeHorta)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Horta não encontrada com id: " + idTipoDeHorta));
            hortaExistente.setTipoDeHorta(tipo);
        } else if (hortaAtualizada.getTipoDeHorta() != null && hortaAtualizada.getTipoDeHorta().getIdTipoDeHorta() != null) {
            TipoDeHorta tipo = tipoDeHortaRepository.findById(hortaAtualizada.getTipoDeHorta().getIdTipoDeHorta())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Horta não encontrada com id: "
                            + hortaAtualizada.getTipoDeHorta().getIdTipoDeHorta()));
            hortaExistente.setTipoDeHorta(tipo);
        }

        // 4. Salva a entidade Horta com os dados e relacionamentos corretos
        return hortaRepository.save(hortaExistente);
    }

    // Usando a versão da DIREITA para 'deletar' (inclui deleção de arquivo)
    @Transactional
    public void deletar(Integer id) {
        Horta hortaParaDeletar = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        // Deleta o arquivo de imagem se existir
        if (hortaParaDeletar.getImagemCaminho() != null && !hortaParaDeletar.getImagemCaminho().isBlank()) {
            fileStorageService.deleteFile(hortaParaDeletar.getImagemCaminho());
        }

        hortaRepository.delete(hortaParaDeletar);
    }

    // Lógica idêntica, adicionado @Transactional da Esquerda
    @Transactional
    public Horta alterarStatus(Integer id, Horta.StatusHorta status) {
        Horta horta = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));
        horta.setStatusHorta(status);
        return hortaRepository.save(horta);
    }

    // Método mantido da Esquerda
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPendingHortaRequests() {
        List<Horta> pendingHortas = hortaRepository.findByStatusHortaFetchingDetails(Horta.StatusHorta.PENDENTE);

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
                    "type", typeName);
        }).collect(Collectors.toList());
    }

    // Método mantido da Esquerda
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHortasByStatusWithUserDetails(Horta.StatusHorta status) {
        List<Horta> hortas = hortaRepository.findByStatusHortaFetchingDetails(status);

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
                    "tipo", (horta.getTipoDeHorta() != null && horta.getTipoDeHorta().getNome() != null ? horta.getTipoDeHorta().getNome() : "Não especificado"));
        }).collect(Collectors.toList());
    }
}