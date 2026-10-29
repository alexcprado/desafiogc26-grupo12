package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrocinioRepository extends JpaRepository<Patrocinio, Long> {

    boolean existsByEvento_IdAndPatrocinador_Cnpj(Long idEvento, String cnpj);

}
