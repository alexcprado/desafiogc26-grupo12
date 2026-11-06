package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CortesiaRepository extends JpaRepository<Cortesia, Long> {

    List<Cortesia> findByPatrocinio_Id(Long idPatrocinio);

    Optional<Cortesia> findByIdAndPatrocinio_Id(Long idCortesia, Long idPatrocinio);

    boolean existsByPatrocinio_Id(Long idPatrocinio);

}
