package br.com.planeta_atlantida.rbs.api.config;

import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Garante uma conta ADMIN conhecida em ambientes de desenvolvimento.
 *
 * O script de mock cria apenas o usuário do grupo, com um hash BCrypt cuja senha
 * não está documentada. Sem esta conta, um banco novo não tem como ser acessado,
 * porque não existe cadastro público nem recuperação de senha.
 *
 * A conta só é criada quando o e-mail configurado ainda não existe. Em produção,
 * desative com SEED_ADMIN=false.
 */
@Component
@RequiredArgsConstructor
public class DevDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.ativo:true}")
    private boolean ativo;

    @Value("${app.seed.admin.nome:Admin de desenvolvimento}")
    private String nome;

    @Value("${app.seed.admin.email:admin@planeta.dev}")
    private String email;

    @Value("${app.seed.admin.senha:admin123}")
    private String senha;

    @Override
    public void run(ApplicationArguments args) {

        if (!ativo) {
            return;
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            return;
        }

        Usuario admin = Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .tipo(TipoUsuario.ADMIN)
                .build();

        usuarioRepository.save(admin);

        log.warn(
                "Conta de desenvolvimento criada: {} / {} — desative com SEED_ADMIN=false",
                email,
                senha
        );
    }

}
