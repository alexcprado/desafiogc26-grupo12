package br.com.planeta_atlantida.rbs.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pesquisa_ativacao")
public class PesquisaAtivacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patrocinio", nullable = false, unique = true)
    private Patrocinio patrocinio;

    @Column(name = "pergunta_1", nullable = false, length = 500)
    private String pergunta1;

    @Column(name = "pergunta_2", nullable = false, length = 500)
    private String pergunta2;

    @Column(name = "pergunta_3", nullable = false, length = 500)
    private String pergunta3;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false)
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
