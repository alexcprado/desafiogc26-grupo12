package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatrocinioRepository extends JpaRepository<Patrocinio, Long> {

    boolean existsByEvento_IdAndPatrocinador_Id(Long idEvento, Long idPatrocinador);

    Page<Patrocinio> findByEvento_Id(Long idEvento, Pageable pageable);

    List<Patrocinio> findByPatrocinador_IdOrderByEvento_AnoDesc(Long idPatrocinador);

}
