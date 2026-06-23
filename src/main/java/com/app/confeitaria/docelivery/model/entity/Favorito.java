package com.app.confeitaria.docelivery.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorito", 
       indexes = {
           @Index(name = "idx_favorito_usuario", columnList = "usuario_id"),
           @Index(name = "idx_favorito_tipo", columnList = "tipo_favorito"),
           @Index(name = "idx_favorito_usuario_tipo_ref", 
                   columnList = "usuario_id,tipo_favorito,referencia_id", unique = true)
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"usuario"})
@ToString(exclude = {"usuario"})
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Usuario usuario;

    @Column(name = "tipo_favorito", nullable = false, length = 20)
    private String tipo;

    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Favorito(Usuario usuario, String tipo, Long referenciaId) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.dataCriacao = LocalDateTime.now();
    }
}
