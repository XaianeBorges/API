package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "tecnico")
public class TecnicoModel {

    public enum TecnicoStatus {
        ATIVO,
        INATIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Integer idTecnico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TecnicoStatus status = TecnicoStatus.ATIVO;

    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "nome", nullable = false, length = 45)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regiao_id", nullable = false)
    private RegiaoModel regiao;

    // NOVO CAMPO ADICIONADO
    @Column(name = "is_adm", nullable = false)
    private boolean isAdm = false;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O formato do e-mail é inválido.")
    @Size(max = 255)
    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "redefinir_senha_token")
    private String resetPasswordToken;

    @Column(name = "redefinir_senha_token_expirado")
    private LocalDateTime resetPasswordTokenExpiry;

}