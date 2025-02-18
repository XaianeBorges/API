package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_equ")
public class TipoEqu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqTipoEqu; // Integer para corresponder ao auto-increment

    @Enumerated(EnumType.STRING)
    @Column(name = "descr_tipo_equ", nullable = false, length = 60)
    private TipoEquipamento descrTipoEqu; // Deve ser do tipo ENUM

    // Getters e Setters
    public Integer getSeqTipoEqu() {
        return seqTipoEqu;
    }

    public void setSeqTipoEqu(Integer seqTipoEqu) {
        this.seqTipoEqu = seqTipoEqu;
    }

    public TipoEquipamento getDescrTipoEqu() {
        return descrTipoEqu;
    }

    public void setDescrTipoEqu(TipoEquipamento descrTipoEqu) {
        this.descrTipoEqu = descrTipoEqu;
    }

}
