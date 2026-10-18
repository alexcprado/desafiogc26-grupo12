package br.com.planeta_atlantida.rbs.api.domain;

import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import br.com.planeta_atlantida.rbs.api.enums.OrigemDocumento;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "documento",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_documento_chave_armazenamento",
                        columnNames = "chave_armazenamento"
                )
        }
)
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_patrocinio", nullable = false)
    private Patrocinio patrocinio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_envio", nullable = false)
    private Usuario usuarioEnvio;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30)
    private CategoriaDocumento categoria;

    @Column(
            name = "chave_armazenamento",
            nullable = false,
            unique = true,
            length = 500
    )
    private String chaveArmazenamento;

    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime;

    @Column(name = "tamanho_bytes", nullable = false)
    private Long tamanhoBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem", nullable = false, length = 20)
    private OrigemDocumento origem;

    @Column(
            name = "data_hora_criacao",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Instant dataHoraCriacao;

}
