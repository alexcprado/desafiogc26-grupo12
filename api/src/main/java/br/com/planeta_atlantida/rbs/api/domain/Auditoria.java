package br.com.planeta_atlantida.rbs.api.domain;

import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "nome_usuario", nullable = false, length = 100)
    private String nomeUsuario;

    @Column(name = "email_usuario", nullable = false, length = 100)
    private String emailUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "acao", nullable = false, length = 50)
    private AcaoAuditoria acao;

    @Column(name = "nome_recurso")
    private String nomeRecurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidade", nullable = false, length = 50)
    private EntidadeAuditoria entidade;

    @Column(name = "id_identidade")
    private Long idEntidade;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private Instant dataHora;

    @PrePersist
    private void prePersist() {
        this.dataHora = Instant.now();
    }

}
