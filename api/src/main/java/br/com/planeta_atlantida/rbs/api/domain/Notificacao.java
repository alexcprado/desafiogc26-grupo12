package br.com.planeta_atlantida.rbs.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_patrocinador", nullable = false)
    private Patrocinador patrocinador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_administrador", nullable = false)
    private Usuario usuarioAdministrador;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "conteudo", nullable = false, length = 100, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false, insertable = false)
    private Instant dataHoraCriacao;
}
