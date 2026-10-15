package br.com.planeta_atlantida.rbs.api.domain;

import br.com.planeta_atlantida.rbs.api.enums.StatusTarefa;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_patrocinio", nullable = false)
    private Patrocinio patrocinio;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusTarefa status;

    @Column(name = "prazo", nullable = false)
    private LocalDate prazo;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false, insertable = false)
    private Instant dataHoraCriacao;

    @Column(name = "data_hora_alteracao")
    private Instant dataHoraAlteracao;

    @PrePersist
    private void prePersist() {
        this.status = StatusTarefa.PENDENTE;
    }

    @PreUpdate
    private void preUpdate() {
        this.dataHoraAlteracao = Instant.now();
    }

}
