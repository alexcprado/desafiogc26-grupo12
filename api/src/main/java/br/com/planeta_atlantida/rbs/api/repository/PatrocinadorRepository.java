package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.enums.StatusPatrocinador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatrocinadorRepository extends JpaRepository<Patrocinador, Long> {

    Optional<Patrocinador> findByCnpj(String cnpj);

    Page<Patrocinador> findByStatus(StatusPatrocinador status, Pageable pageable);

}
