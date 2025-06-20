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

import java.util.List;
import java.util.Optional;

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

    public List<Horta> listarTodas() {
        return hortaRepository.findAll();
    }

    public Optional<Horta> buscarPorId(Integer id) {
        return hortaRepository.findById(id);
    }

    @Transactional
    public Horta salvar(Horta horta, MultipartFile imagem, Integer idUsuario, Integer idUnidadeEnsino,
            Integer idAreaClassificacao, Integer idAtividadesProdutivas, Integer idTipoDeHorta) {

        // 1. Validação de IDs e busca das entidades relacionadas
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

    @Transactional // Garante que todas as operações de banco aconteçam em uma única transação
    public Horta atualizar(Integer id, Horta hortaAtualizada) {
        // 1. Busca a horta existente no banco. Se não encontrar, lança a exceção.
        Horta hortaExistente = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));

        // 2. Atualiza os campos simples (Strings, números, etc)
        // Adicionei verificações de nulidade para permitir atualizações parciais
        // (PATCH-style)
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
        if (hortaAtualizada.getUnidadeDeEnsino() != null)
            hortaExistente.setUnidadeDeEnsino(hortaAtualizada.getUnidadeDeEnsino());
        if (hortaAtualizada.getTamanhoAreaProducao() != null)
            hortaExistente.setTamanhoAreaProducao(hortaAtualizada.getTamanhoAreaProducao());
        if (hortaAtualizada.getAreaClassificacao() != null)
            hortaExistente.setAreaClassificacao(hortaAtualizada.getAreaClassificacao());
        if (hortaAtualizada.getAtividadesProdutivas() != null)
            hortaExistente.setAtividadesProdutivas(hortaAtualizada.getAtividadesProdutivas());
        if (hortaAtualizada.getCaracteristicaGrupo() != null)
            hortaExistente.setCaracteristicaGrupo(hortaAtualizada.getCaracteristicaGrupo());
        if (hortaAtualizada.getQntPessoas() != null)
            hortaExistente.setQntPessoas(hortaAtualizada.getQntPessoas());
        if (hortaAtualizada.getAtividadeDescricao() != null)
            hortaExistente.setAtividadeDescricao(hortaAtualizada.getAtividadeDescricao());
        if (hortaAtualizada.getImagemCaminho() != null)
            hortaExistente.setImagemCaminho(hortaAtualizada.getImagemCaminho());
        if (hortaAtualizada.getUsuario() != null)
            hortaExistente.setUsuario(hortaAtualizada.getUsuario());
        if (hortaAtualizada.getTipoDeHorta() != null)
            hortaExistente.setTipoDeHorta(hortaAtualizada.getTipoDeHorta());
        if (hortaAtualizada.getParceria() != null)
            hortaExistente.setParceria(hortaAtualizada.getParceria());

        // 3. Atualiza os relacionamentos buscando as entidades completas no banco
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

        // 4. Salva a entidade Horta com os dados e relacionamentos corretos
        return hortaRepository.save(hortaExistente);
    }

    public void deletar(Integer id) {
        if (!hortaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horta não encontrada com id: " + id);
        }
        hortaRepository.deleteById(id);
    }

    public Horta alterarStatus(Integer id, Horta.StatusHorta status) {
        Horta horta = hortaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horta não encontrada com id: " + id));
        horta.setStatusHorta(status);
        return hortaRepository.save(horta);
    }
}
