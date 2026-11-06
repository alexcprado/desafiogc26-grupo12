package br.com.planeta_atlantida.rbs.api.domain;

import br.com.planeta_atlantida.rbs.api.enums.SetorCortesia;
import br.com.planeta_atlantida.rbs.api.enums.TipoCortesia;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cortesia")
public class Cortesia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patrocinio", nullable = false)
    private Patrocinio patrocinio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SetorCortesia setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCortesia tipo;

    @Column(nullable = false)
    private Integer quantidade;

}
