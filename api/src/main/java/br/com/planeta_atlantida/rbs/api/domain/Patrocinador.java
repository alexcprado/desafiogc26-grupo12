package br.com.planeta_atlantida.rbs.api.domain;

import br.com.planeta_atlantida.rbs.api.enums.StatusPatrocinador;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patrocinador")
public class Patrocinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "cnpj", nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "segmento")
    private String segmento;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private StatusPatrocinador status;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false)
    private Instant dataHoraCriacao;

    @Column(name = "data_hora_alteracao")
    private Instant dataHoraAlteracao;

    @PrePersist
    private void prePersist() {
        this.dataHoraCriacao = Instant.now();
        this.status = StatusPatrocinador.ATIVO;
    }

    @PreUpdate
    private void preUpdate() {
        this.dataHoraAlteracao = Instant.now();
    }

}
