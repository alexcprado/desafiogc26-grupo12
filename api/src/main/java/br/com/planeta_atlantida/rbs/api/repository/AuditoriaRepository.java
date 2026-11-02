package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    Page<Auditoria> findAll(Pageable pageable);

}
