package br.com.planeta_atlantida.rbs.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
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

    @Column(name = "cota", nullable = false, length = 50)
    private String cota;

    @Column(
            name = "valor_contratado",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal valorContratado;

}
