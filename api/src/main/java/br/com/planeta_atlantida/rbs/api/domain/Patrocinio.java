package br.com.planeta_atlantida.rbs.api.domain;

import br.com.planeta_atlantida.rbs.api.enums.CotaPatrocinio;
import br.com.planeta_atlantida.rbs.api.enums.StatusPatrocinador;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "patrocinio",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_patrocinio_evento_patrocinador",
                        columnNames = {"id_evento", "id_patrocinador"}
                )
        }
)
public class Patrocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_patrocinador", nullable = false)
    private Patrocinador patrocinador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CotaPatrocinio cota;

    @Column(
            name = "valor_contratado",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal valorContratado;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false, insertable = false)
    private Instant dataHoraCriacao;

    @Column(name = "data_hora_alteracao")
    private Instant dataHoraAlteracao;

    @PrePersist
    private void prePersist() {
        this.dataHoraCriacao = Instant.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.dataHoraAlteracao = Instant.now();
    }

}
