package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrocinadorRepository extends JpaRepository<Patrocinador, Long> {}
